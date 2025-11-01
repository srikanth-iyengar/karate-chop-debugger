package `in`.srikanthk.devlabs.kchopdebugger.utils

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Assertions.*

class TrieTest : BasePlatformTestCase() {

    private lateinit var trie: Trie

    override fun setUp() {
        super.setUp()
        trie = Trie()
    }

    fun `test search suggestions with common prefix`() {
        trie.addWord("apple")
        trie.addWord("apply")
        trie.addWord("apricot")

        val suggestions = trie.searchWord("ap", "")
        assertEquals(3, suggestions.size)
        assertTrue(suggestions.containsAll(listOf("apple", "apply", "apricot")))
    }

    fun `test search suggestions with full word match`() {
        trie.addWord("apple")
        trie.addWord("applepie")

        val suggestions = trie.searchWord("apple", "")
        assertEquals(2, suggestions.size)
        assertTrue(suggestions.containsAll(listOf("apple", "applepie")))
    }

    fun `test search with no matching words`() {
        trie.addWord("apple")
        val suggestions = trie.searchWord("b", "")
        assertTrue(suggestions.isEmpty())
    }

    fun `test search in an empty trie`() {
        val suggestions = trie.searchWord("a", "")
        assertTrue(suggestions.isEmpty())
    }

    fun `test insert and search for single-letter words`() {
        trie.addWord("a")
        trie.addWord("b")

        val suggestionsA = trie.searchWord("a", "")
        assertEquals(1, suggestionsA.size)
        assertTrue(suggestionsA.contains("a"))

        val suggestionsB = trie.searchWord("b", "")
        assertEquals(1, suggestionsB.size)
        assertTrue(suggestionsB.contains("b"))
    }

    fun `test insert duplicate words`() {
        trie.addWord("apple")
        trie.addWord("apple")

        val suggestions = trie.searchWord("a", "")
        assertEquals(1, suggestions.size)
        assertTrue(suggestions.contains("apple"))
    }

    fun `test search with prefix that is a full word`() {
        trie.addWord("apple")
        trie.addWord("applepie")

        val suggestions = trie.searchWord("apple", "")
        assertEquals(2, suggestions.size)
        assertTrue(suggestions.containsAll(listOf("apple", "applepie")))
    }

    fun `test search for empty string prefix`() {
        trie.addWord("apple")
        trie.addWord("banana")

        val suggestions = trie.searchWord("", "")
        assertEquals(2, suggestions.size)
        assertTrue(suggestions.containsAll(listOf("apple", "banana")))
    }

    fun `test insert empty string`() {
        trie.addWord("")
        val suggestions = trie.searchWord("", "")
        assertEquals(1, suggestions.size)
        assertTrue(suggestions.contains(""))
    }
}
