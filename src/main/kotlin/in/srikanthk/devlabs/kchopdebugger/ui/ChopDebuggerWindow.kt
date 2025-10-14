package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.Side
import com.intellij.ui.components.JBTabbedPane
import com.intellij.util.ui.UIUtil
import com.intellij.xdebugger.ui.DebuggerColors
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.service.KarateExecutionService
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import java.awt.BorderLayout
import java.util.HashMap
import javax.swing.JPanel

class ChopDebuggerWindow(private val project: Project) : JPanel(BorderLayout()) {

    private val debugVarsPanel = DebugVariableTable(project)
    private val logViewPanel = LogViewPanel(project)
    private val breakpointsPanel = BreakpointEditorPanel(project)
    private val tabbedPane = JBTabbedPane()
    private val karatePropertiesPanel = PropertiesEditorPanel(project)
    private var state: DebuggerState = DebuggerState.Finished
    private val publisher: DebuggerInfoRequestTopic? = project.messageBus.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
    private val karateExecutionService = project.getService(KarateExecutionService::class.java)
    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("Karate Chop Debugger Notification")

    private val resumeAction = object : AnAction("Resume", "Resume Execution", AllIcons.Actions.Resume) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.resume()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }
    }

    private val stepOverAction = object : AnAction("Step Over", "Step Over", AllIcons.Actions.TraceInto) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.stepForward()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }
    }

    private val stopAction = object : AnAction("Stop", "Stop Execution", AllIcons.Actions.Suspend) {
        override fun actionPerformed(e: AnActionEvent) {
            karateExecutionService.stop()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state != DebuggerState.Finished
        }
    }

    private val rerunAction = object : AnAction("Rerun", "Rerun Execution", AllIcons.Actions.Restart) {
        override fun actionPerformed(e: AnActionEvent) {
            karateExecutionService.rerun()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Finished
        }
    }

    init {
        val actionGroup = DefaultActionGroup(rerunAction, resumeAction, stepOverAction, stopAction)
        val actionToolbar = ActionManager.getInstance().createActionToolbar(
            "KarateDebuggerToolbar", actionGroup, false
        ).apply {
            setTargetComponent(this@ChopDebuggerWindow)
        }

        val toolbarPanel = JPanel(BorderLayout()).apply {
            add(actionToolbar.component, BorderLayout.NORTH)
            border = IdeBorderFactory.createBorder()
        }

        tabbedPane.apply {
            addTab("Variables", debugVarsPanel)
            addTab("Logs", logViewPanel)
            addTab("Breakpoints", breakpointsPanel)
            addTab("Run Properties", karatePropertiesPanel)
        }

        val centerPanel = JPanel(BorderLayout()).apply {
            add(tabbedPane, BorderLayout.CENTER)
        }

        add(toolbarPanel, BorderLayout.WEST)
        add(centerPanel, BorderLayout.CENTER)

        project.messageBus.connect().subscribe(DebuggerInfoResponseTopic.TOPIC, object : DebuggerInfoResponseTopic {
            override fun updateKarateVariables(vars: HashMap<String, Map<String, Object>>) {}
            override fun updateState(state: DebuggerState) {
                WriteCommandAction.runWriteCommandAction(project) {
                    updateDebuggerState(state)
                }
            }

            override fun navigateTo(filepath: String, lineNumber: Int) {
                WriteCommandAction.runWriteCommandAction(project) {
                    focusTo(filepath, lineNumber)
                }
            }

            override fun evaluateExpressionResult(result: String, error: String) {
            }

            override fun appendLog(log: String, isSuccess: Boolean) {}
        })
    }

    private fun updateDebuggerState(newState: DebuggerState) {
        state = newState

        if (state == DebuggerState.Finished) {
            karateExecutionService.lastExecutedFileName?.let  {
                cleanupMarkups(it)
            }
        }

        if (state == DebuggerState.Started) {
            ToolWindowManager.getInstance(project).getToolWindow("Karate Chop Debugger")?.show()
            this.tabbedPane.selectedIndex = 0
        }
    }

    private fun cleanupMarkups(filepath: String) {
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(filepath)
        virtualFile?.let {
            val descriptor = OpenFileDescriptor(project, virtualFile)
            val editor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true) ?: return
            val markupModel = editor.markupModel
            markupModel.removeAllHighlighters()
        }
    }

    private fun focusTo(filePath: String, lineNumber: Int) {
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath)

        if (virtualFile != null) {
            val descriptor = OpenFileDescriptor(project, virtualFile, lineNumber - 1, 0)
            val editor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true) ?: return
            val markupModel = editor.markupModel

            val startOffset = editor.document.getLineStartOffset(lineNumber - 1)
            val endOffset = editor.document.getLineEndOffset(lineNumber - 1)

            val attributes =
                EditorColorsManager.getInstance().globalScheme.getAttributes(DebuggerColors.EXECUTIONPOINT_ATTRIBUTES)

            markupModel.removeAllHighlighters()
            markupModel.addRangeHighlighter(
                startOffset,
                endOffset,
                HighlighterLayer.SELECTION - 1,
                attributes,
                HighlighterTargetArea.LINES_IN_RANGE
            )

            descriptor.navigate(true)
        } else {
            notificationGroup
                .createNotification(
                    "External file detected",
                    "The debugger stepped into a source file located outside the current project.",
                    NotificationType.ERROR
                ).notify(project)
        }
    }
}
