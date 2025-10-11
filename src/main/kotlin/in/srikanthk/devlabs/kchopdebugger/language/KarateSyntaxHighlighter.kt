package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class KarateSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val FEATURE_KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_FEATURE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val SCENARIO_KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_SCENARIO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val SCENARIO_OUTLINE_KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_SCENARIO_OUTLINE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val BACKGROUND_KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_BACKGROUND_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val EXAMPLES_KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_EXAMPLES_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)

        val STEP =
            TextAttributesKey.createTextAttributesKey("KARATE_STEP", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("KARATE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val TAG =
            TextAttributesKey.createTextAttributesKey("KARATE_TAG", DefaultLanguageHighlighterColors.METADATA)
        val STRING =
            TextAttributesKey.createTextAttributesKey("KARATE_STRING", DefaultLanguageHighlighterColors.STRING)
        val TABLE_ROW =
            TextAttributesKey.createTextAttributesKey("KARATE_TABLE_ROW", DefaultLanguageHighlighterColors.CONSTANT)
        val OPERATOR =
            TextAttributesKey.createTextAttributesKey("KARATE_TEXT", DefaultLanguageHighlighterColors.INSTANCE_METHOD)
        val DESCRIPTION =
            TextAttributesKey.createTextAttributesKey("KARATE_FEATURE_DESCRIPTION", DefaultLanguageHighlighterColors.LINE_COMMENT)
    }

    override fun getHighlightingLexer() = KarateLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            KarateTypes.FEATURE_KEYWORD -> arrayOf(FEATURE_KEYWORD)
            KarateTypes.SCENARIO_KEYWORD -> arrayOf(SCENARIO_KEYWORD)
            KarateTypes.SCENARIO_OUTLINE_KEYWORD -> arrayOf(SCENARIO_OUTLINE_KEYWORD)
            KarateTypes.BACKGROUND_KEYWORD -> arrayOf(BACKGROUND_KEYWORD)
            KarateTypes.EXAMPLES_KEYWORD -> arrayOf(EXAMPLES_KEYWORD)
            KarateTypes.DESCRIPTION -> arrayOf(DESCRIPTION)

            KarateTypes.GIVEN_STEP,
            KarateTypes.WHEN_STEP,
            KarateTypes.THEN_STEP,
            KarateTypes.AND_STEP,
            KarateTypes.BUT_STEP -> arrayOf(FEATURE_KEYWORD)
            KarateTypes.STAR_STEP -> arrayOf(OPERATOR)

            KarateTypes.WORD -> arrayOf(STRING)
            KarateTypes.COMMENT_STMT -> arrayOf(COMMENT)
            KarateTypes.TAGS_KEY -> arrayOf(TAG)
            KarateTypes.DOC_STRING_KEY -> arrayOf(STRING)
            KarateTypes.TABLE_ROW -> arrayOf(TABLE_ROW)
            else -> emptyArray()
        }
    }
}
