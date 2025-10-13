package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDocumentManager
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateLanguage
import `in`.srikanthk.devlabs.kchopdebugger.service.KarateExecutionService
import java.util.concurrent.CompletableFuture

open class RunTestAction : AnAction() {
    override fun actionPerformed(action: AnActionEvent) {
        try {
            val project = action.project ?: return

            val file = action.getData(CommonDataKeys.VIRTUAL_FILE);

            if (file?.extension != "feature") {
                return
            }

            val executionService = project.getService(KarateExecutionService::class.java);
            CompletableFuture.supplyAsync { executionService.executeSuite(file.path) }
        } catch (e: Exception) {

        }
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
        } catch (e: Exception) {

        }
    }
}