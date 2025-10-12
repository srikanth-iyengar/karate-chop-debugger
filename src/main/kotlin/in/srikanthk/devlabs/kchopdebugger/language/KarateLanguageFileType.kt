package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import `in`.srikanthk.devlabs.kchopdebugger.ui.KChopIcons
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class KarateLanguageFileType : LanguageFileType(KarateLanguage.INSTANCE) {
    companion object {
        val INSTANCE = KarateLanguageFileType()
    }

    override fun getName(): @NonNls String {
        return "Karate DSL"
    }

    override fun getDescription(): @NlsContexts.Label String {
        return "Karate file"
    }

    override fun getDefaultExtension(): @NlsSafe String {
        return "feature"
    }

    override fun getIcon(): Icon? {
        return KChopIcons.Cucumber;
    }
}