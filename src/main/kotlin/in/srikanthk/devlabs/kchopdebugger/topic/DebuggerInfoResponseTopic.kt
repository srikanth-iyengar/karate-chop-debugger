package `in`.srikanthk.devlabs.kchopdebugger.topic

import com.intellij.util.messages.Topic
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.agent.KarateVariableSnapshot
import java.util.*

interface DebuggerInfoResponseTopic {
    companion object {
        val TOPIC = Topic.create("Karate Chop Debugger Response Topic", DebuggerInfoResponseTopic::class.java)
    }

    /**
 * Updates the current set of Karate variable snapshots.
 *
 * @param vars A map from variable name to its corresponding `KarateVariableSnapshot` representing the variable's current state.
 */
fun updateKarateVariables(vars: HashMap<String, KarateVariableSnapshot>) {}
    /**
 * Updates the current debugger state.
 *
 * @param state The new debugger state to apply.
 */
fun updateState(state: DebuggerState) {}
    /**
 * Navigate the editor to the specified file and line.
 *
 * @param filepath Path of the file to open.
 * @param lineNumber Line number to position the caret at.
 */
fun navigateTo(filepath: String, lineNumber: Int) {}
    /**
 * Appends a log entry to the debugger output.
 *
 * @param log The message to append.
 * @param isSuccess `true` if the entry represents a successful operation, `false` if it represents an error or failure.
 */
fun appendLog(log: String, isSuccess: Boolean) {}
    fun evaluateExpressionResult(result: String, error: String){}
}
