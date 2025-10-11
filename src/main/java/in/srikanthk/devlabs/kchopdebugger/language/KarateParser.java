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
  // tags? BACKGROUND_KEYWORD TEXT* (step | doc_string | table)*
  public static boolean background(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BACKGROUND, "<background>");
    r = background_0(b, l + 1);
    r = r && consumeToken(b, BACKGROUND_KEYWORD);
    r = r && background_2(b, l + 1);
    r = r && background_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // tags?
  private static boolean background_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_0")) return false;
    tags(b, l + 1);
    return true;
  }

  // TEXT*
  private static boolean background_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "background_2", c)) break;
    }
    return true;
  }

  // (step | doc_string | table)*
  private static boolean background_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!background_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "background_3", c)) break;
    }
    return true;
  }

  // step | doc_string | table
  private static boolean background_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_3_0")) return false;
    boolean r;
    r = step(b, l + 1);
    if (!r) r = doc_string(b, l + 1);
    if (!r) r = table(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // COMMENT_STMT NEWLINE
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, COMMENT_STMT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMENT_STMT, NEWLINE);
    exit_section_(b, m, COMMENT, r);
    return r;
  }

  /* ********************************************************** */
  // DOC_STRING_KEY TEXT* DOC_STRING_KEY
  public static boolean doc_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doc_string")) return false;
    if (!nextTokenIs(b, DOC_STRING_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOC_STRING_KEY);
    r = r && doc_string_1(b, l + 1);
    r = r && consumeToken(b, DOC_STRING_KEY);
    exit_section_(b, m, DOC_STRING, r);
    return r;
  }

  // TEXT*
  private static boolean doc_string_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doc_string_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "doc_string_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // EXAMPLES_KEYWORD table*
  public static boolean examples(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples")) return false;
    if (!nextTokenIs(b, EXAMPLES_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXAMPLES_KEYWORD);
    r = r && examples_1(b, l + 1);
    exit_section_(b, m, EXAMPLES, r);
    return r;
  }

  // table*
  private static boolean examples_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "examples_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // tags? FEATURE_KEYWORD feature_description? background? (scenario | scenario_outline)* NEWLINE*
  static boolean feature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = feature_0(b, l + 1);
    r = r && consumeToken(b, FEATURE_KEYWORD);
    r = r && feature_2(b, l + 1);
    r = r && feature_3(b, l + 1);
    r = r && feature_4(b, l + 1);
    r = r && feature_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // tags?
  private static boolean feature_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_0")) return false;
    tags(b, l + 1);
    return true;
  }

  // feature_description?
  private static boolean feature_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_2")) return false;
    feature_description(b, l + 1);
    return true;
  }

  // background?
  private static boolean feature_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_3")) return false;
    background(b, l + 1);
    return true;
  }

  // (scenario | scenario_outline)*
  private static boolean feature_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!feature_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "feature_4", c)) break;
    }
    return true;
  }

  // scenario | scenario_outline
  private static boolean feature_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_4_0")) return false;
    boolean r;
    r = scenario(b, l + 1);
    if (!r) r = scenario_outline(b, l + 1);
    return r;
  }

  // NEWLINE*
  private static boolean feature_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "feature_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // line+
  public static boolean feature_description(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_description")) return false;
    if (!nextTokenIs(b, "<feature description>", NEWLINE, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FEATURE_DESCRIPTION, "<feature description>");
    r = line(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!line(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "feature_description", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NEWLINE? TEXT+
  public static boolean line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line")) return false;
    if (!nextTokenIs(b, "<line>", NEWLINE, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LINE, "<line>");
    r = line_0(b, l + 1);
    r = r && line_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEWLINE?
  private static boolean line_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_0")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // TEXT+
  private static boolean line_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TEXT);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "line_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // tags? SCENARIO_KEYWORD TEXT* (step | doc_string | table)*
  public static boolean scenario(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO, "<scenario>");
    r = scenario_0(b, l + 1);
    r = r && consumeToken(b, SCENARIO_KEYWORD);
    r = r && scenario_2(b, l + 1);
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

  // TEXT*
  private static boolean scenario_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "scenario_2", c)) break;
    }
    return true;
  }

  // (step | doc_string | table)*
  private static boolean scenario_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scenario_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_3", c)) break;
    }
    return true;
  }

  // step | doc_string | table
  private static boolean scenario_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_3_0")) return false;
    boolean r;
    r = step(b, l + 1);
    if (!r) r = doc_string(b, l + 1);
    if (!r) r = table(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // tags? SCENARIO_OUTLINE_KEYWORD TEXT* (step | doc_string | table)* examples
  public static boolean scenario_outline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO_OUTLINE, "<scenario outline>");
    r = scenario_outline_0(b, l + 1);
    r = r && consumeToken(b, SCENARIO_OUTLINE_KEYWORD);
    r = r && scenario_outline_2(b, l + 1);
    r = r && scenario_outline_3(b, l + 1);
    r = r && examples(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // tags?
  private static boolean scenario_outline_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_0")) return false;
    tags(b, l + 1);
    return true;
  }

  // TEXT*
  private static boolean scenario_outline_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "scenario_outline_2", c)) break;
    }
    return true;
  }

  // (step | doc_string | table)*
  private static boolean scenario_outline_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scenario_outline_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_outline_3", c)) break;
    }
    return true;
  }

  // step | doc_string | table
  private static boolean scenario_outline_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_3_0")) return false;
    boolean r;
    r = step(b, l + 1);
    if (!r) r = doc_string(b, l + 1);
    if (!r) r = table(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP) TEXT*
  public static boolean step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STEP, "<step>");
    r = step_0(b, l + 1);
    r = r && step_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP
  private static boolean step_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0")) return false;
    boolean r;
    r = consumeToken(b, STAR_STEP);
    if (!r) r = consumeToken(b, GIVEN_STEP);
    if (!r) r = consumeToken(b, WHEN_STEP);
    if (!r) r = consumeToken(b, THEN_STEP);
    if (!r) r = consumeToken(b, AND_STEP);
    if (!r) r = consumeToken(b, BUT_STEP);
    return r;
  }

  // TEXT*
  private static boolean step_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TEXT)) break;
      if (!empty_element_parsed_guard_(b, "step_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // NEWLINE? TABLE_ROW+
  public static boolean table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table")) return false;
    if (!nextTokenIs(b, "<table>", NEWLINE, TABLE_ROW)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE, "<table>");
    r = table_0(b, l + 1);
    r = r && table_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEWLINE?
  private static boolean table_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_0")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // TABLE_ROW+
  private static boolean table_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TABLE_ROW);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TABLE_ROW)) break;
      if (!empty_element_parsed_guard_(b, "table_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // NEWLINE? TAGS_KEY+
  public static boolean tags(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tags")) return false;
    if (!nextTokenIs(b, "<tags>", NEWLINE, TAGS_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TAGS, "<tags>");
    r = tags_0(b, l + 1);
    r = r && tags_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEWLINE?
  private static boolean tags_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tags_0")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // TAGS_KEY+
  private static boolean tags_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tags_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TAGS_KEY);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TAGS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "tags_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

}
