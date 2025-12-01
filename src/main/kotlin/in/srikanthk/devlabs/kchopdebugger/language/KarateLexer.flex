package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;
import com.intellij.psi.TokenType;

%%

%{
    private StringBuilder stringBuffer = new StringBuilder();
    public KarateLexer() {
      this((java.io.Reader) null);
    }
%}

%public
%class KarateLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%state MAIN

// --- Regex fragments ---
WS = [ \t]
LF = \r|\n|\r\n
BOL = {LF}+{WS}*
CHAR = [^\r\n]
WORD_NON_WS = [^ \t\r\n]

%state WAITING_VALUE
%state DOC_STRING_BLOCK
%state SCENARIO
%state STEP
%state TABLE

%%

<YYINITIAL> {
  "#" {CHAR}* {LF}                             { return COMMENT_STMT; }
  "@" {CHAR}+ {LF}                             { return TAGS_KEY; }

  "Feature:"                                   { yybegin(WAITING_VALUE); return FEATURE_KEYWORD; }

  {LF}+                                        { return NEWLINE; }

}

<WAITING_VALUE> {
  "#".*                                      { /* ignore comment */ }
  {BOL}+"\"\"\""                             { yybegin(DOC_STRING_BLOCK); return DOC_STRING_START; }

  "Background:"                              { yybegin(SCENARIO); return BACKGROUND_KEYWORD; }
  "Scenario:"                                { yybegin(SCENARIO); return SCENARIO_KEYWORD; }
  "Scenario Outline:"                        { yybegin(SCENARIO); return SCENARIO_OUTLINE_KEYWORD; }

  {BOL}+ "#" .*                               { return COMMENT_STMT; }
  {WS}                                        { return WS_KEY; }
  {WORD_NON_WS}+                              { return WORD_KEY; }
  {BOL}+                                      { return NEWLINE; }
}

<SCENARIO> {
  "*"                                         { yybegin(STEP); return STAR_STEP; }
  "Given"                                     { yybegin(STEP); return GIVEN_STEP; }
  "When"                                      { yybegin(STEP); return WHEN_STEP; }
  "Then"                                      { yybegin(STEP); return THEN_STEP; }
  "And"                                       { yybegin(STEP); return AND_STEP; }
  "But"                                       { yybegin(STEP); return BUT_STEP; }
  "Background:"                               { return BACKGROUND_KEYWORD; }
  "Scenario:"                                 { return SCENARIO_KEYWORD; }
  "Scenario Outline:"                         { return SCENARIO_OUTLINE_KEYWORD; }
  "Examples:"                                 { return EXAMPLES_KEYWORD; }

  "|" {CHAR}+                                 { return TABLE_ROW; }
  "\"\"\""                                    { yybegin(DOC_STRING_BLOCK); return DOC_STRING_START; }
  {BOL}*"#" .*                                { return COMMENT_STMT; }
  {WS}                                        { return WS_KEY; }
  {WORD_NON_WS}+                              { return WORD_KEY; }
  {BOL}+                                      { return NEWLINE; }
}

<STEP> {
    // OPERATORS
    "+"                                      { return PLUS_OPERATOR; }
    "-"                                      { return MINUS_OPERATOR; }
    "*"                                      { return MULTIPLY_OPERATOR; }
    "/"                                      { return DIVIDE_OPERATOR; }
    "%"                                      { return MODULO_OPERATOR; }
    "=="                                      { return EQUALS_OPERATOR; }
    "!="                                     { return NOT_EQUALS_OPERATOR; }
    "<"                                      { return LESS_THAN_OPERATOR; }
    ">"                                      { return GREATER_THAN_OPERATOR; }
    "<="                                     { return LESS_THAN_EQUAL_OPERATOR; }
    ">="                                     { return GREATER_THAN_EQUAL_OPERATOR; }
    "||"                                     { return LOGICAL_OR_OPERATOR; }
    "&&"                                     { return LOGICAL_AND_OPERATOR; }
    "^"                                      { return LOGICAL_XOR_OPERATOR; }
    "?"                                      { return TERNARY_OPERATOR; }
    ":"                                      { return COLON_OPERATOR; }
    "="                                      { return ASSIGNMENT_OPERATOR; }
    "."                                      { return DOT_OPERATOR; }
    "=>"                                     { return ARROW_OPERATOR; }

    "|"                                     { return PIPE_OPERATOR; }
    "&"                                     { return AMPERSAND_OPERATOR; }
    "!"                                     { return BANG_OPERATOR; }
    "~"                                     { return TILDE_OPERATOR; }

    // KEYWORDS
    "karate"                                { return KARATE_KEYWORD; }
    "call"                                  { return CALL_KEYWORD; }
    "callonce"                              { return CALLONCE_KEYWORD; }
    "read"                                  { return READ_KEYWORD; }
    "print"                                 { return PRINT_KEYWORD; }
    "match"                                 { return MATCH_KEYWORD; }
    "set"                                   { return SET_KEYWORD; }
    "table"                                 { return TABLE_KEYWORD; }
    "driver"                                { return DRIVER_KEYWORD; }
    "configure"                             { return CONFIGURE_KEYWORD; }
    "function"                              { return FUNCTION_KEYWORD; }
    "return"                                { return RETURN_KEYWORD; }
    "if"                                    { return IF_KEYWORD; }
    "else"                                  { return ELSE_KEYWORD; }

    "xmlstring"                             { return XMLSTRING_KEYWORD; }
    "def"                                   { return DEF_KEYWORD; }
    "json"                                  { return JSON_KEYWORD; }
    "xml"                                   { return XML_KEYWORD; }

    // STRING LITERALS
    "'" (\\.|[^\\'\r\n])* "'"               { return STRING_LITERAL; }
    "\"" (\\.|[^\\\"\r\n])* "\""            { return STRING_LITERAL; }
    "`"(\\.|[^\\`\r\n])*"`"                  { return STRING_LITERAL; }

    // NUMERIC LITERALS
    [0-9]+ ("." [0-9]+)?                    { return NUMBER_LITERAL; }

    "null"                                  { return NULL_LITERAL; }
    "true"|"false"                          { return BOOLEAN_LITERAL; }
    [a-zA-Z_][a-zA-Z0-9_]*                  { return IDENTIFIER_KEYWORD; }

    "("                                      { return L_PAREN; }
    ")"                                      { return R_PAREN; }
    "{"                                      { return L_CURLY; }
    "}"                                      { return R_CURLY; }
    "["                                      { return L_SQUARE; }
    "]"                                      { return R_SQUARE; }
    {WS}*","{WS}*                                      { return COMMA; }

    "#"                                      { return OCTOTHORPE; }
    {WS}+                                   { return WS_KEY; }
    {BOL}+                                   { yybegin(SCENARIO); return NEWLINE; }
}

<DOC_STRING_BLOCK> {
    {BOL}+"\"\"\""                                  {
                  yybegin(SCENARIO);
                  return DOC_STRING_END;
    }
    [^]           { return DOC_STRING_CONTENT; }
}

[^]                                                         { return TokenType.BAD_CHARACTER; }