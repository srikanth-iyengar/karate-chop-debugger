// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import in.srikanthk.devlabs.kchopdebugger.language.psi.impl.*;

public interface KarateTypes {

  IElementType BACKGROUND = new KarateElementType("BACKGROUND");
  IElementType COMMENT = new KarateElementType("COMMENT");
  IElementType DOC_STRING = new KarateElementType("DOC_STRING");
  IElementType EXAMPLES = new KarateElementType("EXAMPLES");
  IElementType FEATURE_DESCRIPTION = new KarateElementType("FEATURE_DESCRIPTION");
  IElementType LINE = new KarateElementType("LINE");
  IElementType SCENARIO = new KarateElementType("SCENARIO");
  IElementType SCENARIO_OUTLINE = new KarateElementType("SCENARIO_OUTLINE");
  IElementType STEP = new KarateElementType("STEP");
  IElementType TABLE = new KarateElementType("TABLE");
  IElementType TAGS = new KarateElementType("TAGS");

  IElementType AND_STEP = new KarateTokenType("AND_STEP");
  IElementType BACKGROUND_KEYWORD = new KarateTokenType("BACKGROUND_KEYWORD");
  IElementType BUT_STEP = new KarateTokenType("BUT_STEP");
  IElementType COMMENT_STMT = new KarateTokenType("COMMENT_STMT");
  IElementType DOC_STRING_KEY = new KarateTokenType("DOC_STRING_KEY");
  IElementType EXAMPLES_KEYWORD = new KarateTokenType("EXAMPLES_KEYWORD");
  IElementType FEATURE_KEYWORD = new KarateTokenType("FEATURE_KEYWORD");
  IElementType GIVEN_STEP = new KarateTokenType("GIVEN_STEP");
  IElementType NEWLINE = new KarateTokenType("NEWLINE");
  IElementType SCENARIO_KEYWORD = new KarateTokenType("SCENARIO_KEYWORD");
  IElementType SCENARIO_OUTLINE_KEYWORD = new KarateTokenType("SCENARIO_OUTLINE_KEYWORD");
  IElementType STAR_STEP = new KarateTokenType("STAR_STEP");
  IElementType TABLE_ROW = new KarateTokenType("TABLE_ROW");
  IElementType TAGS_KEY = new KarateTokenType("TAGS_KEY");
  IElementType TEXT = new KarateTokenType("TEXT");
  IElementType THEN_STEP = new KarateTokenType("THEN_STEP");
  IElementType WHEN_STEP = new KarateTokenType("WHEN_STEP");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BACKGROUND) {
        return new KarateBackgroundImpl(node);
      }
      else if (type == COMMENT) {
        return new KarateCommentImpl(node);
      }
      else if (type == DOC_STRING) {
        return new KarateDocStringImpl(node);
      }
      else if (type == EXAMPLES) {
        return new KarateExamplesImpl(node);
      }
      else if (type == FEATURE_DESCRIPTION) {
        return new KarateFeatureDescriptionImpl(node);
      }
      else if (type == LINE) {
        return new KarateLineImpl(node);
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
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
