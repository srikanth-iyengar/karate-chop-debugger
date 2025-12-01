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
  // var_type? WS_KEY* identifier WS_KEY* ASSIGNMENT_OPERATOR WS_KEY* expression?
  public static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT, "<assignment>");
    r = assignment_0(b, l + 1);
    r = r && assignment_1(b, l + 1);
    r = r && identifier(b, l + 1);
    r = r && assignment_3(b, l + 1);
    r = r && consumeToken(b, ASSIGNMENT_OPERATOR);
    r = r && assignment_5(b, l + 1);
    r = r && assignment_6(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // var_type?
  private static boolean assignment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_0")) return false;
    var_type(b, l + 1);
    return true;
  }

  // WS_KEY*
  private static boolean assignment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "assignment_1", c)) break;
    }
    return true;
  }

  // WS_KEY*
  private static boolean assignment_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "assignment_3", c)) break;
    }
    return true;
  }

  // WS_KEY*
  private static boolean assignment_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "assignment_5", c)) break;
    }
    return true;
  }

  // expression?
  private static boolean assignment_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_6")) return false;
    expression(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (comment|tags)* NEWLINE? BACKGROUND_KEYWORD description* step*
  public static boolean background(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BACKGROUND, "<background>");
    r = background_0(b, l + 1);
    r = r && background_1(b, l + 1);
    r = r && consumeToken(b, BACKGROUND_KEYWORD);
    r = r && background_3(b, l + 1);
    r = r && background_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (comment|tags)*
  private static boolean background_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!background_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "background_0", c)) break;
    }
    return true;
  }

  // comment|tags
  private static boolean background_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_0_0")) return false;
    boolean r;
    r = comment(b, l + 1);
    if (!r) r = tags(b, l + 1);
    return r;
  }

  // NEWLINE?
  private static boolean background_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_1")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // description*
  private static boolean background_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!description(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "background_3", c)) break;
    }
    return true;
  }

  // step*
  private static boolean background_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "background_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "background_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // identifier
  //        | literals
  //        | assignment
  //        | L_PAREN expression R_PAREN
  public static boolean base(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BASE, "<base>");
    r = identifier(b, l + 1);
    if (!r) r = literals(b, l + 1);
    if (!r) r = assignment(b, l + 1);
    if (!r) r = base_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // L_PAREN expression R_PAREN
  private static boolean base_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_PAREN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, R_PAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COMMENT_STMT
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, COMMENT_STMT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMENT_STMT);
    exit_section_(b, m, COMMENT, r);
    return r;
  }

  /* ********************************************************** */
  // (line)+
  public static boolean description(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "description")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DESCRIPTION, "<description>");
    r = description_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!description_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "description", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (line)
  private static boolean description_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "description_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = line(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DOC_STRING_CONTENT*
  public static boolean doc_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doc_content")) return false;
    Marker m = enter_section_(b, l, _NONE_, DOC_CONTENT, "<doc content>");
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, DOC_STRING_CONTENT)) break;
      if (!empty_element_parsed_guard_(b, "doc_content", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // comment? DOC_STRING_START doc_content DOC_STRING_END
  public static boolean doc_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doc_string")) return false;
    if (!nextTokenIs(b, "<doc string>", COMMENT_STMT, DOC_STRING_START)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DOC_STRING, "<doc string>");
    r = doc_string_0(b, l + 1);
    r = r && consumeToken(b, DOC_STRING_START);
    r = r && doc_content(b, l + 1);
    r = r && consumeToken(b, DOC_STRING_END);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // comment?
  private static boolean doc_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doc_string_0")) return false;
    comment(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // comment? EXAMPLES_KEYWORD table*
  public static boolean examples(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples")) return false;
    if (!nextTokenIs(b, "<examples>", COMMENT_STMT, EXAMPLES_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXAMPLES, "<examples>");
    r = examples_0(b, l + 1);
    r = r && consumeToken(b, EXAMPLES_KEYWORD);
    r = r && examples_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // comment?
  private static boolean examples_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples_0")) return false;
    comment(b, l + 1);
    return true;
  }

  // table*
  private static boolean examples_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "examples_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "examples_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // unary_operator? primary ((operator | WS_KEY*) expression)*
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = expression_0(b, l + 1);
    r = r && primary(b, l + 1);
    r = r && expression_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // unary_operator?
  private static boolean expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_0")) return false;
    unary_operator(b, l + 1);
    return true;
  }

  // ((operator | WS_KEY*) expression)*
  private static boolean expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expression_2", c)) break;
    }
    return true;
  }

  // (operator | WS_KEY*) expression
  private static boolean expression_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression_2_0_0(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // operator | WS_KEY*
  private static boolean expression_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operator(b, l + 1);
    if (!r) r = expression_2_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WS_KEY*
  private static boolean expression_2_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_2_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "expression_2_0_0_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // (comment|tags)* FEATURE_KEYWORD description? background? (scenario | scenario_outline)* NEWLINE*
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

  // (comment|tags)*
  private static boolean feature_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!feature_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "feature_0", c)) break;
    }
    return true;
  }

  // comment|tags
  private static boolean feature_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_0_0")) return false;
    boolean r;
    r = comment(b, l + 1);
    if (!r) r = tags(b, l + 1);
    return r;
  }

  // description?
  private static boolean feature_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "feature_2")) return false;
    description(b, l + 1);
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
  // L_PAREN (expression (WS_KEY* COMMA expression)*)? R_PAREN
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, L_PAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_PAREN);
    r = r && function_call_1(b, l + 1);
    r = r && consumeToken(b, R_PAREN);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // (expression (WS_KEY* COMMA expression)*)?
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    function_call_1_0(b, l + 1);
    return true;
  }

  // expression (WS_KEY* COMMA expression)*
  private static boolean function_call_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1);
    r = r && function_call_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (WS_KEY* COMMA expression)*
  private static boolean function_call_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_call_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_call_1_0_1", c)) break;
    }
    return true;
  }

  // WS_KEY* COMMA expression
  private static boolean function_call_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_call_1_0_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WS_KEY*
  private static boolean function_call_1_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1_0_1_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "function_call_1_0_1_0_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // keyword |
  //                IDENTIFIER_KEYWORD
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<identifier>");
    r = keyword(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER_KEYWORD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // L_SQUARE (COMMA? (json_object| json_array | NUMBER_LITERAL | STRING_LITERAL | BOOLEAN_LITERAL | NULL_LITERAL))* R_SQUARE
  public static boolean json_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_array")) return false;
    if (!nextTokenIs(b, L_SQUARE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_SQUARE);
    r = r && json_array_1(b, l + 1);
    r = r && consumeToken(b, R_SQUARE);
    exit_section_(b, m, JSON_ARRAY, r);
    return r;
  }

  // (COMMA? (json_object| json_array | NUMBER_LITERAL | STRING_LITERAL | BOOLEAN_LITERAL | NULL_LITERAL))*
  private static boolean json_array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_array_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!json_array_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "json_array_1", c)) break;
    }
    return true;
  }

  // COMMA? (json_object| json_array | NUMBER_LITERAL | STRING_LITERAL | BOOLEAN_LITERAL | NULL_LITERAL)
  private static boolean json_array_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_array_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = json_array_1_0_0(b, l + 1);
    r = r && json_array_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean json_array_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_array_1_0_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // json_object| json_array | NUMBER_LITERAL | STRING_LITERAL | BOOLEAN_LITERAL | NULL_LITERAL
  private static boolean json_array_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_array_1_0_1")) return false;
    boolean r;
    r = json_object(b, l + 1);
    if (!r) r = json_array(b, l + 1);
    if (!r) r = consumeToken(b, NUMBER_LITERAL);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    if (!r) r = consumeToken(b, BOOLEAN_LITERAL);
    if (!r) r = consumeToken(b, NULL_LITERAL);
    return r;
  }

  /* ********************************************************** */
  // L_CURLY (COMMA?
  //                     WS_KEY* (IDENTIFIER_KEYWORD | STRING_LITERAL) WS_KEY* COLON_OPERATOR
  //                         WS_KEY* (STRING_LITERAL |
  //                          NUMBER_LITERAL |
  //                          BOOLEAN_LITERAL |
  //                          NULL_LITERAL |
  //                          json_array |
  //                          json_object | (OCTOTHORPE L_PAREN expression R_PAREN)))* WS_KEY*
  //                 R_CURLY
  public static boolean json_object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object")) return false;
    if (!nextTokenIs(b, L_CURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_CURLY);
    r = r && json_object_1(b, l + 1);
    r = r && json_object_2(b, l + 1);
    r = r && consumeToken(b, R_CURLY);
    exit_section_(b, m, JSON_OBJECT, r);
    return r;
  }

  // (COMMA?
  //                     WS_KEY* (IDENTIFIER_KEYWORD | STRING_LITERAL) WS_KEY* COLON_OPERATOR
  //                         WS_KEY* (STRING_LITERAL |
  //                          NUMBER_LITERAL |
  //                          BOOLEAN_LITERAL |
  //                          NULL_LITERAL |
  //                          json_array |
  //                          json_object | (OCTOTHORPE L_PAREN expression R_PAREN)))*
  private static boolean json_object_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!json_object_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "json_object_1", c)) break;
    }
    return true;
  }

  // COMMA?
  //                     WS_KEY* (IDENTIFIER_KEYWORD | STRING_LITERAL) WS_KEY* COLON_OPERATOR
  //                         WS_KEY* (STRING_LITERAL |
  //                          NUMBER_LITERAL |
  //                          BOOLEAN_LITERAL |
  //                          NULL_LITERAL |
  //                          json_array |
  //                          json_object | (OCTOTHORPE L_PAREN expression R_PAREN))
  private static boolean json_object_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = json_object_1_0_0(b, l + 1);
    r = r && json_object_1_0_1(b, l + 1);
    r = r && json_object_1_0_2(b, l + 1);
    r = r && json_object_1_0_3(b, l + 1);
    r = r && consumeToken(b, COLON_OPERATOR);
    r = r && json_object_1_0_5(b, l + 1);
    r = r && json_object_1_0_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean json_object_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // WS_KEY*
  private static boolean json_object_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "json_object_1_0_1", c)) break;
    }
    return true;
  }

  // IDENTIFIER_KEYWORD | STRING_LITERAL
  private static boolean json_object_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_2")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER_KEYWORD);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    return r;
  }

  // WS_KEY*
  private static boolean json_object_1_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "json_object_1_0_3", c)) break;
    }
    return true;
  }

  // WS_KEY*
  private static boolean json_object_1_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "json_object_1_0_5", c)) break;
    }
    return true;
  }

  // STRING_LITERAL |
  //                          NUMBER_LITERAL |
  //                          BOOLEAN_LITERAL |
  //                          NULL_LITERAL |
  //                          json_array |
  //                          json_object | (OCTOTHORPE L_PAREN expression R_PAREN)
  private static boolean json_object_1_0_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING_LITERAL);
    if (!r) r = consumeToken(b, NUMBER_LITERAL);
    if (!r) r = consumeToken(b, BOOLEAN_LITERAL);
    if (!r) r = consumeToken(b, NULL_LITERAL);
    if (!r) r = json_array(b, l + 1);
    if (!r) r = json_object(b, l + 1);
    if (!r) r = json_object_1_0_6_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OCTOTHORPE L_PAREN expression R_PAREN
  private static boolean json_object_1_0_6_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_1_0_6_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OCTOTHORPE, L_PAREN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, R_PAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // WS_KEY*
  private static boolean json_object_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_object_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "json_object_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // KARATE_KEYWORD |
  //             CALL_KEYWORD |
  //             CALLONCE_KEYWORD |
  //             READ_KEYWORD |
  //             PRINT_KEYWORD |
  //             MATCH_KEYWORD |
  //             SET_KEYWORD |
  //             TABLE_KEYWORD |
  //             NULL_KEYWORD |
  //             DRIVER_KEYWORD |
  //             CONFIGURE_KEYWORD |
  //             FUNCTION_KEYWORD |
  //             RETURN_KEYWORD |
  //             IF_KEYWORD |
  //             ELSE_KEYWORD
  public static boolean keyword(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyword")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEYWORD, "<keyword>");
    r = consumeToken(b, KARATE_KEYWORD);
    if (!r) r = consumeToken(b, CALL_KEYWORD);
    if (!r) r = consumeToken(b, CALLONCE_KEYWORD);
    if (!r) r = consumeToken(b, READ_KEYWORD);
    if (!r) r = consumeToken(b, PRINT_KEYWORD);
    if (!r) r = consumeToken(b, MATCH_KEYWORD);
    if (!r) r = consumeToken(b, SET_KEYWORD);
    if (!r) r = consumeToken(b, TABLE_KEYWORD);
    if (!r) r = consumeToken(b, NULL_KEYWORD);
    if (!r) r = consumeToken(b, DRIVER_KEYWORD);
    if (!r) r = consumeToken(b, CONFIGURE_KEYWORD);
    if (!r) r = consumeToken(b, FUNCTION_KEYWORD);
    if (!r) r = consumeToken(b, RETURN_KEYWORD);
    if (!r) r = consumeToken(b, IF_KEYWORD);
    if (!r) r = consumeToken(b, ELSE_KEYWORD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NEWLINE? (word | WS_KEY)+
  public static boolean line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line")) return false;
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

  // (word | WS_KEY)+
  private static boolean line_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = line_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!line_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "line_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // word | WS_KEY
  private static boolean line_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_1_0")) return false;
    boolean r;
    r = word(b, l + 1);
    if (!r) r = consumeToken(b, WS_KEY);
    return r;
  }

  /* ********************************************************** */
  // STRING_LITERAL |
  //                NUMBER_LITERAL |
  //                NULL_LITERAL |
  //                BOOLEAN_LITERAL |
  //                json_object |
  //                json_array
  public static boolean literals(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literals")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERALS, "<literals>");
    r = consumeToken(b, STRING_LITERAL);
    if (!r) r = consumeToken(b, NUMBER_LITERAL);
    if (!r) r = consumeToken(b, NULL_LITERAL);
    if (!r) r = consumeToken(b, BOOLEAN_LITERAL);
    if (!r) r = json_object(b, l + 1);
    if (!r) r = json_array(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // WS_KEY* (PLUS_OPERATOR |
  //              MINUS_OPERATOR |
  //              MULTIPLY_OPERATOR |
  //              DIVIDE_OPERATOR |
  //              MODULO_OPERATOR |
  //              EQUALS_OPERATOR |
  //              NOT_EQUALS_OPERATOR |
  //              GREATER_THAN_OPERATOR |
  //              LESS_THAN_OPERATOR |
  //              LESS_THAN_EQUAL_OPERATOR |
  //              GREATER_THAN_EQUAL_OPERATOR |
  //              LOGICAL_OR_OPERATOR |
  //              LOGICAL_AND_OPERATOR |
  //              LOGICAL_XOR_OPERATOR |
  //              TERNARY_OPERATOR |
  //              COLON_OPERATOR |
  //              ASSIGNMENT_OPERATOR |
  //              PIPE_OPERATOR |
  //              AMPERSAND_OPERATOR |
  //              BANG_OPERATOR |
  //              ARROW_OPERATOR |
  //              TILDE_OPERATOR) WS_KEY*
  public static boolean operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR, "<operator>");
    r = operator_0(b, l + 1);
    r = r && operator_1(b, l + 1);
    r = r && operator_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // WS_KEY*
  private static boolean operator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "operator_0", c)) break;
    }
    return true;
  }

  // PLUS_OPERATOR |
  //              MINUS_OPERATOR |
  //              MULTIPLY_OPERATOR |
  //              DIVIDE_OPERATOR |
  //              MODULO_OPERATOR |
  //              EQUALS_OPERATOR |
  //              NOT_EQUALS_OPERATOR |
  //              GREATER_THAN_OPERATOR |
  //              LESS_THAN_OPERATOR |
  //              LESS_THAN_EQUAL_OPERATOR |
  //              GREATER_THAN_EQUAL_OPERATOR |
  //              LOGICAL_OR_OPERATOR |
  //              LOGICAL_AND_OPERATOR |
  //              LOGICAL_XOR_OPERATOR |
  //              TERNARY_OPERATOR |
  //              COLON_OPERATOR |
  //              ASSIGNMENT_OPERATOR |
  //              PIPE_OPERATOR |
  //              AMPERSAND_OPERATOR |
  //              BANG_OPERATOR |
  //              ARROW_OPERATOR |
  //              TILDE_OPERATOR
  private static boolean operator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_1")) return false;
    boolean r;
    r = consumeToken(b, PLUS_OPERATOR);
    if (!r) r = consumeToken(b, MINUS_OPERATOR);
    if (!r) r = consumeToken(b, MULTIPLY_OPERATOR);
    if (!r) r = consumeToken(b, DIVIDE_OPERATOR);
    if (!r) r = consumeToken(b, MODULO_OPERATOR);
    if (!r) r = consumeToken(b, EQUALS_OPERATOR);
    if (!r) r = consumeToken(b, NOT_EQUALS_OPERATOR);
    if (!r) r = consumeToken(b, GREATER_THAN_OPERATOR);
    if (!r) r = consumeToken(b, LESS_THAN_OPERATOR);
    if (!r) r = consumeToken(b, LESS_THAN_EQUAL_OPERATOR);
    if (!r) r = consumeToken(b, GREATER_THAN_EQUAL_OPERATOR);
    if (!r) r = consumeToken(b, LOGICAL_OR_OPERATOR);
    if (!r) r = consumeToken(b, LOGICAL_AND_OPERATOR);
    if (!r) r = consumeToken(b, LOGICAL_XOR_OPERATOR);
    if (!r) r = consumeToken(b, TERNARY_OPERATOR);
    if (!r) r = consumeToken(b, COLON_OPERATOR);
    if (!r) r = consumeToken(b, ASSIGNMENT_OPERATOR);
    if (!r) r = consumeToken(b, PIPE_OPERATOR);
    if (!r) r = consumeToken(b, AMPERSAND_OPERATOR);
    if (!r) r = consumeToken(b, BANG_OPERATOR);
    if (!r) r = consumeToken(b, ARROW_OPERATOR);
    if (!r) r = consumeToken(b, TILDE_OPERATOR);
    return r;
  }

  // WS_KEY*
  private static boolean operator_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "operator_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // function_call
  //           | DOT_OPERATOR identifier
  //           | L_SQUARE expression R_SQUARE
  //           | (WS_KEY* COMMA expression)
  public static boolean postfix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POSTFIX, "<postfix>");
    r = function_call(b, l + 1);
    if (!r) r = postfix_1(b, l + 1);
    if (!r) r = postfix_2(b, l + 1);
    if (!r) r = postfix_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // DOT_OPERATOR identifier
  private static boolean postfix_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT_OPERATOR);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // L_SQUARE expression R_SQUARE
  private static boolean postfix_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_SQUARE);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, R_SQUARE);
    exit_section_(b, m, null, r);
    return r;
  }

  // WS_KEY* COMMA expression
  private static boolean postfix_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = postfix_3_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WS_KEY*
  private static boolean postfix_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_3_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "postfix_3_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // base postfix*
  public static boolean primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY, "<primary>");
    r = base(b, l + 1);
    r = r && primary_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // postfix*
  private static boolean primary_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!postfix(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "primary_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // (comment|tags)* NEWLINE? SCENARIO_KEYWORD description? step*
  public static boolean scenario(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO, "<scenario>");
    r = scenario_0(b, l + 1);
    r = r && scenario_1(b, l + 1);
    r = r && consumeToken(b, SCENARIO_KEYWORD);
    r = r && scenario_3(b, l + 1);
    r = r && scenario_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (comment|tags)*
  private static boolean scenario_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scenario_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_0", c)) break;
    }
    return true;
  }

  // comment|tags
  private static boolean scenario_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_0_0")) return false;
    boolean r;
    r = comment(b, l + 1);
    if (!r) r = tags(b, l + 1);
    return r;
  }

  // NEWLINE?
  private static boolean scenario_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_1")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // description?
  private static boolean scenario_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_3")) return false;
    description(b, l + 1);
    return true;
  }

  // step*
  private static boolean scenario_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // (comment|tags)* NEWLINE? SCENARIO_OUTLINE_KEYWORD description? step* examples
  public static boolean scenario_outline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCENARIO_OUTLINE, "<scenario outline>");
    r = scenario_outline_0(b, l + 1);
    r = r && scenario_outline_1(b, l + 1);
    r = r && consumeToken(b, SCENARIO_OUTLINE_KEYWORD);
    r = r && scenario_outline_3(b, l + 1);
    r = r && scenario_outline_4(b, l + 1);
    r = r && examples(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (comment|tags)*
  private static boolean scenario_outline_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scenario_outline_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_outline_0", c)) break;
    }
    return true;
  }

  // comment|tags
  private static boolean scenario_outline_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_0_0")) return false;
    boolean r;
    r = comment(b, l + 1);
    if (!r) r = tags(b, l + 1);
    return r;
  }

  // NEWLINE?
  private static boolean scenario_outline_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_1")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // description?
  private static boolean scenario_outline_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_3")) return false;
    description(b, l + 1);
    return true;
  }

  // step*
  private static boolean scenario_outline_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_outline_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!step(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_outline_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // comment? NEWLINE? ((STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP) WS_KEY+ expression NEWLINE*) | doc_string | table
  public static boolean step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STEP, "<step>");
    r = step_0(b, l + 1);
    if (!r) r = doc_string(b, l + 1);
    if (!r) r = table(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // comment? NEWLINE? ((STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP) WS_KEY+ expression NEWLINE*)
  private static boolean step_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = step_0_0(b, l + 1);
    r = r && step_0_1(b, l + 1);
    r = r && step_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment?
  private static boolean step_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_0")) return false;
    comment(b, l + 1);
    return true;
  }

  // NEWLINE?
  private static boolean step_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_1")) return false;
    consumeToken(b, NEWLINE);
    return true;
  }

  // (STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP) WS_KEY+ expression NEWLINE*
  private static boolean step_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = step_0_2_0(b, l + 1);
    r = r && step_0_2_1(b, l + 1);
    r = r && expression(b, l + 1);
    r = r && step_0_2_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STAR_STEP | GIVEN_STEP | WHEN_STEP | THEN_STEP | AND_STEP | BUT_STEP
  private static boolean step_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_2_0")) return false;
    boolean r;
    r = consumeToken(b, STAR_STEP);
    if (!r) r = consumeToken(b, GIVEN_STEP);
    if (!r) r = consumeToken(b, WHEN_STEP);
    if (!r) r = consumeToken(b, THEN_STEP);
    if (!r) r = consumeToken(b, AND_STEP);
    if (!r) r = consumeToken(b, BUT_STEP);
    return r;
  }

  // WS_KEY+
  private static boolean step_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WS_KEY);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "step_0_2_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE*
  private static boolean step_0_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_0_2_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "step_0_2_3", c)) break;
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

  /* ********************************************************** */
  // WS_KEY* (PLUS_OPERATOR |
  //                  MINUS_OPERATOR |
  //                  BANG_OPERATOR |
  //                  TILDE_OPERATOR) WS_KEY*
  public static boolean unary_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_OPERATOR, "<unary operator>");
    r = unary_operator_0(b, l + 1);
    r = r && unary_operator_1(b, l + 1);
    r = r && unary_operator_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // WS_KEY*
  private static boolean unary_operator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "unary_operator_0", c)) break;
    }
    return true;
  }

  // PLUS_OPERATOR |
  //                  MINUS_OPERATOR |
  //                  BANG_OPERATOR |
  //                  TILDE_OPERATOR
  private static boolean unary_operator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator_1")) return false;
    boolean r;
    r = consumeToken(b, PLUS_OPERATOR);
    if (!r) r = consumeToken(b, MINUS_OPERATOR);
    if (!r) r = consumeToken(b, BANG_OPERATOR);
    if (!r) r = consumeToken(b, TILDE_OPERATOR);
    return r;
  }

  // WS_KEY*
  private static boolean unary_operator_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, WS_KEY)) break;
      if (!empty_element_parsed_guard_(b, "unary_operator_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // DEF_KEYWORD |
  //              JSON_KEYWORD |
  //              XML_KEYWORD |
  //              XMLSTRING_KEYWORD
  public static boolean var_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VAR_TYPE, "<var type>");
    r = consumeToken(b, DEF_KEYWORD);
    if (!r) r = consumeToken(b, JSON_KEYWORD);
    if (!r) r = consumeToken(b, XML_KEYWORD);
    if (!r) r = consumeToken(b, XMLSTRING_KEYWORD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // WORD_KEY | TABLE_ROW
  public static boolean word(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "word")) return false;
    if (!nextTokenIs(b, "<word>", TABLE_ROW, WORD_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, WORD, "<word>");
    r = consumeToken(b, WORD_KEY);
    if (!r) r = consumeToken(b, TABLE_ROW);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
