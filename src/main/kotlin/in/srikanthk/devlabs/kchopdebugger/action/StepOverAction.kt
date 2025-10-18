package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDocumentManager
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateLanguage
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic

class StepOverAction: AnAction() {
    override fun actionPerformed(action: AnActionEvent) {
        val publisher =  action.project?.messageBus?.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
        publisher?.stepOver()
    }

    override fun update(action: AnActionEvent) {
        try {
            val editor = action.getData(CommonDataKeys.EDITOR)
            val psiFile = PsiDocumentManager.getInstance(action.project!!).getPsiFile(editor!!.document)

            val language = psiFile?.language

            if (language != null) {
                action.presentation.isEnabledAndVisible = language === KarateLanguage.INSTANCE
            } else {
                action.presentation.isEnabledAndVisible = false
            }
        } catch (ignored: Exception) {

        }
    }
}