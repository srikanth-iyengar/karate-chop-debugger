package `in`.srikanthk.devlabs.kchopdebugger.language.annotator.injector;

import com.intellij.lang.Language
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import `in`.srikanthk.devlabs.kchopdebugger.language.psi.impl.KarateDocContentImpl


class KarateDocStringInjector : MultiHostInjector {

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, host: PsiElement) {
        if (host !is KarateDocContentImpl) return

        val text = host.text

        val content = text.trim { it <= ' ' }
        var language: Language? = null
        if (content.startsWith("{") && content.endsWith("}")) {
            language = com.intellij.json.JsonLanguage.INSTANCE
        } else if (content.startsWith("<") && content.endsWith(">")) {
            language = XMLLanguage.INSTANCE
        }

        if (language != null) {
            registrar.startInjecting(language)
            registrar.addPlace(null, null, host as PsiLanguageInjectionHost, TextRange(0, host.textLength))
            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(KarateDocContentImpl::class.java)
    }
}
