package `in`.srikanthk.devlabs.kchopdebugger.language.annotator.injector;

import com.intellij.lang.Language
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.KarateDocString
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.impl.KarateDocStringImpl


class KarateDocStringInjector : MultiHostInjector {

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, host: PsiElement) {
        if (host !is KarateDocStringImpl) return

        val text = host.text

        // Find the content inside the triple quotes
        val start = text.indexOf("\"\"\"") + 3 // skip opening """
        val end = text.lastIndexOf("\"\"\"") // before closing """

        if (start >= end) return  // empty content


        val endDiff = text.length - end
        val rangeInsideHost = TextRange(host.textRange.startOffset + start, host.textRange.endOffset - endDiff)

        val content = text.substring(start, end).trim { it <= ' ' }
        var language: Language? = null
        if (content.startsWith("{") && content.endsWith("}")) {
            language = com.intellij.json.JsonLanguage.INSTANCE
        } else if (content.startsWith("<") && content.endsWith(">")) {
            language = XMLLanguage.INSTANCE
        }

        if (language != null) {
            registrar.startInjecting(language)
            registrar.addPlace(null, null, host as PsiLanguageInjectionHost, rangeInsideHost)
            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(KarateDocString::class.java)
    }
}
