package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.icons.AllIcons
import com.intellij.json.JsonFileType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.ui.EditorTextField
import com.intellij.ui.JBColor
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.TextFieldWithAutoCompletionListProvider
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import `in`.srikanthk.devlabs.kchopdebugger.agent.DebuggerState
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoRequestTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.DebuggerInfoResponseTopic
import `in`.srikanthk.devlabs.kchopdebugger.topic.UIActionTopic
import `in`.srikanthk.devlabs.kchopdebugger.utils.JS_KEYWORDS
import `in`.srikanthk.devlabs.kchopdebugger.utils.KARATE_KEYWORDS
import `in`.srikanthk.devlabs.kchopdebugger.utils.Trie
import `in`.srikanthk.devlabs.kchopdebugger.utils.TrieResult
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.table.DefaultTableModel

enum class ExpressionType {
    KARATE,
    JAVASCRIPT
}

enum class ResultType {
    KEYWORD,
    VARIABLE
}

class DebugVariableTable(private val project: Project) : JPanel(BorderLayout()) {
    private val tableModel = object : DefaultTableModel(arrayOf("Variable", "Type", "Value"), 0) {
        override fun isCellEditable(row: Int, column: Int): Boolean = false
    }
    private val table = JBTable(tableModel)
    private val publisher = project.messageBus.syncPublisher(DebuggerInfoRequestTopic.TOPIC)
    private var jsonResultString = ""
    private var trie = Trie<ResultType>()
    private var expressionType: ExpressionType = ExpressionType.JAVASCRIPT

    private val completionProvider = object : TextFieldWithAutoCompletionListProvider<TrieResult<ResultType>>(emptyList()) {
        override fun getLookupString(item: TrieResult<ResultType>): String = item.word

        override fun getItems(
            searchStr: String?,
            cached: Boolean,
            parameters: CompletionParameters?
        ): Collection<TrieResult<ResultType>?> {
            return trie.searchWord(searchStr ?: "", "")
        }

        override fun getIcon(item: TrieResult<ResultType>): Icon {
            return when(item.data) {
                ResultType.KEYWORD -> AllIcons.Nodes.Favorite
                ResultType.VARIABLE -> AllIcons.Nodes.Variable
            }
        }

        override fun getPrefix(text: String, offset: Int): String {
            if (offset == 0) return ""
            val stopChars = setOf(
                ' ', '\t', '\n', '\r',     // whitespace
                ',', ';', ':',        // separators
                '(', ')', '{', '}', '[', ']', // brackets
                '+', '-', '*', '/', '%', '=', '<', '>', '!', '&', '|', '?', // operators
                '"', '\'', '`',            // string delimiters
                '@', '#', '$',             // meta/special symbols
                '\\'                       // escape
            )

            var start = offset - 1
            while (start >= 0 && text[start] !in stopChars) {
                start--
            }

            return text.substring(start + 1, offset)
        }
    }

    private val expressionField = TextFieldWithAutoCompletion(project, completionProvider, true, "").apply {
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
    }
    private val iconLabel = JLabel(AllIcons.FileTypes.JavaScript).apply {
        toolTipText = "Karate JavaScript Expression"
        border = JBUI.Borders.emptyLeft(5)
    }
    private val expressionPanel = JPanel(BorderLayout()).apply {
        add(iconLabel, BorderLayout.WEST)
        add(expressionField, BorderLayout.CENTER)
        preferredSize = Dimension(300, 28)
        border = BorderFactory.createLineBorder(JBColor.border())
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

            add(expressionPanel)
            add(Box.createVerticalStrut(2))
            add(resultField)
        }

        add(evalPanel, BorderLayout.NORTH)
        add(JBScrollPane(table), BorderLayout.CENTER)

        val inputMap = expressionField.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
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
                    updateTrieKeywords()
                    vars.entries.forEach {
                        tableModel.addRow(arrayOf(it.key, it.value["type"], it.value["value"]))
                        trie.addWord(it.key, ResultType.VARIABLE)
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
                    if (state == DebuggerState.Halted) {
                        table.isEnabled = true
                        evalPanel.isEnabled = true
                    } else {
                        table.isEnabled = true
                        tableModel.setNumRows(0)
                        evalPanel.isEnabled = false
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
        messageBus.subscribe(UIActionTopic.TOPIC, object : UIActionTopic {
            override fun updateExprText(text: String) {
                expressionField.text = text
            }
        })

        resultField.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent?) {
                if (jsonResultString.isNotBlank()) showJsonPopup(project, jsonResultString)
            }
        })

        expressionField.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                updateIcon()
            }

            override fun bulkUpdateFinished(document: Document) {
                updateIcon()
            }

            private fun updateIcon() {
                val text = expressionField.text.trim()
                val currentExpressionType =
                    if (text.startsWith("*")) ExpressionType.KARATE else ExpressionType.JAVASCRIPT
                if (text.startsWith("*")) {
                    iconLabel.icon = AllIcons.Nodes.Function
                    iconLabel.toolTipText = "Karate Expression (prefixed with *)"
                } else {
                    iconLabel.icon = AllIcons.FileTypes.JavaScript
                    iconLabel.toolTipText = "Karate JavaScript Expression"
                }
                if (currentExpressionType != expressionType) {
                    expressionType = currentExpressionType
                    trie = Trie()
                    updateTrieKeywords()
                }
                publisher.publishKarateVariables()
            }
        })
        updateTrieKeywords()
    }

    fun updateTrieKeywords() {
        when (expressionType) {
            ExpressionType.KARATE -> KARATE_KEYWORDS
            ExpressionType.JAVASCRIPT -> JS_KEYWORDS
        }.forEach {
            trie.addWord(it, ResultType.KEYWORD)
        }
    }

    private fun showJsonPopup(project: Project, json: String) {
        val editorFactory = EditorFactory.getInstance()
        val prettyJson = try {
            val jsonNode = JsonParser.parseString(json)
            GsonBuilder().setPrettyPrinting().create().toJson(jsonNode)
        } catch (_: Exception) {
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
