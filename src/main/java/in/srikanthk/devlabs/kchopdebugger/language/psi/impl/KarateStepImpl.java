// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static in.srikanthk.devlabs.kchopdebugger.language.KarateTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import in.srikanthk.devlabs.kchopdebugger.language.psi.*;

public class KarateStepImpl extends ASTWrapperPsiElement implements KarateStep {

  public KarateStepImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull KarateVisitor visitor) {
    visitor.visitStep(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof KarateVisitor) accept((KarateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public KarateDocString getDocString() {
    return findChildByClass(KarateDocString.class);
  }

  @Override
  @NotNull
  public KarateLine getLine() {
    return findNotNullChildByClass(KarateLine.class);
  }

  @Override
  @NotNull
  public KaratePrefix getPrefix() {
    return findNotNullChildByClass(KaratePrefix.class);
  }

  @Override
  @Nullable
  public KarateTable getTable() {
    return findChildByClass(KarateTable.class);
  }

}
