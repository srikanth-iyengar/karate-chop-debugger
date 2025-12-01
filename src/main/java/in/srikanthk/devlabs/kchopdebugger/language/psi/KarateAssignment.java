// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface KarateAssignment extends PsiElement {

  @Nullable
  KarateExpression getExpression();

  @NotNull
  KarateIdentifier getIdentifier();

  @Nullable
  KarateVarType getVarType();

}
