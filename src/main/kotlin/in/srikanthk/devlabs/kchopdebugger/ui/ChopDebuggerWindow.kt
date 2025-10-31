package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBTabbedPane
import com.intellij.xdebugger.ui.DebuggerColors
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.service.KarateExecutionService
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import java.awt.BorderLayout
import javax.swing.JPanel

class ChopDebuggerWindow(private val project: Project) : JPanel(BorderLayout()) {

    private val debugVarsPanel = DebugVariableTable(project)
    private val logViewPanel = LogViewPanel(project)
    private val tabbedPane = JBTabbedPane()
    private val karatePropertiesPanel = PropertiesEditorPanel(project)
    private var state: DebuggerState = DebuggerState.Finished
    private val publisher: DebuggerInfoRequestTopic? = project.messageBus.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
    private val karateExecutionService = project.getService(KarateExecutionService::class.java)
    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("Karate Chop Debugger Notification")

    private val resumeAction = object : AnAction("Resume", "Resume execution", AllIcons.Actions.Resume) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.resume()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val stepIntoAction = object : AnAction("Step Into", "Step into", AllIcons.Actions.TraceInto) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.stepForward()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val stepOverAction = object : AnAction("Step Over", "Step over", AllIcons.Actions.TraceOver) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.stepOver()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val stopAction = object : AnAction("Stop", "Stop execution", AllIcons.Actions.Suspend) {
        override fun actionPerformed(e: AnActionEvent) {
            karateExecutionService.stop()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state != DebuggerState.Finished
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val rerunAction = object : AnAction("Rerun", "Rerun execution", AllIcons.Actions.RestartDebugger) {
        override fun actionPerformed(e: AnActionEvent) {
            karateExecutionService.rerun()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Finished
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val stepBackAction = object : AnAction("Step Back", "Step back", AllIcons.Actions.Back) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.stepBack()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val stepOutAction = object : AnAction("Step Out", "Step out", AllIcons.Actions.StepOut) {
        override fun actionPerformed(e: AnActionEvent) {
            publisher?.stepOut()
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    private val toggleSkipBreakpointAction =
        object : ToggleAction("Mute Breakpoints", "Mute breakpoints", AllIcons.Debugger.MuteBreakpoints) {
            var skipBreakpoints = false
            override fun isSelected(p0: AnActionEvent): Boolean {
                return skipBreakpoints
            }

            override fun setSelected(p0: AnActionEvent, p1: Boolean) {
                skipBreakpoints = p1
                publisher?.setShouldSkipBreakpoints(skipBreakpoints)
            }

            override fun getActionUpdateThread(): ActionUpdateThread {
                return ActionUpdateThread.BGT
            }
        }

    private val hotReload = object : AnAction(
        "Hot Reload Scenario",
        "Hot reloads current scenario in execution",
        AllIcons.Actions.BuildLoadChanges
    ) {
        override fun actionPerformed(e: AnActionEvent) {
            karateExecutionService.hotReload {
                publisher?.hotReload()
                WriteCommandAction.runWriteCommandAction(project) {
                    ToolWindowManager.getInstance(project).getToolWindow("Karate Chop Debugger")?.show()
                    tabbedPane.selectedIndex = 0
                }

                notificationGroup.createNotification(
                    "Hot reload completed",
                    "Current scenario execution reloaded, no new steps will be added for execution",
                    NotificationType.INFORMATION
                ).notify(project)
            }
        }

        override fun update(e: AnActionEvent) {
            e.presentation.isEnabled = state == DebuggerState.Halted
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
            return ActionUpdateThread.BGT
        }
    }

    init {
        val actionGroup = DefaultActionGroup(
            rerunAction, stopAction, hotReload, Separator.create(),
            resumeAction, stepOverAction, stepIntoAction, stepOutAction, stepBackAction, Separator.create(),
            toggleSkipBreakpointAction
        )
        val actionToolbar = ActionManager.getInstance().createActionToolbar(
            "KarateDebuggerToolbar", actionGroup, false
        ).apply {
            targetComponent = this@ChopDebuggerWindow
        }

        val toolbarPanel = JPanel(BorderLayout()).apply {
            add(actionToolbar.component, BorderLayout.NORTH)
            border = IdeBorderFactory.createBorder()
        }

        tabbedPane.apply {
            addTab("Variables", debugVarsPanel)
            addTab("Logs", logViewPanel)
            addTab("VM options", karatePropertiesPanel)
        }

        val centerPanel = JPanel(BorderLayout()).apply {
            add(tabbedPane, BorderLayout.CENTER)
        }

        add(toolbarPanel, BorderLayout.WEST)
        add(centerPanel, BorderLayout.CENTER)

        project.messageBus.connect().subscribe(DebuggerInfoResponseTopic.TOPIC, object : DebuggerInfoResponseTopic {
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
        })
    }

    private fun updateDebuggerState(newState: DebuggerState) {
        state = newState

        if (state == DebuggerState.Finished) {
            karateExecutionService.lastExecutedFileName?.let {
                cleanupMarkups(it)
            }
        }

        if (state == DebuggerState.Started) {
            ToolWindowManager.getInstance(project).getToolWindow("Karate Chop Debugger")?.show()
            this.tabbedPane.selectedIndex = 1
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
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        editor?.let {
            editor.virtualFile?.let { file -> cleanupMarkups(file.path) }
        }
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
            notificationGroup.createNotification(
                "External file detected",
                "The debugger stepped into a source file located outside the current project.",
                NotificationType.ERROR
            ).notify(project)
        }
    }
}
