package in.srikanthk.devlabs.kchopdebugger.agent;

import com.intuit.karate.Runner;
import in.srikanthk.devlabs.kchopdebugger.agent.communication.DebugClient;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final SessionState sessionState = SessionState.getInstance();
    private static final DebugResponse responsePublisher = DebugMessageBus.getInstance().publisher(DebugResponse.TOPIC);

    /**
     * Entrypoint for the debug agent that initializes session state, starts a debug client,
     * and executes the test suite (optionally filtered by scenario).
     *
     * <p>Expects command-line arguments describing feature path, project base path, breakpoint file,
     * classpath URLs, and whether to skip breakpoints. On successful completion exits with code 0.
     * If the argument count is insufficient exits with code 1. On unexpected failures exits with code 2
     * after publishing the error stack trace to the response publisher.</p>
     *
     * @param args command-line arguments in the following order:
     *             0: featureClassPath — classpath location of feature files;
     *             1: projectBasePath — project base directory used for reports and resolving paths;
     *             2: breakpointsFile — path to a serialized breakpoint file to populate initial breakpoints;
     *             3: classpathUrls — semicolon-separated list of JAR/file URLs added to the runtime classloader;
     *             4: skipBreakpoints — boolean flag ("true"/"false") to ignore breakpoints when executing;
     *             5: scenarioPattern (optional) — pattern to filter which scenario to execute.
     */
    public static void main(String[] args) {
        if (args.length < 5) {
            logger.error("Expected arguments: <featureClassPath> <projectBasePath> <breakpointsFile> <classpathUrls> <skipBreakpoints> [scenarioPattern]");
            System.exit(1);
        }
        final String featureClassPath = args[0];
        final String projectBasePath = args[1];
        final String breakpointsJson = args[2];
        final String classpathUrls = args[3];
        final boolean skipBreakpoints = Boolean.parseBoolean(args[4]);

        try {
            List<URL> jars = parseClasspathUrls(classpathUrls);
            logger.info("Loading classpath JARs: {}", jars);

            ClassLoader customLoader = createClassLoader(jars);
            initializeSessionState(featureClassPath, projectBasePath, breakpointsJson, skipBreakpoints);

            logger.info("Debug port: {}", System.getProperty("debug.port"));
            DebugClient client = new DebugClient("localhost", NumberUtils.toInt(System.getProperty("debug.port")));

            DebugMessageBus.getInstance().subscribe(DebugRequest.TOPIC, new DebugRequest() {
                @Override
                public void addBreakpoint(String fileName, Integer lineNumber) {
                    var breakpoints = SessionState.getInstance().getBreakpoints();
                    breakpoints.computeIfAbsent(fileName, k -> new TreeSet<>()).add(lineNumber);
                }

                @Override
                public void removeBreakpoint(String fileName, Integer lineNumber) {
                    var breakpoints = SessionState.getInstance().getBreakpoints();
                    breakpoints.computeIfAbsent(fileName, k -> new TreeSet<>()).remove(lineNumber);
                }
            });
            var th = new Thread(() -> {
                try {
                    String scenarioPatten = null;
                    if (args.length > 5) {
                        scenarioPatten = args[5];
                    }
                    executeSuite(customLoader, Optional.ofNullable(scenarioPatten));
                } catch (Exception e) {
                    responsePublisher.appendLog(getStackTraceAsString(e), false);
                }
            });
            th.setContextClassLoader(customLoader);
            th.start();
            th.join();

            client.stop();
            System.exit(0);
        } catch (Exception e) {
            responsePublisher.appendLog(getStackTraceAsString(e), false);
            logger.error("Execution failed", e);
            System.exit(2);
        }
    }

    private static List<URL> parseClasspathUrls(String cpUrls) {
        return Arrays.stream(cpUrls.split(";"))
                .map(File::new)
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid classpath URL: " + file, e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Initialize the global session state with feature path, project path, breakpoint data, and the skip-breakpoints flag.
     *
     * @param featurePath    classpath location (prefix-free) where feature files are located
     * @param basePath       project base directory used for report output and path resolution
     * @param breakpointPath filesystem path to a file containing serialized breakpoint data; decoded with BreakpointFileCodec
     * @param skipBreakpoints if `true`, session state will be marked to ignore breakpoints during execution
     * @throws IOException if the breakpoint file cannot be read
     */
    private static void initializeSessionState(String featurePath, String basePath, String breakpointPath, boolean skipBreakpoints) throws IOException {
        sessionState.setFeatureClassPath(featurePath);
        sessionState.setProjectPath(basePath);
        sessionState.setSkipBreakpoints(skipBreakpoints);

        String serializedBreakpoints = Files.readString(Path.of(breakpointPath), StandardCharsets.UTF_8);
        BreakpointFileCodec.decode(serializedBreakpoints).forEach((key, value) -> {
            if (key != null) {
                sessionState.getBreakpoints()
                        .computeIfAbsent(key, ignored -> new TreeSet<>())
                        .addAll(value);
            }
        });
    }

    private static ClassLoader createClassLoader(List<URL> jars) {
        return new URLClassLoader(jars.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
    }

    private static void executeSuite(ClassLoader loader, Optional<String> scenarioPatten) throws IOException {
        Runner.Builder<?> builder =
                Runner.builder()
                        .path("classpath:" + sessionState.getFeatureClassPath())
                        .hook(new DebugHook())
                        .backupReportDir(false)
                        .classLoader(loader)
                        .reportDir(new File(sessionState.getProjectPath(), "karate-report").getAbsolutePath());
        if (scenarioPatten.isPresent()) {
            builder = builder.scenarioName(scenarioPatten.get());
        }
        builder.parallel(1);
    }

    public static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
