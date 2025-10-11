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

%%

<YYINITIAL> {
  "#" {CHAR}* {LF}                             { return COMMENT_STMT; }
  "@" {CHAR}+ {LF}                             { return TAGS_KEY; }

  "Feature:"                                   { yybegin(WAITING_VALUE); return FEATURE_KEYWORD; }

  {LF}+                                        { return NEWLINE; }

}

<WAITING_VALUE> {
  "#".*                                      { /* ignore comment */ }
  {BOL}+"\"\"\""                             { yybegin(DOC_STRING_BLOCK); }

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
  {BOL}+ "#" {CHAR}*                          { return COMMENT_STMT; }

  "@" {CHAR}+                                 { return TAGS_KEY; }
  {WORD_NON_WS}+                              { return WORD_KEY; }
  {WS}                                        { return WS_KEY; }


  {BOL}+                                      { return NEWLINE; }
}

<DOC_STRING_BLOCK> {
    {BOL}+"\"\"\""                                  {
                  String text = this.stringBuffer.toString();
                  this.stringBuffer.setLength(0); // clear buffer
                  yybegin(WAITING_VALUE);
                  if (!text.isEmpty()) {
                      return DOC_STRING_KEY; // return accumulated doc string content
                  } else {
                      return WORD; // handle empty doc string
                  }
    }
    {CHAR}+ { this.stringBuffer.append(yytext()); }
    {WS}+ { this.stringBuffer.append(yytext()); }
    {LF}+ { this.stringBuffer.append(yytext()); }
}


[^]                                                         { return TokenType.BAD_CHARACTER; }