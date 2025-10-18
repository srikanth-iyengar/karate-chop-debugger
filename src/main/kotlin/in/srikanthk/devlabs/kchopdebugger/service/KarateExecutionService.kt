package `in`.srikanthk.devlabs.kchopdebugger.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.xdebugger.XDebuggerManager
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebugMessageBus
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.agent.communication.DebugServer
import `in`.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest
import `in`.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse
import `in`.srikanthk.devlabs.kchopdebugger.configuration.KaratePropertiesState
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import org.jetbrains.idea.maven.execution.MavenRunner
import org.jetbrains.idea.maven.execution.MavenRunnerParameters
import org.jetbrains.idea.maven.project.MavenProjectsManager
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class KarateExecutionService(val project: Project) {
    val mapper = ObjectMapper()
    private val responsePublisher = project.messageBus.syncPublisher(DebuggerInfoResponseTopic.TOPIC)
    private val runPropertiesService = KaratePropertiesState.getInstance(project)
    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("Karate Chop Debugger Notification")
    val projectBasePath = project.basePath + Constants.JAVA_BASE_PATH
    val breakpointManager = XDebuggerManager.getInstance(project).breakpointManager

    private var process: Process? = null
    var lastExecutedFileName: String? = null
    var lastScenarioName: String? = null

    fun getBreakpoints() : Map<String, ConcurrentSkipListSet<*>>{
        val breakpoints = HashMap<String, ConcurrentSkipListSet<Int>>()
        breakpointManager.allBreakpoints.forEach {
            it.sourcePosition?.let { sourcePosition ->
                val list = breakpoints.computeIfAbsent(sourcePosition.file.path) { ConcurrentSkipListSet() };
                list.add(sourcePosition.line + 1)
            }
        }
        return breakpoints
    }

    fun executeSuite(fileName: String, scenarioName: String?) {
        if(this.process != null) {
            notificationGroup
                .createNotification(
                    "Debugger already running",
                    "A debugging session is already in progress. Stop it before starting a new one.",NotificationType.WARNING)
                .notify(project)
            return
        }
        this.lastExecutedFileName = fileName
        this.lastScenarioName = scenarioName
        buildMavenProject {
            ApplicationManager.getApplication().executeOnPooledThread {
                val featureClasspath = fileName.substring(projectBasePath.length + 1)

                val urls = getMavenDependenciesURL().joinToString(";");
                val breakpointJson = mapper.writeValueAsString(getBreakpoints())
                val breakpointPath = Files.createTempFile("breakpoints_", ".json")
                Files.writeString(
                    breakpointPath,
                    breakpointJson,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                )

                val subscriber = createRemoteCallSubscriber();
                DebugMessageBus.getInstance().subscribe(DebugResponse.TOPIC, subscriber);

                val remotePublisher = DebugMessageBus.getInstance().publisher(DebugRequest.TOPIC);

                val messageBus = project.messageBus.connect();
                messageBus.subscribe(DebuggerInfoRequestTopic.TOPIC, object : DebuggerInfoRequestTopic {
                    override fun publishKarateVariables() {
                        remotePublisher.publishKarateVariables();
                    }

                    override fun stepForward() {
                        remotePublisher.stepInto()
                    }

                    override fun stepOver() {
                        remotePublisher.stepOver()
                    }

                    override fun resume() {
                        remotePublisher.resume()
                    }

                    override fun evaluateExpression(expression: String) {
                        remotePublisher.evaluateExpression(expression)
                    }

                    override fun addBreakpoint(fileName: String, lineNumber: Int) {
                        remotePublisher.addBreakpoint(fileName, lineNumber)
                    }

                    override fun removeBreakpoint(fileName: String, lineNumber: Int) {
                        remotePublisher.removeBreakpoint(fileName, lineNumber)
                    }
                })

                val debugServer = DebugServer.getInstance().start()

                val vmOptions = buildString {
                    runPropertiesService?.state?.state?.entries?.forEach { entry ->
                        append("\"-D${entry.key}=${entry.value}\" ")
                    }
                    append("-Ddebug.port=${debugServer.port}")
                }.trim()

                val options = "$vmOptions -jar ${getAgentJarFile().path} $featureClasspath ${project.basePath} $breakpointPath $urls ${scenarioName ?: ""}"
                val argumentPath = Files.createTempFile("argument", ".txt")
                Files.writeString(
                    argumentPath,
                    options.trim(),
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                )

                val command =
                    "${getProjectJavaExecutable(project)} @${argumentPath}"

                this.process =
                    ProcessBuilder(*command.split(" ").toTypedArray())
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .start();
                val stdout = BufferedReader(InputStreamReader(process?.inputStream))
                val stderr = BufferedReader(InputStreamReader(process?.errorStream))

                // Read stdout in its own thread
                Thread {
                    stdout.forEachLine { line ->
                        responsePublisher.appendLog(line, true)
                    }
                }.start()

                // Read stderr in its own thread
                Thread {
                    stderr.forEachLine { line ->
                        responsePublisher.appendLog(line, false)
                    }
                }.start()
                process?.waitFor()

                debugServer.stop()
                DebugMessageBus.getInstance().clearAll()
                messageBus.disconnect()
                Files.deleteIfExists(breakpointPath)
                this.process = null
            }
        }
    }

    fun getProjectJavaExecutable(project: Project): String {
        val sdk = ProjectRootManager.getInstance(project).projectSdk
            ?: throw IllegalStateException("❌ No SDK configured for the project")

        val javaHome = sdk.homePath
            ?: throw IllegalStateException("❌ SDK has no home path")

        return Paths.get(javaHome, "bin", "java").toString()
    }

    fun stop() {
        process?.destroyForcibly()
        process = null
        responsePublisher.updateState(DebuggerState.Finished)
    }

    fun rerun() {
        lastExecutedFileName?.let { executeSuite(it, this.lastScenarioName) }
    }

    private fun buildMavenProject(callback: Runnable): Boolean {
        val mavenProjectManager = MavenProjectsManager.getInstance(project)
        val mavenProjects = mavenProjectManager.projects

        if (mavenProjects.isEmpty()) {
            notificationGroup.createNotification(
                "Karate Chop Error",
                "No Maven projects detected",
                NotificationType.ERROR
            ).notify(project)
            return false
        }

        val mavenProject = mavenProjects.first()

        // ✅ Continue Maven build
        val runner = MavenRunner.getInstance(project)
        val pomFile = File(mavenProject.file.path)
        val parameters = MavenRunnerParameters(
            pomFile.parent,
            pomFile.name,
            true,
            listOf("package", "-DskipTests=true"),
            emptyMap()
        )

        runner.run(parameters, null, callback)
        return true
    }

    private fun getMavenDependenciesURL(): List<String> {
        val mavenProjectManager = MavenProjectsManager.getInstance(project);
        val dependencies = ArrayList(mavenProjectManager.projects[0].dependencies.map { dep -> dep.file.path });
        dependencies.add(File(getTestClassesPath()).path);

        return dependencies
    }

    private fun getTestClassesPath(): String {
        val testClassDir = File(File(project.basePath, "target"), "test-classes")

        return testClassDir.path.toString()
    }

    fun createRemoteCallSubscriber(): DebugResponse {
        return object : DebugResponse {
            override fun updateKarateVariable(vars: HashMap<String, String>) {
                val objectMapper = ObjectMapper()
                val parsedVars = HashMap<String, Map<String, Object>>();
                for ((key, json) in vars) {
                    try {
                        val parsed = objectMapper.readValue(json, object : TypeReference<Map<String, Object>>() {})
                        parsedVars[key] = parsed
                    } catch (e: Exception) {
                        // Optional: log or handle the malformed JSON case
                    }
                }
                responsePublisher.updateKarateVariables(parsedVars);
            }

            override fun updateState(state: DebuggerState) {
                responsePublisher.updateState(state)
            }

            override fun navigateTo(filePath: String, lineNumber: Int) {
                responsePublisher.navigateTo(filePath, lineNumber);
            }

            override fun appendLog(log: String, isSuccess: Boolean) {
            }

            override fun evaluationResult(
                result: String,
                error: String
            ) {
                responsePublisher.evaluateExpressionResult(result, error)
            }

        }
    }

    fun getAgentJarFile(): File {
        val plugin = PluginManagerCore.getPlugin(PluginId.getId("in.srikanthk.devlabs.karate-chop-debugger"))
        val jarFileName = "debug-agent-${plugin?.version}.jar"
        val pluginPath = plugin?.pluginPath
        return File(File(pluginPath?.pathString, "lib"), jarFileName)
    }
}
