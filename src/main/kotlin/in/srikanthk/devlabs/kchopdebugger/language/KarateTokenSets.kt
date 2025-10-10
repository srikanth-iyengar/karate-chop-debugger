package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.psi.tree.TokenSet


object KarateTokenSets {
    val IDENTIFERS = TokenSet.create(
        KarateTypes.TAGS, KarateTypes.BACKGROUND, KarateTypes.EXAMPLES,
        KarateTypes.SCENARIO, KarateTypes.SCENARIO_OUTLINE, KarateTypes.STEP, KarateTypes.TABLE_ROW,
    )
    val COMMENTS = TokenSet.create(KarateTypes.DOC_STRING)
}