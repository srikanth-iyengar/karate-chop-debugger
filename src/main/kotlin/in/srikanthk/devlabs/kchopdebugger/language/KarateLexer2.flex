package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;

%%

%{
  public _KarateLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _KarateLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}                  { return WHITE_SPACE; }

  "NEWLINE"                      { return NEWLINE; }
  "FEATURE_KEYWORD"              { return FEATURE_KEYWORD; }
  "TAGS"                         { return TAGS; }
  "BACKGROUND_KEYWORD"           { return BACKGROUND_KEYWORD; }
  "SCENARIO_KEYWORD"             { return SCENARIO_KEYWORD; }
  "SCENARIO_OUTLINE_KEYWORD"     { return SCENARIO_OUTLINE_KEYWORD; }
  "TEXT"                         { return TEXT; }
  "EXAMPLES_KEYWORD"             { return EXAMPLES_KEYWORD; }
  "TABLE_ROW"                    { return TABLE_ROW; }
  "STAR_STEP"                    { return STAR_STEP; }
  "GIVEN_STEP"                   { return GIVEN_STEP; }
  "WHEN_STEP"                    { return WHEN_STEP; }
  "THEN_STEP"                    { return THEN_STEP; }
  "AND_STEP"                     { return AND_STEP; }
  "BUT_STEP"                     { return BUT_STEP; }
  "DOC_STRING"                   { return DOC_STRING; }


}

[^] { return BAD_CHARACTER; }
