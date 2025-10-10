package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class KarateFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, KarateLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return KarateLanguageFileType.INSTANCE
    }
}