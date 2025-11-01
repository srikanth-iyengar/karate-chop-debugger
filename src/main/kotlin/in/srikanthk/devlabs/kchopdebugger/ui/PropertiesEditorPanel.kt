package `in`.srikanthk.devlabs.kchopdebugger.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import `in`.srikanthk.devlabs.kchopdebugger.configuration.KaratePropertiesState
import `in`.srikanthk.devlabs.kchopdebugger.configuration.PropertiesState
import java.awt.BorderLayout
import java.awt.datatransfer.DataFlavor
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.KeyStroke
import javax.swing.event.TableModelEvent
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer

data class PropertyEntry(var name: String, var value: String)

class PropertiesEditorPanel(val project: Project) : JPanel(BorderLayout()) {

    private val karatePropertiesState = KaratePropertiesState.getInstance(project)
    private val tableModel = PropertyTableModel()
    private val table = JBTable(tableModel).apply {
        autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        rowHeight = 24

        // Mask renderer
        columnModel.getColumn(1).cellRenderer = object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                table: JTable,
                value: Any?,
                isSelected: Boolean,
                hasFocus: Boolean,
                row: Int,
                column: Int
            ): java.awt.Component {
                val comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
                val model = table.model as PropertyTableModel
                val entry = model.entries[row]
                val shouldMask = entry.name.contains("password", true) ||
                        entry.name.contains("pwd", true) ||
                        entry.name.contains("secret", true)
                text = if (shouldMask && !isEditing(table, row, column)) "•".repeat(entry.value.length) else entry.value
                return comp
            }

            private fun isEditing(table: JTable, row: Int, col: Int) =
                table.editingRow == row && table.editingColumn == col
        }
    }

    init {
        loadProperties()

        // this doesn't work
        val saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        table.inputMap.put(saveKeyStroke, "saveProperties")
        table.actionMap.put("saveProperties", object : AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent?) {
                saveProperties()
            }
        })
        tableModel.addTableModelListener { tableModelEvent ->
            run {
                when (tableModelEvent.type) {
                    TableModelEvent.INSERT -> saveProperties()
                    TableModelEvent.DELETE -> saveProperties()
                    TableModelEvent.UPDATE -> saveProperties()
                }
            }
        }

        val panel = ToolbarDecorator.createDecorator(table)
            .setAddAction {
                tableModel.addRow(PropertyEntry("", ""))
            }
            .setRemoveAction {
                val row = table.selectedRow
                if (row != -1) {
                    tableModel.removeRow(row)
                }
            }
            .addExtraAction(object :
                AnAction("Paste", null, com.intellij.icons.AllIcons.Actions.MenuPaste) {
                override fun actionPerformed(p0: AnActionEvent) {
                    val clipboardText = CopyPasteManager.getInstance()
                        .getContents<String>(DataFlavor.stringFlavor) ?: return
                    clipboardText.lines().forEach { line ->
                        val parts = line.split("\t", limit = 2)
                        if (parts.size == 2) {
                            tableModel.addRow(PropertyEntry(parts[0].trim(), parts[1].trim()))
                        }
                    }
                }
            })
            .createPanel()

        add(JBScrollPane(panel), BorderLayout.CENTER)
    }

    private fun loadProperties() {
        val state = karatePropertiesState?.state ?: return
        val entries = state.state.entries.map { PropertyEntry(it.key, it.value) }
        tableModel.setData(entries)
    }

    private fun saveProperties() {
        val state: MutableMap<String, String> = mutableMapOf()
        tableModel.entries.forEach {
            if (it.name.isNotBlank()) {
                state[it.name.trim()] = it.value.trim()
            }
        }
        karatePropertiesState?.loadState(PropertiesState(state));
    }

    private class PropertyTableModel : AbstractTableModel() {
        val entries: MutableList<PropertyEntry> = mutableListOf()

        fun setData(data: List<PropertyEntry>) {
            entries.clear()
            entries.addAll(data)
            fireTableDataChanged()
        }

        fun addRow(entry: PropertyEntry) {
            entries.add(entry)
            fireTableRowsInserted(entries.size - 1, entries.size - 1)
        }

        fun removeRow(index: Int) {
            if (index in entries.indices) {
                entries.removeAt(index)
                fireTableRowsDeleted(index, index)
            }
        }

        override fun getRowCount(): Int = entries.size
        override fun getColumnCount(): Int = 2
        override fun getColumnName(col: Int): String = if (col == 0) "Name" else "Value"
        override fun getValueAt(row: Int, col: Int): Any =
            if (col == 0) entries[row].name else entries[row].value

        override fun isCellEditable(row: Int, col: Int): Boolean = true

        override fun setValueAt(aValue: Any?, row: Int, col: Int) {
            val value = StringUtil.notNullize(aValue?.toString())
            if (row in entries.indices) {
                if (col == 0) entries[row].name = value else entries[row].value = value
                fireTableCellUpdated(row, col)
            }
        }
    }
}
