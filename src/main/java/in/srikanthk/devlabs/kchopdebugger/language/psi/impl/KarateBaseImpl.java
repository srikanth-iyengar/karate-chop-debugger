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

public class KarateBaseImpl extends ASTWrapperPsiElement implements KarateBase {

  public KarateBaseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull KarateVisitor visitor) {
    visitor.visitBase(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof KarateVisitor) accept((KarateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public KarateAssignment getAssignment() {
    return findChildByClass(KarateAssignment.class);
  }

  @Override
  @Nullable
  public KarateExpression getExpression() {
    return findChildByClass(KarateExpression.class);
  }

  @Override
  @Nullable
  public KarateIdentifier getIdentifier() {
    return findChildByClass(KarateIdentifier.class);
  }

  @Override
  @Nullable
  public KarateLiterals getLiterals() {
    return findChildByClass(KarateLiterals.class);
  }

}
