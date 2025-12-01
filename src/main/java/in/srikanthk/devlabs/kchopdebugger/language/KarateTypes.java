// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import in.srikanthk.devlabs.kchopdebugger.language.psi.impl.*;

public interface KarateTypes {

  IElementType ASSIGNMENT = new KarateElementType("ASSIGNMENT");
  IElementType BACKGROUND = new KarateElementType("BACKGROUND");
  IElementType BASE = new KarateElementType("BASE");
  IElementType COMMENT = new KarateElementType("COMMENT");
  IElementType DESCRIPTION = new KarateElementType("DESCRIPTION");
  IElementType DOC_CONTENT = new KarateElementType("DOC_CONTENT");
  IElementType DOC_STRING = new KarateElementType("DOC_STRING");
  IElementType EXAMPLES = new KarateElementType("EXAMPLES");
  IElementType EXPRESSION = new KarateElementType("EXPRESSION");
  IElementType FUNCTION_CALL = new KarateElementType("FUNCTION_CALL");
  IElementType IDENTIFIER = new KarateElementType("IDENTIFIER");
  IElementType JSON_ARRAY = new KarateElementType("JSON_ARRAY");
  IElementType JSON_OBJECT = new KarateElementType("JSON_OBJECT");
  IElementType KEYWORD = new KarateElementType("KEYWORD");
  IElementType LINE = new KarateElementType("LINE");
  IElementType LITERALS = new KarateElementType("LITERALS");
  IElementType OPERATOR = new KarateElementType("OPERATOR");
  IElementType POSTFIX = new KarateElementType("POSTFIX");
  IElementType PRIMARY = new KarateElementType("PRIMARY");
  IElementType SCENARIO = new KarateElementType("SCENARIO");
  IElementType SCENARIO_OUTLINE = new KarateElementType("SCENARIO_OUTLINE");
  IElementType STEP = new KarateElementType("STEP");
  IElementType TABLE = new KarateElementType("TABLE");
  IElementType TAGS = new KarateElementType("TAGS");
  IElementType UNARY_OPERATOR = new KarateElementType("UNARY_OPERATOR");
  IElementType VAR_TYPE = new KarateElementType("VAR_TYPE");
  IElementType WORD = new KarateElementType("WORD");

