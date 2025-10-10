// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class KarateParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return feature(b, l + 1);
  }

  /* ********************************************************** */
  // BACKGROUND_KEY scenarioDescription step*
  public static boolean background(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background")) return false;
    if (!nextTokenIs(b, BACKGROUND_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKGROUND_KEY);
    r = r && scenarioDescription(b, l + 1);
    r = r && background_2(b, l + 1);
    exit_section_(b, m, BACKGROUND, r);
    return r;
  }

  // step*
  private static boolean background_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "background_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(BACKGROUND_KEY | SCENARIO_KEY | SCENARIO_OUTLINE_KEY | TAGS_KEY | STAR | GIVEN | WHEN | THEN | AND | BUT | EXAMPLES_KEY | TABLE_ROW | DOC_STRING_KEY) CHAR
  public static boolean description_text(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "description_text")) return false;
    if (!nextTokenIs(b, CHAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = description_text_0(b, l + 1);
    r = r && consumeToken(b, CHAR);
    exit_section_(b, m, DESCRIPTION_TEXT, r);
    return r;
  }

  // !(BACKGROUND_KEY | SCENARIO_KEY | SCENARIO_OUTLINE_KEY | TAGS_KEY | STAR | GIVEN | WHEN | THEN | AND | BUT | EXAMPLES_KEY | TABLE_ROW | DOC_STRING_KEY)
  private static boolean description_text_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "description_text_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !description_text_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BACKGROUND_KEY | SCENARIO_KEY | SCENARIO_OUTLINE_KEY | TAGS_KEY | STAR | GIVEN | WHEN | THEN | AND | BUT | EXAMPLES_KEY | TABLE_ROW | DOC_STRING_KEY
  private static boolean description_text_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "description_text_0_0")) return false;
    boolean r;
    r = consumeToken(b, BACKGROUND_KEY);
    if (!r) r = consumeToken(b, SCENARIO_KEY);
    if (!r) r = consumeToken(b, SCENARIO_OUTLINE_KEY);
    if (!r) r = consumeToken(b, TAGS_KEY);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, GIVEN);
    if (!r) r = consumeToken(b, WHEN);
    if (!r) r = consumeToken(b, THEN);
    if (!r) r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, BUT);
    if (!r) r = consumeToken(b, EXAMPLES_KEY);
    if (!r) r = consumeToken(b, TABLE_ROW);
    if (!r) r = consumeToken(b, DOC_STRING_KEY);
    return r;
  }

  /* ********************************************************** */
  // DOC_STRING_KEY
  public static boolean docString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "docString")) return false;
    if (!nextTokenIs(b, DOC_STRING_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOC_STRING_KEY);
    exit_section_(b, m, DOC_STRING, r);
    return r;
  }

  /* ********************************************************** */
  // description_text*
  public static boolean exampleDescription(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exampleDescription")) return false;
    Marker m = enter_section_(b, l, _NONE_, EXAMPLE_DESCRIPTION, "<example description>");
    while (true) {
      int c = current_position_(b);
      if (!description_text(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "exampleDescription", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // tags? EXAMPLES_KEY exampleDescription table
  public static boolean examples(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples")) return false;
    if (!nextTokenIs(b, "<examples>", EXAMPLES_KEY, TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXAMPLES, "<examples>");
    r = examples_0(b, l + 1);
    r = r && consumeToken(b, EXAMPLES_KEY);
    r = r && exampleDescription(b, l + 1);
    r = r && table(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // tags?
  private static boolean examples_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples_0")) return false;
    tags(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // featureHeader background? (scenario | scenarioOutline)* NEWLINE? <<eof>>
  static boolean feature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature")) return false;
    if (!nextTokenIs(b, "", FEATURE_KEY, FEATURE_TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = featureHeader(b, l + 1);
    r = r && feature_1(b, l + 1);
    r = r && feature_2(b, l + 1);
    r = r && feature_3(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // background?
  private static boolean feature_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_1")) return false;
    background(b, l + 1);
    return true;
  }

  // (scenario | scenarioOutline)*
  private static boolean feature_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!feature_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "feature_2", c)) break;
    }
    return true;
  }

  // scenario | scenarioOutline
  private static boolean feature_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_2_0")) return false;
    boolean r;
    r = scenario(b, l + 1);
    if (!r) r = scenarioOutline(b, l + 1);
    return r;
  }

  // NEWLINE?
  private static boolean feature_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_3")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  /* ********************************************************** */
  // description_text*
  public static boolean featureDescription(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "featureDescription")) return false;
    Marker m = enter_section_(b, l, _NONE_, FEATURE_DESCRIPTION, "<feature description>");
    while (true) {
      int c = current_position_(b);
      if (!description_text(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "featureDescription", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // featureTags? FEATURE_KEY featureDescription
  public static boolean featureHeader(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "featureHeader")) return false;
    if (!nextTokenIs(b, "<feature header>", FEATURE_KEY, FEATURE_TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FEATURE_HEADER, "<feature header>");
    r = featureHeader_0(b, l + 1);
    r = r && consumeToken(b, FEATURE_KEY);
    r = r && featureDescription(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // featureTags?
  private static boolean featureHeader_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "featureHeader_0")) return false;
    featureTags(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FEATURE_TAGS_KEY+
  public static boolean featureTags(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "featureTags")) return false;
    if (!nextTokenIs(b, FEATURE_TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FEATURE_TAGS_KEY);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, FEATURE_TAGS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "featureTags", c)) break;
    }
    exit_section_(b, m, FEATURE_TAGS, r);
    return r;
  }

  /* ********************************************************** */
  // CHAR+
  public static boolean line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line")) return false;
    if (!nextTokenIs(b, CHAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CHAR);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, CHAR)) break;
      if (!empty_element_parsed_guard_(b, "line", c)) break;
    }
    exit_section_(b, m, LINE, r);
    return r;
  }

  /* ********************************************************** */
  // STAR | GIVEN | WHEN | THEN | AND | BUT
  public static boolean prefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIX, "<prefix>");
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, GIVEN);
    if (!r) r = consumeToken(b, WHEN);
    if (!r) r = consumeToken(b, THEN);
    if (!r) r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, BUT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // tags? SCENARIO_KEY scenarioDescription step*
  public static boolean scenario(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario")) return false;
    if (!nextTokenIs(b, "<scenario>", SCENARIO_KEY, TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO, "<scenario>");
    r = scenario_0(b, l + 1);
    r = r && consumeToken(b, SCENARIO_KEY);
    r = r && scenarioDescription(b, l + 1);
    r = r && scenario_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // tags?
  private static boolean scenario_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_0")) return false;
    tags(b, l + 1);
    return true;
  }

  // step*
  private static boolean scenario_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // description_text*
  public static boolean scenarioDescription(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenarioDescription")) return false;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO_DESCRIPTION, "<scenario description>");
    while (true) {
      int c = current_position_(b);
      if (!description_text(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenarioDescription", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // tags? SCENARIO_OUTLINE_KEY scenarioDescription step* examples+
  public static boolean scenarioOutline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenarioOutline")) return false;
    if (!nextTokenIs(b, "<scenario outline>", SCENARIO_OUTLINE_KEY, TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO_OUTLINE, "<scenario outline>");
    r = scenarioOutline_0(b, l + 1);
    r = r && consumeToken(b, SCENARIO_OUTLINE_KEY);
    r = r && scenarioDescription(b, l + 1);
    r = r && scenarioOutline_3(b, l + 1);
    r = r && scenarioOutline_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // tags?
  private static boolean scenarioOutline_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenarioOutline_0")) return false;
    tags(b, l + 1);
    return true;
  }

  // step*
  private static boolean scenarioOutline_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenarioOutline_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenarioOutline_3", c)) break;
    }
    return true;
  }

  // examples+
  private static boolean scenarioOutline_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenarioOutline_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = examples(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!examples(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenarioOutline_4", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // prefix line (docString | table)?
  public static boolean step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STEP, "<step>");
    r = prefix(b, l + 1);
    r = r && line(b, l + 1);
    r = r && step_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (docString | table)?
  private static boolean step_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_2")) return false;
    step_2_0(b, l + 1);
    return true;
  }

  // docString | table
  private static boolean step_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_2_0")) return false;
    boolean r;
    r = docString(b, l + 1);
    if (!r) r = table(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // TABLE_ROW+
  public static boolean table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table")) return false;
    if (!nextTokenIs(b, TABLE_ROW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TABLE_ROW);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TABLE_ROW)) break;
      if (!empty_element_parsed_guard_(b, "table", c)) break;
    }
    exit_section_(b, m, TABLE, r);
    return r;
  }

  /* ********************************************************** */
  // TAGS_KEY+
  public static boolean tags(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tags")) return false;
    if (!nextTokenIs(b, TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TAGS_KEY);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TAGS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "tags", c)) break;
    }
    exit_section_(b, m, TAGS, r);
    return r;
  }

}
