package `in`.srikanthk.devlabs.kchopdebugger.language.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import `in`.srikanthk.devlabs.kchopdebugger.language.KarateTypes
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.KarateDocString

class DocStringAnnotator : Annotator {
    val DOC_STRING_DELIMITER = TextAttributesKey.createTextAttributesKey(
        "KARATE_DOCSTRING_DELIMITER"
    )
    val DOC_STRING_CONTENT = TextAttributesKey.createTextAttributesKey(
        "KARATE_DOCSTRING_CONTENT"
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is KarateDocString) return

        val node = element.node

        // Highlight the triple quote delimiters
        node.getChildren(null).filter { it.elementType == KarateTypes.DOC_STRING_KEY }
            .forEach { quoteNode ->
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(quoteNode.textRange)
                    .textAttributes(DOC_STRING_DELIMITER)
                    .create()
            }

        // Highlight the content text inside the doc string
        node.getChildren(null).filter { it.elementType == KarateTypes.WORD }
            .forEach { textNode ->
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(textNode.textRange)
                    .textAttributes(DOC_STRING_CONTENT)
                    .create()
            }
    }
}