  IElementType AMPERSAND_OPERATOR = new KarateTokenType("AMPERSAND_OPERATOR");
  IElementType AND_STEP = new KarateTokenType("AND_STEP");
  IElementType ARROW_OPERATOR = new KarateTokenType("ARROW_OPERATOR");
  IElementType ASSIGNMENT_OPERATOR = new KarateTokenType("ASSIGNMENT_OPERATOR");
  IElementType BACKGROUND_KEYWORD = new KarateTokenType("BACKGROUND_KEYWORD");
  IElementType BANG_OPERATOR = new KarateTokenType("BANG_OPERATOR");
  IElementType BOOLEAN_LITERAL = new KarateTokenType("BOOLEAN_LITERAL");
  IElementType BUT_STEP = new KarateTokenType("BUT_STEP");
  IElementType CALLONCE_KEYWORD = new KarateTokenType("CALLONCE_KEYWORD");
  IElementType CALL_KEYWORD = new KarateTokenType("CALL_KEYWORD");
  IElementType COLON_OPERATOR = new KarateTokenType("COLON_OPERATOR");
  IElementType COMMA = new KarateTokenType("COMMA");
  IElementType COMMENT_STMT = new KarateTokenType("COMMENT_STMT");
  IElementType CONFIGURE_KEYWORD = new KarateTokenType("CONFIGURE_KEYWORD");
  IElementType DEF_KEYWORD = new KarateTokenType("DEF_KEYWORD");
  IElementType DIVIDE_OPERATOR = new KarateTokenType("DIVIDE_OPERATOR");
  IElementType DOC_STRING_CONTENT = new KarateTokenType("DOC_STRING_CONTENT");
  IElementType DOC_STRING_END = new KarateTokenType("DOC_STRING_END");
  IElementType DOC_STRING_START = new KarateTokenType("DOC_STRING_START");
  IElementType DOT_OPERATOR = new KarateTokenType("DOT_OPERATOR");
  IElementType DRIVER_KEYWORD = new KarateTokenType("DRIVER_KEYWORD");
  IElementType ELSE_KEYWORD = new KarateTokenType("ELSE_KEYWORD");
  IElementType EQUALS_OPERATOR = new KarateTokenType("EQUALS_OPERATOR");
  IElementType EXAMPLES_KEYWORD = new KarateTokenType("EXAMPLES_KEYWORD");
  IElementType FEATURE_KEYWORD = new KarateTokenType("FEATURE_KEYWORD");
  IElementType FUNCTION_KEYWORD = new KarateTokenType("FUNCTION_KEYWORD");
  IElementType GIVEN_STEP = new KarateTokenType("GIVEN_STEP");
  IElementType GREATER_THAN_EQUAL_OPERATOR = new KarateTokenType("GREATER_THAN_EQUAL_OPERATOR");
  IElementType GREATER_THAN_OPERATOR = new KarateTokenType("GREATER_THAN_OPERATOR");
  IElementType IDENTIFIER_KEYWORD = new KarateTokenType("IDENTIFIER_KEYWORD");
  IElementType IF_KEYWORD = new KarateTokenType("IF_KEYWORD");
  IElementType JSON_KEYWORD = new KarateTokenType("JSON_KEYWORD");
  IElementType KARATE_KEYWORD = new KarateTokenType("KARATE_KEYWORD");
  IElementType LESS_THAN_EQUAL_OPERATOR = new KarateTokenType("LESS_THAN_EQUAL_OPERATOR");
  IElementType LESS_THAN_OPERATOR = new KarateTokenType("LESS_THAN_OPERATOR");
  IElementType LOGICAL_AND_OPERATOR = new KarateTokenType("LOGICAL_AND_OPERATOR");
  IElementType LOGICAL_OR_OPERATOR = new KarateTokenType("LOGICAL_OR_OPERATOR");
  IElementType LOGICAL_XOR_OPERATOR = new KarateTokenType("LOGICAL_XOR_OPERATOR");
  IElementType L_CURLY = new KarateTokenType("L_CURLY");
  IElementType L_PAREN = new KarateTokenType("L_PAREN");
  IElementType L_SQUARE = new KarateTokenType("L_SQUARE");
  IElementType MATCH_KEYWORD = new KarateTokenType("MATCH_KEYWORD");
  IElementType MINUS_OPERATOR = new KarateTokenType("MINUS_OPERATOR");
  IElementType MODULO_OPERATOR = new KarateTokenType("MODULO_OPERATOR");
  IElementType MULTIPLY_OPERATOR = new KarateTokenType("MULTIPLY_OPERATOR");
  IElementType NEWLINE = new KarateTokenType("NEWLINE");
  IElementType NOT_EQUALS_OPERATOR = new KarateTokenType("NOT_EQUALS_OPERATOR");
  IElementType NULL_KEYWORD = new KarateTokenType("NULL_KEYWORD");
  IElementType NULL_LITERAL = new KarateTokenType("NULL_LITERAL");
  IElementType NUMBER_LITERAL = new KarateTokenType("NUMBER_LITERAL");
  IElementType OCTOTHORPE = new KarateTokenType("OCTOTHORPE");
  IElementType PIPE_OPERATOR = new KarateTokenType("PIPE_OPERATOR");
  IElementType PLUS_OPERATOR = new KarateTokenType("PLUS_OPERATOR");
  IElementType PRINT_KEYWORD = new KarateTokenType("PRINT_KEYWORD");
  IElementType READ_KEYWORD = new KarateTokenType("READ_KEYWORD");
  IElementType RETURN_KEYWORD = new KarateTokenType("RETURN_KEYWORD");
  IElementType R_CURLY = new KarateTokenType("R_CURLY");
  IElementType R_PAREN = new KarateTokenType("R_PAREN");
  IElementType R_SQUARE = new KarateTokenType("R_SQUARE");
  IElementType SCENARIO_KEYWORD = new KarateTokenType("SCENARIO_KEYWORD");
  IElementType SCENARIO_OUTLINE_KEYWORD = new KarateTokenType("SCENARIO_OUTLINE_KEYWORD");
  IElementType SET_KEYWORD = new KarateTokenType("SET_KEYWORD");
  IElementType STAR_STEP = new KarateTokenType("STAR_STEP");
  IElementType STRING_LITERAL = new KarateTokenType("STRING_LITERAL");
  IElementType TABLE_KEYWORD = new KarateTokenType("TABLE_KEYWORD");
  IElementType TABLE_ROW = new KarateTokenType("TABLE_ROW");
  IElementType TAGS_KEY = new KarateTokenType("TAGS_KEY");
  IElementType TERNARY_OPERATOR = new KarateTokenType("TERNARY_OPERATOR");
  IElementType THEN_STEP = new KarateTokenType("THEN_STEP");
  IElementType TILDE_OPERATOR = new KarateTokenType("TILDE_OPERATOR");
  IElementType WHEN_STEP = new KarateTokenType("WHEN_STEP");
  IElementType WORD_KEY = new KarateTokenType("WORD_KEY");
  IElementType WS_KEY = new KarateTokenType("WS_KEY");
  IElementType XMLSTRING_KEYWORD = new KarateTokenType("XMLSTRING_KEYWORD");
  IElementType XML_KEYWORD = new KarateTokenType("XML_KEYWORD");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ASSIGNMENT) {
        return new KarateAssignmentImpl(node);
      }
      else if (type == BACKGROUND) {
        return new KarateBackgroundImpl(node);
      }
      else if (type == BASE) {
        return new KarateBaseImpl(node);
      }
      else if (type == COMMENT) {
        return new KarateCommentImpl(node);
      }
      else if (type == DESCRIPTION) {
        return new KarateDescriptionImpl(node);
      }
      else if (type == DOC_CONTENT) {
        return new KarateDocContentImpl(node);
      }
      else if (type == DOC_STRING) {
        return new KarateDocStringImpl(node);
      }
      else if (type == EXAMPLES) {
        return new KarateExamplesImpl(node);
      }
      else if (type == EXPRESSION) {
        return new KarateExpressionImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new KarateFunctionCallImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new KarateIdentifierImpl(node);
      }
      else if (type == JSON_ARRAY) {
        return new KarateJsonArrayImpl(node);
      }
      else if (type == JSON_OBJECT) {
        return new KarateJsonObjectImpl(node);
      }
      else if (type == KEYWORD) {
        return new KarateKeywordImpl(node);
      }
      else if (type == LINE) {
        return new KarateLineImpl(node);
      }
      else if (type == LITERALS) {
        return new KarateLiteralsImpl(node);
      }
      else if (type == OPERATOR) {
        return new KarateOperatorImpl(node);
      }
      else if (type == POSTFIX) {
        return new KaratePostfixImpl(node);
      }
      else if (type == PRIMARY) {
        return new KaratePrimaryImpl(node);
      }
      else if (type == SCENARIO) {
        return new KarateScenarioImpl(node);
      }
      else if (type == SCENARIO_OUTLINE) {
        return new KarateScenarioOutlineImpl(node);
      }
      else if (type == STEP) {
        return new KarateStepImpl(node);
      }
      else if (type == TABLE) {
        return new KarateTableImpl(node);
      }
      else if (type == TAGS) {
        return new KarateTagsImpl(node);
      }
      else if (type == UNARY_OPERATOR) {
        return new KarateUnaryOperatorImpl(node);
      }
      else if (type == VAR_TYPE) {
        return new KarateVarTypeImpl(node);
      }
      else if (type == WORD) {
        return new KarateWordImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
