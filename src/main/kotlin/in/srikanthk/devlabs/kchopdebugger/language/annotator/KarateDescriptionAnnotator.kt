package `in`.srikanthk.devlabs.kchopdebugger.language.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.KarateDescription

class KarateDescriptionAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is KarateDescription) return
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
            .textAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT).create()
    }
}