package in.srikanthk.devlabs.kchopdebugger.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;

import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;

%%

%class KarateLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%ignorecase

%eof{
    return;
%eof}

/* ---------------------- Regular Expressions ---------------------- */

EOL             = \r|\n|\r\n
WS              = [ \t\f]+

FEATURE         = "Feature:"
BACKGROUND      = "Background:"
SCENARIO        = "Scenario:"
SCENARIO_OUTLINE= "Scenario Outline:"
EXAMPLES        = "Examples:"
TAG             = \@[A-Za-z0-9_\-]+
GIVEN           = "Given"
WHEN            = "When"
THEN            = "Then"
AND             = "And"
BUT             = "But"


DOCSTRING_START = "\"\"\""
TABLE_CELL      = \| [^\r\n]*

TEXT            = [^ \t\r\n#\|@\"]([^\r\n]*)?

%%

<YYINITIAL>{

    {FEATURE}               { return FEATURE_KEY; }
    {BACKGROUND}            { return BACKGROUND_KEY; }
    {SCENARIO_OUTLINE}      { return SCENARIO_OUTLINE_KEY; }
    {SCENARIO}              { return SCENARIO_KEY; }
    {EXAMPLES}              { return EXAMPLES_KEY; }

    {GIVEN}                 { return GIVEN; }
    {WHEN}                  { return WHEN; }
    {THEN}                  { return THEN; }
    {AND}                   { return AND; }
    {BUT}                   { return BUT; }

    {TAG}                   { return TAGS_KEY; }

    {DOCSTRING_START}       { return DOC_STRING_KEY; }
    {TABLE_CELL}            { return TABLE_ROW; }

    {EOL}                   { return NEWLINE; }
    {WS}                    { return TokenType.WHITE_SPACE; }

    .                       { return TokenType.BAD_CHARACTER; }
}
