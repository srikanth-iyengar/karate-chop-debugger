package `in`.srikanthk.devlabs.kchopdebugger.language.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateTypes
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.KarateStep

class KarateStepAnnotator : Annotator {

    private val karateTypes = setOf(
        "def",
        "string",
        "json",
        "int",
        "boolean",
        "true",
        "false",
        "method",
        "status",
        "driver",
        "url",
        "path",
        "request"
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is KarateStep) return

        val children = element.node.getChildren(null)
        if (children.isEmpty()) return

        val firstChild = children[0]
        val firstPsi = firstChild.psi
        when (firstChild.elementType) {
            KarateTypes.STAR_STEP -> highlight(
                holder,
                firstPsi.textRange,
                DefaultLanguageHighlighterColors.INSTANCE_METHOD
            )

            KarateTypes.GIVEN_STEP,
            KarateTypes.WHEN_STEP,
            KarateTypes.THEN_STEP,
            KarateTypes.AND_STEP,
            KarateTypes.BUT_STEP -> highlight(holder, firstPsi.textRange, DefaultLanguageHighlighterColors.KEYWORD)
        }

        // Highlight second child (step text)
        if (children.size > 1) {
            val list = children[1].getChildren(TokenSet.create(KarateTypes.WORD))
            if(!list.isEmpty()) {
                list.forEachIndexed { index, child ->
                    val secondPsi = child.psi
                    val text = child.text

                    if (child.elementType.toString() == "WORD" && index == 0) {
                        if (text.trim(' ', '\t') in karateTypes) {
                            highlight(holder, secondPsi.textRange, DefaultLanguageHighlighterColors.INSTANCE_FIELD)
                        }
                    }
                    if(text.toDoubleOrNull() != null) {
                        highlight(holder, secondPsi.textRange, DefaultLanguageHighlighterColors.NUMBER)
                    } else {
                        highlight(holder, secondPsi.textRange, DefaultLanguageHighlighterColors.STRING)
                    }
                    return
                }
            }
        }
    }

    private fun highlight(
        holder: AnnotationHolder,
        range: TextRange,
        attributes: com.intellij.openapi.editor.colors.TextAttributesKey
    ) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(range)
            .textAttributes(attributes)
            .create()
    }
}
