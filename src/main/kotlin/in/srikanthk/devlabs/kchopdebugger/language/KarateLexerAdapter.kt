package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.lexer.FlexAdapter

class KarateLexerAdapter: FlexAdapter(KarateLexer(null)) {
}