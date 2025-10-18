package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic

class StepOverAction: AnAction() {
    override fun actionPerformed(action: AnActionEvent) {
        val publisher =  action.project?.messageBus?.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
        publisher?.stepOver()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = false
    }
}