package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class KarateEnterHandler : EnterHandlerDelegateAdapter() {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        return EnterHandlerDelegate.Result.Continue
    }

    override fun postProcessEnter(
        file: PsiFile,
        editor: Editor,
        dataContext: DataContext
    ): EnterHandlerDelegate.Result {
        if (!isKarateFile(file)) {
            return EnterHandlerDelegate.Result.Continue
        }

        val document = editor.document
        val caretOffset = editor.caretModel.offset
        val currentLine = document.getLineNumber(caretOffset)
        if (currentLine == 0) {
            return EnterHandlerDelegate.Result.Continue
        }

        val currentLineText = document.lineText(currentLine)
        if (currentLineText.any { it != ' ' && it != '\t' }) {
            return EnterHandlerDelegate.Result.Continue
        }

        val previousLineText = document.lineText(currentLine - 1)
        if (!HEADER_PATTERN.matches(previousLineText.trimEnd())) {
            return EnterHandlerDelegate.Result.Continue
        }

        val targetIndent = previousLineText.leadingIndent() + indentUnit()
        if (currentLineText == targetIndent) {
            return EnterHandlerDelegate.Result.Continue
        }

        val currentLineStart = document.getLineStartOffset(currentLine)
        val currentLineEnd = document.getLineEndOffset(currentLine)
        document.replaceString(currentLineStart, currentLineEnd, targetIndent)
        editor.caretModel.moveToOffset(currentLineStart + targetIndent.length)
        return EnterHandlerDelegate.Result.Stop
    }

    private fun isKarateFile(file: PsiFile): Boolean {
        val fileType = file.fileType
        return (fileType is LanguageFileType && fileType.language.isKindOf(KarateLanguage.INSTANCE)) ||
            file.language.isKindOf(KarateLanguage.INSTANCE) ||
            file.name.endsWith(".feature")
    }

    private fun indentUnit(): String {
        return DEFAULT_INDENT
    }

    private fun com.intellij.openapi.editor.Document.lineText(line: Int): String {
        val startOffset = getLineStartOffset(line)
        val endOffset = getLineEndOffset(line)
        return getText(TextRange(startOffset, endOffset))
    }

    private fun String.leadingIndent(): String = takeWhile { it == ' ' || it == '\t' }

    companion object {
        private val HEADER_PATTERN = Regex(
            """^\s*(Feature|Background|Scenario Outline|Scenario|Examples):.*$"""
        )

        private const val DEFAULT_INDENT = "  "
    }
}
