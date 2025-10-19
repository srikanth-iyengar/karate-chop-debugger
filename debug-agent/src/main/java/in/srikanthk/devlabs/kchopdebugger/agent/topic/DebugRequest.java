package in.srikanthk.devlabs.kchopdebugger.agent.topic;

import in.srikanthk.devlabs.kchopdebugger.agent.Topic;

public interface DebugRequest {
    Topic<DebugRequest> TOPIC = Topic.createTopic("DebugRequestTopic", DebugRequest.class);

    default void publishKarateVariables() { };
    default void stepInto() { };
    default void resume() { };
    default void evaluateExpression(String expression) { };
    default void addBreakpoint(String fileName, Integer lineNumber) { };
    default void removeBreakpoint(String fileName, Integer lineNumber) { };
    default void stepOver() { }
    default void stepBack() { }
    default void hotReload() { }
}
