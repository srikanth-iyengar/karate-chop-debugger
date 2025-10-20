package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.intellij.icons.AllIcons
import com.intellij.json.JsonFileType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.ui.EditorTextField
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.table.JBTable
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.table.DefaultTableModel

class DebugVariableTable(private val project: Project) : JPanel(BorderLayout()) {
    private val tableModel = object : DefaultTableModel(arrayOf("Variable", "Type", "Value"), 0) {
        override fun isCellEditable(row: Int, column: Int): Boolean = false
    }
    private val table = JBTable(tableModel)
    private val publisher = project.messageBus.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
    private var jsonResultString = ""

    // --- Expression Field with Dynamic Icon ---
    private val expressionField = JBTextField().apply {
        emptyText.text = "Prefix with * for Karate expression; without * for Karate JS"
    }
    private val iconLabel = JLabel(AllIcons.FileTypes.JavaScript).apply {
        border = BorderFactory.createEmptyBorder(0, 4, 0, 4)
        toolTipText = "Karate JavaScript Expression"
    }
    private val expressionPanel = JPanel(BorderLayout()).apply {
        border = BorderFactory.createLineBorder(JBColor.border())
        add(iconLabel, BorderLayout.WEST)
        add(expressionField, BorderLayout.CENTER)
        preferredSize = Dimension(300, 28)
    }

    private val resultField =
        EditorTextField("Evaluation result will appear here", project, JsonFileType.INSTANCE).apply {
            isViewer = true
            isFocusable = false
            setOneLineMode(true)
        }

    init {
        val evalPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(5, 5, 5, 5)

            add(expressionPanel)
            add(Box.createVerticalStrut(4))
            add(resultField)
        }

        add(evalPanel, BorderLayout.NORTH)
        add(JBScrollPane(table), BorderLayout.CENTER)

        val inputMap = expressionField.getInputMap(JComponent.WHEN_FOCUSED)
        val actionMap = expressionField.actionMap
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "evaluateExpression")
        actionMap.put("evaluateExpression", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                val expr = expressionField.text
                if (expr.isNotEmpty()) {
                    publisher.evaluateExpression(expr)
                }
            }
        })

        val messageBus = project.messageBus.connect()
        messageBus.subscribe(DebuggerInfoResponseTopic.TOPIC, object : DebuggerInfoResponseTopic {
            override fun updateKarateVariables(vars: HashMap<String, Map<String, Object>>) {
                WriteCommandAction.runWriteCommandAction(project) {
                    val shouldResize = tableModel.rowCount == 0
                    tableModel.setNumRows(0)
                    vars.entries.forEach {
                        tableModel.addRow(arrayOf(it.key, it.value["type"], it.value["value"]))
                    }
                    if (shouldResize) table.doLayout()
                }
            }

            override fun updateState(state: DebuggerState) {
                WriteCommandAction.runWriteCommandAction(project) {
                    if (state == DebuggerState.Finished) {
                        tableModel.setNumRows(0)
                        resultField.text = "Evaluation result will appear here"
                        jsonResultString = ""
                    }
                }
            }

            override fun evaluateExpressionResult(result: String, error: String) {
                WriteCommandAction.runWriteCommandAction(project) {
                    if (error.isNotEmpty()) {
                        resultField.text = "[Error] $error"
                        resultField.toolTipText = null
                        resultField.cursor = Cursor.getDefaultCursor()
                        jsonResultString = ""
                    } else {
                        if (result.isNotEmpty()) {
                            jsonResultString = result
                            resultField.text =
                                if (result.length > 100) result.take(100) + "..." else result
                            resultField.toolTipText = "Click to expand"
                            resultField.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        } else {
                            resultField.text = "[null]"
                            jsonResultString = ""
                        }
                    }
                }
            }

            override fun appendLog(log: String, isSuccess: Boolean) {}
            override fun navigateTo(filepath: String, lineNumber: Int) {}
        })

        resultField.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent?) {
                if (jsonResultString.isNotBlank()) showJsonPopup(project, jsonResultString)
            }
        })

        // --- Add document listener to change icon dynamically ---
        expressionField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) = updateIcon()
            override fun removeUpdate(e: DocumentEvent?) = updateIcon()
            override fun changedUpdate(e: DocumentEvent?) = updateIcon()

            private fun updateIcon() {
                val text = expressionField.text.trim()
                if (text.startsWith("*")) {
                    iconLabel.icon = AllIcons.Nodes.Function
                    iconLabel.toolTipText = "Karate Expression (prefixed with *)"
                } else {
                    iconLabel.icon = AllIcons.FileTypes.JavaScript
                    iconLabel.toolTipText = "Karate JavaScript Expression"
                }
            }
        })
    }

    private fun showJsonPopup(project: Project, json: String) {
        val editorFactory = EditorFactory.getInstance()
        val prettyJson = try {
            val jsonNode = JsonParser.parseString(json)
            GsonBuilder().setPrettyPrinting().create().toJson(jsonNode)
        } catch (e: Exception) {
            json
        }
        val document = editorFactory.createDocument(prettyJson)
        val editor = editorFactory.createEditor(document, project, JsonFileType.INSTANCE, true)
        val popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(editor.component, editor.contentComponent)
            .setTitle("Evaluated Result")
            .setResizable(true)
            .setMovable(true)
            .setRequestFocus(true)
            .setCancelOnWindowDeactivation(true)
            .setCancelOnClickOutside(true)
            .setDimensionServiceKey(project, "KarateChopDebugger.JsonPopup", false)
            .addListener(object : JBPopupListener {
                override fun onClosed(event: LightweightWindowEvent) {
                    editorFactory.releaseEditor(editor)
                }
            })
            .createPopup()
        popup.showInFocusCenter()
    }
}
