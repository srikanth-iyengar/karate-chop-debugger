package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class KarateSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(
        project: Project?,
        fiel: VirtualFile?
    ): SyntaxHighlighter {
        return KarateSyntaxHighlighter()
    }
}