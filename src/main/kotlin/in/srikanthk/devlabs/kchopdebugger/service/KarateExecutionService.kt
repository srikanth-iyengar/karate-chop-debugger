package `in`.srikanthk.devlabs.kchopdebugger.service

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.xdebugger.XDebuggerManager
import `in`.srikanthk.devlabs.kchopdebugger.agent.BreakpointFileCodec
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebugMessageBus
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.agent.KarateVariableSnapshot
import `in`.srikanthk.devlabs.kchopdebugger.agent.communication.DebugServer
import `in`.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest
import `in`.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse
import `in`.srikanthk.devlabs.kchopdebugger.configuration.KaratePropertiesState
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import org.jetbrains.idea.maven.execution.MavenRunner
import org.jetbrains.idea.maven.execution.MavenRunnerParameters
import org.jetbrains.idea.maven.project.MavenProjectsManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class KarateExecutionService(val project: Project) {
    private val responsePublisher = project.messageBus.syncPublisher(DebuggerInfoResponseTopic.TOPIC)
    private val runPropertiesService = KaratePropertiesState.getInstance(project)
    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("Karate Chop Debugger Notification")
    val projectBasePath = project.basePath + Constants.JAVA_BASE_PATH
    val breakpointManager = XDebuggerManager.getInstance(project).breakpointManager

    private var process: Process? = null
    var lastExecutedFileName: String? = null
    var lastScenarioName: String? = null

    /**
     * Collects all editor breakpoints and groups them by source file path.
     *
     * @return A map where each key is an absolute file path and each value is a sorted set of 1-based line numbers with breakpoints in that file.
     */
    fun getBreakpoints(): Map<String, ConcurrentSkipListSet<Int>> {
        val breakpoints = HashMap<String, ConcurrentSkipListSet<Int>>()
        breakpointManager.allBreakpoints.forEach {
            it.sourcePosition?.let { sourcePosition ->
                val list = breakpoints.computeIfAbsent(sourcePosition.file.path) { ConcurrentSkipListSet() }
                list.add(sourcePosition.line + 1)
            }
        }
        return breakpoints
    }

    /**
     * Starts a Karate debugging session for the given feature file, scheduling the work asynchronously.
     *
     * This will update the service's last-executed file and scenario, build the project, publish debugger messages on the IDE message bus, and launch an external Java process that runs the debug agent. If a debug session is already running a warning notification is shown and the call returns without starting a new session.
     *
     * @param fileName Absolute path to the feature file to execute.
     * @param scenarioName Optional scenario name to run within the feature; ignored when null or blank.
     * @param skipBreakpoints When true, instructs the agent to skip breakpoints for this run.
     */
    fun executeSuite(fileName: String, scenarioName: String?, skipBreakpoints: Boolean = false) {
        if(this.process != null) {
            notificationGroup
                .createNotification(
                    "Debugger already running",
                    "A debugging session is already in progress. Stop it before starting a new one.",NotificationType.WARNING)
                .addAction(object: AnAction("Stop And Run") {
                    override fun actionPerformed(p0: AnActionEvent) {
                        stop()
                        executeSuite(fileName, scenarioName)
                    }
                })
                .notify(project)
            return
        }
        this.lastExecutedFileName = fileName
        this.lastScenarioName = scenarioName
        buildMavenProject {
            ApplicationManager.getApplication().executeOnPooledThread {
                val featureClasspath = fileName.substring(projectBasePath.length + 1)
                val projectBase = requireNotNull(project.basePath) { "Project base path is not available" }

                val urls = getMavenDependenciesURL().joinToString(";")
                val breakpointPath = Files.createTempFile("breakpoints_", ".txt")
                Files.writeString(
                    breakpointPath,
                    BreakpointFileCodec.encode(getBreakpoints()),
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

                    override fun stepBack() {
                        remotePublisher.stepBack()
                    }

                    override fun hotReload() {
                        remotePublisher.hotReload()
                    }

                    override fun setShouldSkipBreakpoints(skipBreakpoints: Boolean) {
                        remotePublisher.setShouldSkipBreakpoints(skipBreakpoints)
                    }

                    override fun stepOut() {
                        remotePublisher.stepOut()
                    }
                })

                val debugServer = DebugServer.getInstance().start()

                val command = mutableListOf(getProjectJavaExecutable(project))
                command.addAll(getVmOptions(debugServer.port))
                command.add("-cp")
                command.add(getAgentLaunchClasspath())
                command.add("in.srikanthk.devlabs.kchopdebugger.agent.Main")
                command.add(featureClasspath)
                command.add(projectBase)
                command.add(breakpointPath.toString())
                command.add(urls)
                command.add(skipBreakpoints.toString())
                if (!scenarioName.isNullOrBlank()) {
                    command.add(scenarioName)
                }

                this.process =
                    ProcessBuilder(command)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .start()
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
                "Karate chop error",
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

        responsePublisher.updateState(DebuggerState.Started)
        runner.run(parameters, null, callback)
        return true
    }

    /**
     * Starts a Maven package build for the project and invokes the provided callback when the build finishes.
     *
     * @param callback Runnable to execute after the Maven build completes.
     */
    fun hotReload(callback: Runnable) {
        buildMavenProject(callback)
    }

    /**
     * Builds a list of JVM system property arguments from configured run properties and the debug port.
     *
     * @param debugPort The port number used by the debug agent (added as `-Ddebug.port=<port>`).
     * @return A list of VM option strings formatted as `-Dkey=value`, including the debug port entry.
     */
    private fun getVmOptions(debugPort: Int): List<String> {
        val vmOptions = mutableListOf<String>()
        runPropertiesService?.state?.state?.entries?.forEach { entry ->
            vmOptions.add("-D${entry.key}=${entry.value}")
        }
        vmOptions.add("-Ddebug.port=$debugPort")
        return vmOptions
    }

    /**
     * Collects file paths for the first Maven project's dependency artifacts and the project's test-classes directory.
     *
     * @return A list of absolute filesystem paths: one entry per Maven dependency artifact followed by the target test-classes path.
     */
    private fun getMavenDependenciesURL(): List<String> {
        val mavenProjectManager = MavenProjectsManager.getInstance(project)
        val dependencies = ArrayList(mavenProjectManager.projects[0].dependencies.map { dep -> dep.file.path })
        dependencies.add(File(getTestClassesPath()).path)

        return dependencies
    }

    private fun getTestClassesPath(): String {
        val testClassDir = File(File(project.basePath, "target"), "test-classes")

        return testClassDir.path.toString()
    }

    /**
     * Creates a DebugResponse subscriber that forwards debugger events to the project's response publisher.
     *
     * @return A DebugResponse that forwards variable updates, state changes, navigation requests, and evaluation results to the project's response publisher; `appendLog` is a no-op.
     */
    fun createRemoteCallSubscriber(): DebugResponse {
        return object : DebugResponse {
            override fun updateKarateVariable(vars: HashMap<String, KarateVariableSnapshot>) {
                responsePublisher.updateKarateVariables(vars)
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
                responsePublisher.evaluateExpressionResult(result ?: "", error ?: "")
            }
        }
    }

    /**
     * Locates the debug agent JAR file placed in the plugin's lib directory.
     *
     * @return The File pointing to the first JAR whose name starts with `debug-agent-`.
     * @throws IllegalArgumentException if the plugin agent JAR cannot be found in the plugin's lib directory.
     */
    fun getAgentJarFile(): File {
        val plugin = PluginManagerCore.getPlugin(PluginId.getId("in.srikanthk.devlabs.karate-chop-debugger"))
        val pluginPath = plugin?.pluginPath
        val libDir = File(pluginPath?.pathString, "lib")
        return requireNotNull(libDir.listFiles()?.firstOrNull { it.name.startsWith("debug-agent-") && it.extension == "jar" }) {
            "Debug agent jar not found in ${libDir.path}"
        }
    }

    /**
     * Builds a classpath string containing all JAR files in the agent's lib directory, sorted by file name.
     *
     * @return A platform path-separator separated string of absolute paths to the agent JAR files.
     */
    fun getAgentLaunchClasspath(): String {
        val agentLibDir = getAgentJarFile().parentFile
        return requireNotNull(agentLibDir.listFiles())
            .filter { it.isFile && it.extension == "jar" }
            .sortedBy { it.name }
            .joinToString(File.pathSeparator) { it.absolutePath }
    }
}
