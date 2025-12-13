package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiElement
import com.intellij.patterns.PlatformPatterns.psiElement

class KarateCompletionContributor : CompletionContributor() {
    init {
        // Generic completion for any Karate PSI element (fallback)
        extendCompletionFor(psiElement(PsiElement::class.java)) { result ->
            addBasicKeywords(result)
        }

        // Doc-string content: offer JSON-like completions and punctuation
        extendCompletionFor(psiElement().withElementType(KarateTypes.DOC_STRING_CONTENT)) { result ->
            result.addElement(LookupElementBuilder.create("true"))
            result.addElement(LookupElementBuilder.create("false"))
            result.addElement(LookupElementBuilder.create("null"))
            result.addElement(LookupElementBuilder.create("{").withTailText(" }", true))
            result.addElement(LookupElementBuilder.create("[").withTailText(" ]", true))
            result.addElement(LookupElementBuilder.create(":"))
            result.addElement(LookupElementBuilder.create(","))
            result.addElement(LookupElementBuilder.create("\"").withLookupString("\"key\"").withPresentableText("\"key\""))
        }

        // Identifier positions: suggest var_type keywords and common DSL keywords
        extendCompletionFor(psiElement().withElementType(KarateTypes.IDENTIFIER_KEYWORD)) { result ->
            result.addElement(LookupElementBuilder.create("def"))
            result.addElement(LookupElementBuilder.create("json"))
            result.addElement(LookupElementBuilder.create("xml"))
            result.addElement(LookupElementBuilder.create("xmlstring"))
            addBasicKeywords(result)
        }

        // WORD_KEY (DSL words / step keys) - offer step words and feature tokens
        extendCompletionFor(psiElement().withElementType(KarateTypes.WORD_KEY)) { result ->
            arrayOf("Given", "When", "Then", "And", "But", "*", "Feature:", "Scenario:", "Background:", "Examples:")
                .forEach { result.addElement(LookupElementBuilder.create(it)) }
        }
    }

    private fun extendCompletionFor(pattern: com.intellij.patterns.ElementPattern<out PsiElement>, providerAction: (CompletionResultSet) -> Unit) {
        extend(
            CompletionType.BASIC,
            pattern,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    providerAction(result)
                }
            }
        )
    }

    private fun addBasicKeywords(result: CompletionResultSet) {
        val keywords = arrayOf(
            "Feature:", "Background:", "Scenario:", "Scenario Outline:", "Examples:",
            "Given", "When", "Then", "And", "But", "*",
            "def", "json", "xml", "xmlstring", "call", "callonce", "read", "print", "match",
            "set", "table", "driver", "configure", "function", "return", "if", "else", "karate",
            "url", "path", "param", "params", "cookie", "cookies", "header", "headers",
            "form field", "form fields", "request", "multipart field", "multipart file", "multipart entity",
            "multipart fields", "multipart files", "status", "method", "retry until", "soap action",
            "compareImage", "listen", "doc"
        )
        for (k in keywords) result.addElement(LookupElementBuilder.create(k))

        // JSON/JS literals
        result.addElement(LookupElementBuilder.create("true"))
        result.addElement(LookupElementBuilder.create("false"))
        result.addElement(LookupElementBuilder.create("null"))
    }
}
