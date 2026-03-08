package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.agent.KarateVariableSnapshot
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import java.awt.BorderLayout
import javax.swing.JPanel

class LogViewPanel(val project: Project) : JPanel(BorderLayout()) {
    private val consoleViewPanel = ConsoleViewImpl(project, true)
    private val messageBus = project.messageBus.connect()

    init {
        add(consoleViewPanel.component, BorderLayout.CENTER)
        messageBus.subscribe(DebuggerInfoResponseTopic.TOPIC, object : DebuggerInfoResponseTopic {
            override fun evaluateExpressionResult(result: String, error: String) {
            }

            override fun appendLog(log: String, isSuccess: Boolean) {
                WriteCommandAction.runWriteCommandAction(project) {
                    addLog(log, isSuccess)
                }
            }

            /**
             * Handles a navigation request to a source file and line number; currently does nothing.
             *
             * @param filepath The path to the target file.
             * @param lineNumber The target line number within the file.
             */
            override fun navigateTo(filepath: String, lineNumber: Int) {
            }

            /**
             * Handles an updated set of Karate variables from the debugger.
             *
             * @param vars Map from variable name to its `KarateVariableSnapshot` containing the variable's current value and metadata.
             */
            override fun updateKarateVariables(vars: HashMap<String, KarateVariableSnapshot>) {
            }

            /**
             * Reacts to debugger state changes and clears the console when a debugging session starts.
             *
             * @param state The new debugger state. If `state` equals `DebuggerState.Started`, the console view is cleared.
             */
            override fun updateState(state: DebuggerState) {
                if(state == DebuggerState.Started) {
                    consoleViewPanel.clear()
                }
            }
        })
    }

    fun addLog(message: String, isError: Boolean) {
        val contentType = if (isError) {
            com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT
        } else {
            com.intellij.execution.ui.ConsoleViewContentType.LOG_INFO_OUTPUT
        }


        consoleViewPanel.print(message + "\n", contentType)

    }
}
