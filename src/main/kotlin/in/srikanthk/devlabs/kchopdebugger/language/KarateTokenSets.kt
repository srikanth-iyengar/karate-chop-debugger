package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.psi.tree.TokenSet


object KarateTokenSets {
    val COMMENTS = TokenSet.create(KarateTypes.COMMENT, KarateTypes.COMMENT_STMT, KarateTypes.DOC_STRING)
}