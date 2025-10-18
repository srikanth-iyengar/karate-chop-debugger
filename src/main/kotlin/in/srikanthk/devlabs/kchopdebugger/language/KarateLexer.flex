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
  {BOL}+"\"\"\""                             { yybegin(DOC_STRING_BLOCK); return DOC_STRING_START; }

  "|" {CHAR}+                                 { return TABLE_ROW; }
  "Background:"                        { return BACKGROUND_KEYWORD; }
  "Scenario:"                          { return SCENARIO_KEYWORD; }
  "Scenario Outline:"                  { return SCENARIO_OUTLINE_KEYWORD; }
  {BOL}+ "Examples:"                          { return EXAMPLES_KEYWORD; }

  "*"                                  { return STAR_STEP; }
  "Given"                              { return GIVEN_STEP; }
  "When"                               { return WHEN_STEP; }
  "Then"                               { return THEN_STEP; }
  "And"                                { return AND_STEP; }
  "But"                                { return BUT_STEP; }
  {BOL}+ "#" {CHAR}*                          { return COMMENT_STMT; }

  "@" {CHAR}+                                 { return TAGS_KEY; }
  {WORD_NON_WS}+                              { return WORD_KEY; }
  {WS}                                        { return WS_KEY; }


  {BOL}+                                      { return NEWLINE; }
}

<DOC_STRING_BLOCK> {
    {BOL}+"\"\"\""                                  {
                  yybegin(WAITING_VALUE);
                  return DOC_STRING_END;
    }
    [^]           { return DOC_STRING_CONTENT; }
}


[^]                                                         { return TokenType.BAD_CHARACTER; }