package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDocumentManager
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateLanguage
import `in`.srikanthk.devlabs.kchopdebugger.topic.UIActionTopic

open class QuickFillEvalExpression: AnAction() {
    override fun actionPerformed(action: AnActionEvent) {
        val editor = action.getData(CommonDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.getSelectedText(true)
        val project = action.project
        val publisher = project?.messageBus?.syncPublisher(UIActionTopic.TOPIC)

        if (selectedText != null) {
            publisher?.updateExprText(selectedText)
        }
    }

    override fun update(action: AnActionEvent) {
        try {
            val editor = action.getData(CommonDataKeys.EDITOR)
            val psiFile = PsiDocumentManager.getInstance(action.project!!).getPsiFile(editor!!.document)

            val language = psiFile?.language

            action.presentation.isEnabledAndVisible = language === KarateLanguage.INSTANCE
        } catch (e: Exception) {

        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }
}