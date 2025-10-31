package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.FunctionUtil
import `in`.srikanthk.devlabs.kchopdebugger.action.DebugTestAction

class KarateRunLineMarkerContributor : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!shouldHighlight(element)) return null

        // Extract scenario name
        val scenarioName = element.nextSibling?.text
            ?.trim()
            ?.lineSequence()
            ?.firstOrNull()
            ?.let { "\"^${it}$\"" }
            ?: return null

        val actionManager = ActionManager.getInstance()
        val debugTest = actionManager.getAction("in.srikanthk.devlabs.kchopdebugger.action.DebugTestAction")
        val runTest = actionManager.getAction("in.srikanthk.devlabs.kchopdebugger.action.RunTestAction")
        if (debugTest == null || runTest == null) return null

        val navigationHandler = GutterIconNavigationHandler<PsiElement> { mouseEvent, _ ->
            val dataContext = SimpleDataContext.builder()
                .add(DebugTestAction.KARATE_SCENARIO_NAME, if (isScenario(element)) scenarioName else null)
                .add(CommonDataKeys.PROJECT, element.project)
                .add(CommonDataKeys.VIRTUAL_FILE, element.containingFile.virtualFile)
                .build()

            val group = DefaultActionGroup().apply {
                add(runTest)
                add(debugTest)
            }

            val popup = JBPopupFactory.getInstance().createActionGroupPopup(
                null,
                group,
                dataContext,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true
            )

            popup.show(RelativePoint(mouseEvent))
        }

        return LineMarkerInfo(
            element,
            element.textRange,
            if(isScenario(element)) AllIcons.Actions.Execute else AllIcons.Actions.RunAll,
            FunctionUtil.constant<Any, String>("Run Scenario"),
            navigationHandler,
            GutterIconRenderer.Alignment.RIGHT
        ) { if(isScenario(element)) "Run Scenario" else "Run Feature" }
    }

    private fun shouldHighlight(element: PsiElement): Boolean {
        return isFeature(element) || isScenario(element)
    }

    private fun isFeature(element: PsiElement): Boolean {
        return element.elementType == KarateTypes.FEATURE_KEYWORD
    }

    private fun isScenario(element: PsiElement): Boolean {
        return element.elementType == KarateTypes.SCENARIO_KEYWORD ||
                element.elementType == KarateTypes.SCENARIO_OUTLINE_KEYWORD
    }
}
