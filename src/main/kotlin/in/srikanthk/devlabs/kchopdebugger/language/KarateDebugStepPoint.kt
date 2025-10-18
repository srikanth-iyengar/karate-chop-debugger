package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpointType

class KarateDebugStepPoint: XLineBreakpointType<XBreakpointProperties<*>>("karate-dsl", "Karate DSL") {
    override fun createBreakpointProperties(
        p0: VirtualFile,
        p1: Int
    ): XBreakpointProperties<*>? {
        return null
    }

    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        return true
    }
}