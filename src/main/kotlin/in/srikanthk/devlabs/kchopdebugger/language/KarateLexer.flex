package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;

%%

%{
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

%state WAITING_VALUE
%state DOC_STRING_BLOCK

%%

<YYINITIAL> {
  "#" {CHAR}* {LF}                             { return COMMENT_STMT; }
  "@" {CHAR}+ {LF}                             { return TAGS_KEY; }

  "Feature:"                                   { yybegin(WAITING_VALUE); return FEATURE_KEYWORD; }

  {LF}+                                        { return NEWLINE; }

}

<WAITING_VALUE> {
  "#".*                                      { /* ignore comment */ }
  {BOL}+"\"\"\""                             { yybegin(DOC_STRING_BLOCK); return DOC_STRING_KEY; }

  "|" {CHAR}+                                 { return TABLE_ROW; }
  {BOL}+ "Background:"                        { return BACKGROUND_KEYWORD; }
  {BOL}+ "Scenario:"                          { return SCENARIO_KEYWORD; }
  {BOL}+ "Scenario Outline:"                  { return SCENARIO_OUTLINE_KEYWORD; }
  {BOL}+ "Examples:"                          { return EXAMPLES_KEYWORD; }

  {BOL}+ "*"                                  { return STAR_STEP; }
  {BOL}+ "Given"                              { return GIVEN_STEP; }
  {BOL}+ "When"                               { return WHEN_STEP; }
  {BOL}+ "Then"                               { return THEN_STEP; }
  {BOL}+ "And"                                { return AND_STEP; }
  {BOL}+ "But"                                { return BUT_STEP; }
  {BOL}+ "#" {CHAR}*                          {  }

  "@" {CHAR}+                                 { return TAGS_KEY; }
  {CHAR}+                                     { return TEXT; }

  {BOL}+                                      { return NEWLINE; }
}

<DOC_STRING_BLOCK> {
    {BOL}+"\"\"\""                                  { yybegin(WAITING_VALUE); return DOC_STRING_KEY; }
    {CHAR}+ { return TEXT; }
    {WS}+ {return TEXT;}
    {LF}+ {return TEXT;}
}
