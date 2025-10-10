package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class KarateParserDefinition: ParserDefinition {
    val FILE: IFileElementType = IFileElementType(KarateLanguage.INSTANCE)

    override fun createLexer(project: Project?): Lexer {
        return KarateLexerAdapter()
    }

    override fun createParser(p0: Project?): PsiParser {
        return KarateParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun getCommentTokens(): TokenSet {
        return KarateTokenSets.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return KarateTypes.Factory.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return KarateFile(viewProvider)
    }
}