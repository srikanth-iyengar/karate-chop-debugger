package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KarateEnterHandlerTest : BasePlatformTestCase() {
    fun testScenarioHeaderAddsIndentedLine() {
        myFixture.configureByText(
            "sample.feature",
            """
            Feature: Sample feature
              Scenario: Happy path<caret>
            """.trimIndent()
        )

        applyKarateEnterIndent()

        assertEquals(
            lines(
                "Feature: Sample feature",
                "  Scenario: Happy path",
                "    "
            ),
            myFixture.editor.document.text
        )
        assertEquals(myFixture.editor.document.text.length, myFixture.editor.caretModel.offset)
    }

    fun testScenarioOutlineHeaderAddsIndentedLine() {
        myFixture.configureByText(
            "sample.feature",
            """
            Feature: Sample feature
              Scenario Outline: Happy path<caret>
            """.trimIndent()
        )

        applyKarateEnterIndent()

        assertEquals(
            lines(
                "Feature: Sample feature",
                "  Scenario Outline: Happy path",
                "    "
            ),
            myFixture.editor.document.text
        )
        assertEquals(myFixture.editor.document.text.length, myFixture.editor.caretModel.offset)
    }

    fun testExamplesHeaderAddsIndentedTableLine() {
        myFixture.configureByText(
            "sample.feature",
            """
            Feature: Sample feature
              Scenario Outline: Happy path
                Given def id = <id>
                Examples:<caret>
            """.trimIndent()
        )

        applyKarateEnterIndent()

        assertEquals(
            lines(
                "Feature: Sample feature",
                "  Scenario Outline: Happy path",
                "    Given def id = <id>",
                "    Examples:",
                "      "
            ),
            myFixture.editor.document.text
        )
        assertEquals(myFixture.editor.document.text.length, myFixture.editor.caretModel.offset)
    }

    private fun applyKarateEnterIndent() {
        myFixture.performEditorAction(IdeActions.ACTION_EDITOR_ENTER)
        var result: EnterHandlerDelegate.Result? = null
        WriteCommandAction.runWriteCommandAction(project) {
            result = KarateEnterHandler().postProcessEnter(myFixture.file, myFixture.editor, DataContext { null })
        }
        assertEquals(EnterHandlerDelegate.Result.Stop, result)
    }

    private fun lines(vararg lines: String): String = lines.joinToString("\n")
}
