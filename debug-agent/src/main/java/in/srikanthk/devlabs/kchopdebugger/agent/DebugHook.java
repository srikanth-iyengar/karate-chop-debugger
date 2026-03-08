package in.srikanthk.devlabs.kchopdebugger.agent;

import com.intuit.karate.KarateException;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.Suite;
import com.intuit.karate.core.*;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest;
import in.srikanthk.devlabs.kchopdebugger.agent.topic.DebugResponse;
import org.apache.commons.lang3.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    private boolean doingStepOut = false;
    private Optional<Scenario> stepOutScenario = Optional.empty();

    private static final String JAVA_BASE_PATH = "src/test/java";
    private static final String CLASSPATH_COLON = "classpath:";

    @Override
    public void beforeSuite(Suite suite) {
        responsePublisher.updateState(DebuggerState.Started);
    }

    /**
     * Decides whether execution should pause at the given step for debugger interaction and, when paused, publishes debugger state,
     * navigation and variable snapshots and waits for remote debug commands (step into/over/out, resume, evaluate, hot-reload, step-back).
     *
     * @param step the current Step being executed
     * @param sr the ScenarioRuntime for the current scenario (provides the script engine, actions, and scenario context)
     * @return `true` to continue normal step processing, `false` to stop processing further steps (for example after a step-back)
     */
    @Override
    public boolean beforeStep(Step step, ScenarioRuntime sr) {
        if (doingStepOver) {
            if (sr.scenario == stepOverScenario) {
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

        if ((shouldHalt || stopOnNextStep.get()) && !sessionState.isSkipBreakpoints()) {
            responsePublisher.navigateTo(filePath, startLine);
            responsePublisher.updateState(DebuggerState.Halted);
            publishKarateVariablesSerializable(sr.engine.vars);
            stopOnNextStep.set(false);
            CountDownLatch latch = new CountDownLatch(1);

            var listener = new DebugRequest() {
                @Override
                public void publishKarateVariables() {
                    publishKarateVariablesSerializable(sr.engine.vars);
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
                        if (expression.startsWith("*")) {
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
                        publishKarateVariablesSerializable(sr.engine.vars);
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
                    publishKarateVariablesSerializable(sr.engine.vars);
                }

                @Override
                public void setShouldSkipBreakpoints(boolean skipBreakpoints) {
                    SessionState.getInstance().setSkipBreakpoints(skipBreakpoints);
                }

                @Override
                public void stepOut() {
                    doingStepOut = true;
                    stepOutScenario = Optional.of(sr.scenario);
                    latch.countDown();
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

        if (stepBack.get()) {
            return false;
        }

        return RuntimeHook.super.beforeStep(step, sr);
    }

    /**
     * Create snapshots for the given Karate variables and publish them to the debug response channel.
     *
     * Builds a map of variable name to KarateVariableSnapshot (containing the variable's type and string value)
     * and sends it via the configured DebugResponse publisher.
     *
     * @param vars map of variable names to their corresponding `Variable` instances to snapshot and publish
     */
    public void publishKarateVariablesSerializable(Map<String, Variable> vars) {
        HashMap<String, KarateVariableSnapshot> snapshots = new HashMap<>();
        for (Map.Entry<String, Variable> entry : vars.entrySet()) {
            snapshots.put(
                    entry.getKey(),
                    new KarateVariableSnapshot(
                            String.valueOf(entry.getValue().type),
                            entry.getValue().getAsString()
                    )
            );
        }

        responsePublisher.updateKarateVariable(snapshots);
    }

    /**
     * Handles post-scenario debugger state updates for step-over and step-out operations.
     *
     * If the finished scenario matches the recorded step-over target, clears the step-over state
     * and ensures the debugger will halt at the next step. If it matches the recorded step-out target,
     * clears the step-out state and ensures the debugger will halt at the next step. Delegates to the
     * superclass hook after updating state.
     *
     * @param sr the runtime information for the scenario that just finished
     */
    @Override
    public void afterScenario(ScenarioRuntime sr) {
        if (doingStepOver) {
            if(sr.scenario == stepOverScenario) {
                doingStepOver = false;
                stepOverScenario = null;
                this.stopOnNextStep.set(true);
            }
        } else if (doingStepOut && stepOutScenario.isPresent() && sr.scenario == stepOutScenario.get()) {
            doingStepOut = false;
            stepOutScenario = Optional.empty();
            this.stopOnNextStep.set(true);
        }
        RuntimeHook.super.afterScenario(sr);
    }

    @Override
    public void afterSuite(Suite suite) {
        this.responsePublisher.updateState(DebuggerState.Finished);
    }

}
