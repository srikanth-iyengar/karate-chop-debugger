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

public class KarateJsonArrayImpl extends ASTWrapperPsiElement implements KarateJsonArray {

  public KarateJsonArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull KarateVisitor visitor) {
    visitor.visitJsonArray(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof KarateVisitor) accept((KarateVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<KarateJsonArray> getJsonArrayList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, KarateJsonArray.class);
  }

  @Override
  @NotNull
  public List<KarateJsonObject> getJsonObjectList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, KarateJsonObject.class);
  }

}
