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
        val STRING_LITERAL =
            TextAttributesKey.createTextAttributesKey(KarateTypes.STRING_LITERAL.toString(), DefaultLanguageHighlighterColors.STRING)
        val NUMBER_LITERAL =
            TextAttributesKey.createTextAttributesKey(KarateTypes.NUMBER_LITERAL.toString(), DefaultLanguageHighlighterColors.NUMBER)
        val BOOLEAN_LITERAL =
            TextAttributesKey.createTextAttributesKey(KarateTypes.BOOLEAN_LITERAL.toString(), DefaultLanguageHighlighterColors.NUMBER)

        // New / improved keys for better JSON and general highlighting
        val IDENTIFIER =
            TextAttributesKey.createTextAttributesKey(KarateTypes.IDENTIFIER.toString(), DefaultLanguageHighlighterColors.IDENTIFIER)
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("KARATE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val JSON_BRACES =
            TextAttributesKey.createTextAttributesKey("KARATE_JSON_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val JSON_BRACKETS =
            TextAttributesKey.createTextAttributesKey("KARATE_JSON_BRACKETS", DefaultLanguageHighlighterColors.BRACES)
        val JSON_PUNCTUATION =
            TextAttributesKey.createTextAttributesKey("KARATE_JSON_PUNCTUATION", DefaultLanguageHighlighterColors.COMMA)
        val DOCSTRING =
            TextAttributesKey.createTextAttributesKey("KARATE_DOCSTRING", DefaultLanguageHighlighterColors.DOC_COMMENT)
        val DOCSTRING_CONTENT =
            TextAttributesKey.createTextAttributesKey(KarateTypes.DOC_STRING_CONTENT.toString(), DefaultLanguageHighlighterColors.DOC_COMMENT)
        val OP_SIGN =
            TextAttributesKey.createTextAttributesKey("KARATE_OPERATOR_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PAREN =
            TextAttributesKey.createTextAttributesKey("KARATE_PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
    }

    override fun getHighlightingLexer() = KarateLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            // element / keyword tokens
            KarateTypes.FEATURE_KEYWORD -> arrayOf(FEATURE_KEYWORD)
            KarateTypes.SCENARIO_KEYWORD -> arrayOf(SCENARIO_KEYWORD)
            KarateTypes.SCENARIO_OUTLINE_KEYWORD -> arrayOf(SCENARIO_OUTLINE_KEYWORD)
            KarateTypes.BACKGROUND_KEYWORD -> arrayOf(BACKGROUND_KEYWORD)
            KarateTypes.EXAMPLES_KEYWORD -> arrayOf(EXAMPLES_KEYWORD)
            KarateTypes.DESCRIPTION -> arrayOf(DESCRIPTION)
            KarateTypes.KEYWORD -> arrayOf(KEYWORD)

            // explicit keyword tokens - map common DSL keywords to same KEYWORD style
            KarateTypes.CONFIGURE_KEYWORD,
            KarateTypes.DEF_KEYWORD,
            KarateTypes.CALL_KEYWORD,
            KarateTypes.CALLONCE_KEYWORD,
            KarateTypes.MATCH_KEYWORD,
            KarateTypes.PRINT_KEYWORD,
            KarateTypes.READ_KEYWORD,
            KarateTypes.RETURN_KEYWORD,
            KarateTypes.DRIVER_KEYWORD,
            KarateTypes.FUNCTION_KEYWORD,
            KarateTypes.IF_KEYWORD,
            KarateTypes.SET_KEYWORD,
            KarateTypes.TABLE_KEYWORD,
            KarateTypes.JSON_KEYWORD,
            KarateTypes.XML_KEYWORD,
            KarateTypes.XMLSTRING_KEYWORD -> arrayOf(KEYWORD)

            // Given/When/Then/And/But steps - keep as feature-keyword color
            KarateTypes.GIVEN_STEP,
            KarateTypes.WHEN_STEP,
            KarateTypes.THEN_STEP,
            KarateTypes.AND_STEP,
            KarateTypes.BUT_STEP -> arrayOf(FEATURE_KEYWORD)

            KarateTypes.STAR_STEP -> arrayOf(OPERATOR)

            // Literals
            KarateTypes.STRING_LITERAL -> arrayOf(STRING_LITERAL)
            KarateTypes.BOOLEAN_LITERAL -> arrayOf(BOOLEAN_LITERAL)
            KarateTypes.NUMBER_LITERAL -> arrayOf(NUMBER_LITERAL)
            KarateTypes.NULL_LITERAL -> arrayOf(BOOLEAN_LITERAL)

            // JSON and punctuation
            KarateTypes.L_CURLY, KarateTypes.R_CURLY -> arrayOf(JSON_BRACES)
            KarateTypes.JSON_OBJECT -> arrayOf(JSON_BRACES)
            KarateTypes.L_SQUARE, KarateTypes.R_SQUARE -> arrayOf(JSON_BRACKETS)
            KarateTypes.JSON_ARRAY -> arrayOf(JSON_BRACKETS)
            KarateTypes.COLON_OPERATOR, KarateTypes.COMMA -> arrayOf(JSON_PUNCTUATION)

            // Parentheses
            KarateTypes.L_PAREN, KarateTypes.R_PAREN -> arrayOf(PAREN)

            // Doc strings / content
            KarateTypes.DOC_STRING, KarateTypes.DOC_STRING_START, KarateTypes.DOC_STRING_END -> arrayOf(DOCSTRING)
            KarateTypes.DOC_STRING_CONTENT, KarateTypes.DOC_CONTENT -> arrayOf(DOCSTRING_CONTENT)

            // Identifiers and identifier-like tokens
            // Note: WORD_KEY is used for DSL words (step keys etc.) - keep those as KEYWORD to avoid colliding with variable IDENTIFIER
            KarateTypes.WORD_KEY -> arrayOf(KEYWORD)
            KarateTypes.IDENTIFIER, KarateTypes.IDENTIFIER_KEYWORD, KarateTypes.WORD -> arrayOf(IDENTIFIER)

            // Operators and punctuation
            KarateTypes.OPERATOR,
            KarateTypes.ASSIGNMENT_OPERATOR,
            KarateTypes.EQUALS_OPERATOR,
            KarateTypes.DOT_OPERATOR,
            KarateTypes.ARROW_OPERATOR,
            KarateTypes.TERNARY_OPERATOR,
            KarateTypes.UNARY_OPERATOR,
            KarateTypes.PIPE_OPERATOR,
            KarateTypes.PLUS_OPERATOR,
            KarateTypes.MINUS_OPERATOR,
            KarateTypes.MULTIPLY_OPERATOR,
            KarateTypes.DIVIDE_OPERATOR,
            KarateTypes.MODULO_OPERATOR,
            KarateTypes.BANG_OPERATOR,
            KarateTypes.AMPERSAND_OPERATOR,
            KarateTypes.LOGICAL_AND_OPERATOR,
            KarateTypes.LOGICAL_OR_OPERATOR,
            KarateTypes.LOGICAL_XOR_OPERATOR,
            KarateTypes.TILDE_OPERATOR,
            KarateTypes.GREATER_THAN_OPERATOR,
            KarateTypes.LESS_THAN_OPERATOR,
            KarateTypes.GREATER_THAN_EQUAL_OPERATOR,
            KarateTypes.LESS_THAN_EQUAL_OPERATOR,
            KarateTypes.NOT_EQUALS_OPERATOR -> arrayOf(OP_SIGN)

            // Comments, tags, tables
            KarateTypes.COMMENT_STMT, KarateTypes.COMMENT -> arrayOf(COMMENT)
            KarateTypes.TAGS_KEY -> arrayOf(TAG)
            KarateTypes.TABLE_ROW -> arrayOf(TABLE_ROW)

            // Fallback
            else -> emptyArray()
        }
    }
}
