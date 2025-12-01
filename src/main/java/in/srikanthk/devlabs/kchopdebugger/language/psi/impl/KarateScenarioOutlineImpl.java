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

public class KarateScenarioOutlineImpl extends ASTWrapperPsiElement implements KarateScenarioOutline {

  public KarateScenarioOutlineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull KarateVisitor visitor) {
    visitor.visitScenarioOutline(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof KarateVisitor) accept((KarateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<KarateComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, KarateComment.class);
  }

  @Override
  @Nullable
  public KarateDescription getDescription() {
    return findChildByClass(KarateDescription.class);
  }

  @Override
  @NotNull
  public KarateExamples getExamples() {
    return findNotNullChildByClass(KarateExamples.class);
  }

  @Override
  @NotNull
  public List<KarateStep> getStepList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, KarateStep.class);
  }

  @Override
  @NotNull
  public List<KarateTags> getTagsList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, KarateTags.class);
  }

}
