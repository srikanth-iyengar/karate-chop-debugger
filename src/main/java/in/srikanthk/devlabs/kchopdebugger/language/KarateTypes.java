// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import in.srikanthk.devlabs.kchopdebugger.language.psi.impl.*;

public interface KarateTypes {

  IElementType BACKGROUND = new KarateElementType("BACKGROUND");
  IElementType DESCRIPTION_TEXT = new KarateElementType("DESCRIPTION_TEXT");
  IElementType DOC_STRING = new KarateElementType("DOC_STRING");
  IElementType EXAMPLES = new KarateElementType("EXAMPLES");
  IElementType EXAMPLE_DESCRIPTION = new KarateElementType("EXAMPLE_DESCRIPTION");
  IElementType FEATURE_DESCRIPTION = new KarateElementType("FEATURE_DESCRIPTION");
  IElementType FEATURE_HEADER = new KarateElementType("FEATURE_HEADER");
  IElementType FEATURE_TAGS = new KarateElementType("FEATURE_TAGS");
  IElementType LINE = new KarateElementType("LINE");
  IElementType PREFIX = new KarateElementType("PREFIX");
  IElementType SCENARIO = new KarateElementType("SCENARIO");
  IElementType SCENARIO_DESCRIPTION = new KarateElementType("SCENARIO_DESCRIPTION");
  IElementType SCENARIO_OUTLINE = new KarateElementType("SCENARIO_OUTLINE");
  IElementType STEP = new KarateElementType("STEP");
  IElementType TABLE = new KarateElementType("TABLE");
  IElementType TAGS = new KarateElementType("TAGS");

  IElementType AND = new KarateTokenType("AND");
  IElementType BACKGROUND_KEY = new KarateTokenType("BACKGROUND_KEY");
  IElementType BUT = new KarateTokenType("BUT");
  IElementType CHAR = new KarateTokenType("CHAR");
  IElementType DOC_STRING_KEY = new KarateTokenType("DOC_STRING_KEY");
  IElementType EXAMPLES_KEY = new KarateTokenType("EXAMPLES_KEY");
  IElementType FEATURE_KEY = new KarateTokenType("FEATURE_KEY");
  IElementType FEATURE_TAGS_KEY = new KarateTokenType("FEATURE_TAGS_KEY");
  IElementType GIVEN = new KarateTokenType("GIVEN");
  IElementType NEWLINE = new KarateTokenType("NEWLINE");
  IElementType SCENARIO_KEY = new KarateTokenType("SCENARIO_KEY");
  IElementType SCENARIO_OUTLINE_KEY = new KarateTokenType("SCENARIO_OUTLINE_KEY");
  IElementType STAR = new KarateTokenType("STAR");
  IElementType TABLE_ROW = new KarateTokenType("TABLE_ROW");
  IElementType TAGS_KEY = new KarateTokenType("TAGS_KEY");
  IElementType THEN = new KarateTokenType("THEN");
  IElementType WHEN = new KarateTokenType("WHEN");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BACKGROUND) {
        return new KarateBackgroundImpl(node);
      }
      else if (type == DESCRIPTION_TEXT) {
        return new KarateDescriptionTextImpl(node);
      }
      else if (type == DOC_STRING) {
        return new KarateDocStringImpl(node);
      }
      else if (type == EXAMPLES) {
        return new KarateExamplesImpl(node);
      }
      else if (type == EXAMPLE_DESCRIPTION) {
        return new KarateExampleDescriptionImpl(node);
      }
      else if (type == FEATURE_DESCRIPTION) {
        return new KarateFeatureDescriptionImpl(node);
      }
      else if (type == FEATURE_HEADER) {
        return new KarateFeatureHeaderImpl(node);
      }
      else if (type == FEATURE_TAGS) {
        return new KarateFeatureTagsImpl(node);
      }
      else if (type == LINE) {
        return new KarateLineImpl(node);
      }
      else if (type == PREFIX) {
        return new KaratePrefixImpl(node);
      }
      else if (type == SCENARIO) {
        return new KarateScenarioImpl(node);
      }
      else if (type == SCENARIO_DESCRIPTION) {
        return new KarateScenarioDescriptionImpl(node);
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
