package `in`.srikanthk.devlabs.kchopdebugger.listener

import com.intellij.openapi.project.Project
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebugMessageBus
import `in`.srikanthk.devlabs.kchopdebugger.agent.topic.DebugRequest
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import java.nio.file.Files

class KarateBreakpointListener(private val project: Project) : XBreakpointListener<XBreakpoint<*>> {
    val remotePublisher = project.messageBus.syncPublisher(DebuggerInfoRequestTopic.TOPIC)

    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        breakpoint.sourcePosition?.let {
            remotePublisher.addBreakpoint(it.file.path, it.line +1)
        }
    }

    override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
        breakpoint.sourcePosition?.let {
            remotePublisher.removeBreakpoint(it.file.path, it.line +1)
        }
    }
}