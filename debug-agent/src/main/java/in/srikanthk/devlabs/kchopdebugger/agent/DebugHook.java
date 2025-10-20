package in.srikanthk.devlabs.kchopdebugger.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.karate.KarateException;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.Suite;
import com.intuit.karate.core.*;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;
import org.apache.commons.lang3.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class DebugHook implements RuntimeHook {
    private final Map<String, TreeSet<Integer>> breakpoints = SessionState.getInstance().getBreakpoints();
    private final AtomicBoolean stopOnNextStep = new AtomicBoolean(false);
    private final DebugResponse responsePublisher = DebugMessageBus.getInstance().publisher(DebugResponse.TOPIC);
    private final DebugMessageBus messageBus = DebugMessageBus.getInstance();
    private final SessionState sessionState = SessionState.getInstance();
    private Scenario stepOverScenario;
    private boolean doingStepOver = false;

    private static final String JAVA_BASE_PATH = "src/test/java";
    private static final String CLASSPATH_COLON = "classpath:";

    @Override
    public void beforeSuite(Suite suite) {
        responsePublisher.updateState(DebuggerState.Started);
    }

    @Override
    public boolean beforeStep(Step step, ScenarioRuntime sr) {
        if(doingStepOver) {
            if(sr.scenario == stepOverScenario) {
                stopOnNextStep.set(true);
                stepOverScenario = null;
                doingStepOver = false;
            } else {
                return true;
            }
        }
        var startLine = step.getLine();
        var endLine = step.getLine();
        var filePath = String.format(
                "%s/%s/%s",
                sessionState.getProjectPath(),
                JAVA_BASE_PATH,
                Strings.CS.removeStart(step.getFeature().getResource().getPrefixedPath(), CLASSPATH_COLON)
        );
        var lineSet = breakpoints.computeIfAbsent(filePath, (k) -> new TreeSet<>());

        var shouldHalt =
                lineSet.contains(startLine) || (lineSet.ceiling(startLine) != null && lineSet.ceiling(startLine) <= endLine && lineSet.floor(
                        endLine
                ) != null && lineSet.floor(endLine) >= startLine);
        AtomicBoolean stepBack = new AtomicBoolean(false);

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
                public void stepInto() {
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
                        if(expression.startsWith("*")) {
                            var tempStep = new Step(sr.scenario, -1);
                            try {
                                tempStep.parseAndUpdateFrom(expression);
                            } catch (Exception e) {
                                responsePublisher.evaluationResult("", e.getMessage());
                            }
                            var response = StepRuntime.execute(tempStep, sr.actions);
                            var stepResult = new StepResult(tempStep, response);
                            responsePublisher.evaluationResult(stepResult.getResult().getStatus(), "");
                        } else {
                            var response = sr.engine.evalJs(expression);
                            responsePublisher.evaluationResult(response.getAsString(), "");
                        }
                    } catch (KarateException exception) {
                        responsePublisher.evaluationResult("", exception.getMessage());
                    } finally {
                        publishKarateVariablesSerializabel(sr.engine.vars);
                        sr.engine.setFailedReason(null);
                    }
                }

                @Override
                public void stepOver() {
                    stepOverScenario = sr.scenario;
                    doingStepOver = true;
                    latch.countDown();
                }

                @Override
                public void stepBack() {
                    sr.stepBack();
                    stepBack.set(true);
                    stopOnNextStep.set(true);
                    latch.countDown();
                }

                @Override
                public void hotReload() {
                    sr.hotReload();
                    publishKarateVariablesSerializabel(sr.engine.vars);
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

        if(stepBack.get()) {
            return false;
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

    @Override
    public void afterScenario(ScenarioRuntime sr) {
        if(doingStepOver) {
            doingStepOver = false;
            this.stopOnNextStep.set(true);
        }
        RuntimeHook.super.afterScenario(sr);
    }

    @Override
    public void afterSuite(Suite suite) {
        this.responsePublisher.updateState(DebuggerState.Finished);
    }

}
