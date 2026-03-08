package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.tree.TokenSet

class KarateCodeFoldingBuilder : FoldingBuilder, DumbAware {
    var BLOCKS_TO_FOLD = TokenSet.create(KarateTypes.SCENARIO, KarateTypes.SCENARIO_OUTLINE, KarateTypes.BACKGROUND)

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false;
    }

    override fun buildFoldRegions(
        p0: ASTNode,
        p1: Document
    ): Array<out FoldingDescriptor?> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        appendDescriptors(p0, descriptors)
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{...}"
    }

    fun appendDescriptors(node: ASTNode, descriptor: MutableList<FoldingDescriptor>) {
        if(BLOCKS_TO_FOLD.contains(node.elementType)) {
            descriptor.add(FoldingDescriptor(node, TextRange(node.textRange.startOffset, node.textRange.endOffset)))
        }

        var child = node.firstChildNode
        while(child != null) {
            appendDescriptors(child, descriptor)
            child = child.treeNext
        }
    }
}
