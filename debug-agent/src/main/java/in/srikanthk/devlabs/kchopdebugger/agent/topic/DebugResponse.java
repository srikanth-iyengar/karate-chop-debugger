package in.srikanthk.devlabs.kchopdebugger.agent.topic;

import in.srikanthk.devlabs.kchopdebugger.agent.DebuggerState;
import in.srikanthk.devlabs.kchopdebugger.agent.KarateVariableSnapshot;
import in.srikanthk.devlabs.kchopdebugger.agent.Topic;

import java.util.HashMap;

public interface DebugResponse {
    Topic<DebugResponse> TOPIC = Topic.createTopic("DebugResponseTopic", DebugResponse.class);

    /**
 * Update the debugger's collection of Karate variable snapshots.
 *
 * @param vars a map from Karate variable name to its current {@link KarateVariableSnapshot}
 */
void updateKarateVariable(HashMap<String, KarateVariableSnapshot> vars);

    /**
 * Update the current debugger state to the provided value.
 *
 * @param state the new debugger state to set
 */
void updateState(DebuggerState state);
    /**
 * Navigate to the specified source file at a given line number.
 *
 * @param filePath   the path of the target source file
 * @param lineNumber the 1-based line number within the file; may be null to open the file without a specific line
 */
void navigateTo(String filePath, Integer lineNumber);
    void appendLog(String log, Boolean isSuccess);
    void evaluationResult(String result, String error);
}
