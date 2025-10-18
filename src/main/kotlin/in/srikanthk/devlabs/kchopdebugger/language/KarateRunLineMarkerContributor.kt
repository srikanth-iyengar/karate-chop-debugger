package `in`.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings
import `in`.srikanthk.devlabs.kchopdebugger.action.RunTestAction

class KarateRunLineMarkerContributor : RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        if (!shouldHighlight(element)) return null

        // The actions shown in the popup when the icon is clicked
        val customAction = RunTestAction()

        if (isScenario(element)) {
            val descriptions = element.nextSibling.text.trim().split(Regex("""(?:\r?\n)+"""))
            if(descriptions.isNotEmpty()) {
                customAction.scenarioName = "\"^${descriptions[0]}$\""
            }
        }
        return Info(
            AllIcons.Actions.Execute, // The green Run icon
            arrayOf(customAction),
        ) { if (isScenario(element)) "Run Scenario" else "Run Test" }
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
