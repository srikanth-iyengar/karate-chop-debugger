package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.lang.Language

class KarateLanguage: Language("karate") {

    companion object {
        val INSTANCE = KarateLanguage()
    }
}