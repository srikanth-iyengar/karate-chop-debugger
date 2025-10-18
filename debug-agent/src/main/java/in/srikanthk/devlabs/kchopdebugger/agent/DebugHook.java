package in.srikanthk.devlabs.kchopdebugger.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.karate.KarateException;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.Suite;
import com.intuit.karate.core.*;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.StringUtils.removeStart;

public class DebugHook implements RuntimeHook {
    private final Map<String, TreeSet<Integer>> breakpoints = SessionState.getInstance().getBreakpoints();
    private final AtomicBoolean stopOnNextStep = new AtomicBoolean(false);
    private final DebugResponse responsePublisher = DebugMessageBus.getInstance().publisher(DebugResponse.TOPIC);
    private final DebugMessageBus messageBus = DebugMessageBus.getInstance();
    private final SessionState sessionState = SessionState.getInstance();

    private static final String JAVA_BASE_PATH = "src/test/java";
    private static final String CLASSPATH_COLON = "classpath:";

    @Override
    public void beforeSuite(Suite suite) {
        responsePublisher.updateState(DebuggerState.Started);
    }

    @Override
    public boolean beforeStep(Step step, ScenarioRuntime sr) {
        var startLine = step.getLine();
        var endLine = step.getLine();
        var filePath = String.format(
                "%s/%s/%s",
                sessionState.getProjectPath(),
                JAVA_BASE_PATH,
                removeStart(step.getFeature().getResource().getPrefixedPath(), CLASSPATH_COLON)
        );
        var lineSet = breakpoints.computeIfAbsent(filePath, (k) -> new TreeSet<>());

        var shouldHalt =
                lineSet.contains(startLine) || (lineSet.ceiling(startLine) != null && lineSet.ceiling(startLine) <= endLine && lineSet.floor(
                        endLine
                ) != null && lineSet.floor(endLine) >= startLine);

        if (shouldHalt || stopOnNextStep.get()) {
            responsePublisher.navigateTo(filePath, startLine);
            responsePublisher.updateState(DebuggerState.Halted);
            publishKarateVariablesSerializabel(sr.engine.vars);
            stopOnNextStep.set(false);
            CountDownLatch latch = new CountDownLatch(1);

            var listener = new DebugRequest() {
                @Override
                public void publishKarateVariables() {
                    publishKarateVariablesSerializabel(sr.engine.vars);
                }

                @Override
                public void stepOver() {
                    stopOnNextStep.set(true);
                    latch.countDown();
                }

                @Override
                public void resume() {
                    latch.countDown();
                }

                @Override
                public void evaluateExpression(String expression) {
                    try {
                        var response = sr.engine.evalJs(expression);
                        responsePublisher.evaluationResult(response.getAsString(), "");
                    } catch (KarateException exception) {
                        responsePublisher.evaluationResult("", exception.getMessage());
                    } finally {
                        publishKarateVariablesSerializabel(sr.engine.vars);
                        sr.engine.setFailedReason(null);
                    }
                }

                @Override
                public void addBreakpoint(String fileName, Integer lineNumber) {
                }

                @Override
                public void removeBreakpoint(String fileName, Integer lineNumber) {
                }
            };
            messageBus.subscribe(DebugRequest.TOPIC, listener);

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            responsePublisher.updateState(DebuggerState.Running);
            messageBus.unsubscribe(DebugRequest.TOPIC, listener);
        }

        return RuntimeHook.super.beforeStep(step, sr);
    }

    public void publishKarateVariablesSerializabel(Map<String, Variable> vars) {
        HashMap<String, String> mp = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Map.Entry<String, Variable> entry : vars.entrySet()) {
            Map<String, Object> varMap = new HashMap<>();
            varMap.put("type", entry.getValue().type);
            varMap.put("value", entry.getValue().getAsString());
            try {
                mp.put(entry.getKey(), mapper.writeValueAsString(varMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        responsePublisher.updateKarateVariable(mp);
    }
}
