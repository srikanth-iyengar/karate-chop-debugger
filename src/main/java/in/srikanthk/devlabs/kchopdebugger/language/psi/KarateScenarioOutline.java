// This is a generated file. Not intended for manual editing.
package in.srikanthk.devlabs.kchopdebugger.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface KarateScenarioOutline extends PsiElement {

  @NotNull
  List<KarateComment> getCommentList();

  @Nullable
  KarateDescription getDescription();

  @NotNull
  KarateExamples getExamples();

  @NotNull
  List<KarateStep> getStepList();

  @NotNull
  List<KarateTags> getTagsList();

}
