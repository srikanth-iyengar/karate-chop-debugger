// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language.psi.impl;

import java.util.List;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import in.srikanthk.devlabs.kchopdebugger.language.KarateTypes;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import in.srikanthk.devlabs.kchopdebugger.language.psi.*;

public class KarateDocContentImpl extends ASTWrapperPsiElement implements KarateDocContent, PsiLanguageInjectionHost {

    public KarateDocContentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull KarateVisitor visitor) {
        visitor.visitDocContent(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof KarateVisitor) accept((KarateVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    public boolean isValidHost() {
        return true;
    }

    @Override
    public PsiLanguageInjectionHost updateText(@NotNull String text) {
        // Replace the TEXT node(s) inside this doc string
        ASTNode firstTextNode = getNode().findChildByType(DOC_STRING_CONTENT);
        if (firstTextNode != null) {
            firstTextNode.getPsi().replace(new LeafPsiElement(DOC_STRING_CONTENT, text));
        }
        return this;
    }

    @Override
    public @NotNull LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return new LiteralTextEscaper<PsiLanguageInjectionHost>(this) {

            @Override
            public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
                outChars.append(getText().substring(rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset()));
                return true;
            }

            @Override
            public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost) {
                return offsetInDecoded;
            }

            @Override
            public @NotNull TextRange getRelevantTextRange() {
                return new TextRange(0, getTextLength());
            }

            @Override
            public boolean isOneLine() {
                return false;
            }
        };
    }
}
