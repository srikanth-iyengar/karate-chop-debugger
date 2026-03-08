package `in`.srikanthk.devlabs.kchopdebugger.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.psi.PsiDocumentManager
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateLanguage
import `in`.srikanthk.devlabs.kchopdebugger.service.KarateExecutionService
import java.util.concurrent.CompletableFuture

open class DebugTestAction : AnAction() {
    companion object {
        val KARATE_SCENARIO_NAME: DataKey<String> =
            DataKey.create("KARATE_SCENARIO_NAME")
    }

    override fun actionPerformed(action: AnActionEvent) {
        val project = action.project ?: return
        val scenarioName = action.getData(KARATE_SCENARIO_NAME)

        val file = action.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file?.extension != "feature") {
            return
        }

        val executionService = project.getService(KarateExecutionService::class.java);
        CompletableFuture.supplyAsync { executionService.executeSuite(file.path, scenarioName) }
    }

    /**
     * Enable and show the action only when the current editor's PSI file language is Karate.
     *
     * Sets the action presentation's enabledAndVisible flag to `true` when the open editor's PSI file
     * language equals `KarateLanguage.INSTANCE`; otherwise sets it to `false`. If no editor or PSI file
     * is available or an exception occurs, the action is disabled and hidden.
     */
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
