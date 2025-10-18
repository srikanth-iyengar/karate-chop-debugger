package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic

class StepIntoAction: AnAction() {
    override fun actionPerformed(action: AnActionEvent) {
        val publisher =  action.project?.messageBus?.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
        publisher?.stepForward()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = false
    }
}