package `in`.srikanthk.devlabs.kchopdebugger.utils

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TrieTest : BasePlatformTestCase() {
    private lateinit var stringTrie: Trie<String>
    private lateinit var intTrie: Trie<Int>
    private lateinit var dataClassTrie: Trie<WordData>

    data class WordData(val word: String, val frequency: Int)

    override fun setUp() {
        super.setUp()
        stringTrie = Trie()
        intTrie = Trie()
        dataClassTrie = Trie()
    }

    fun testAddAndSearchWithStringData() {
        stringTrie.addWord("apple", "A fruit")
        stringTrie.addWord("application", "A program")

        val results = stringTrie.searchWord("app", "")
        val data = results.map { it.data }
        assertEquals(2, data.size)
        assertEquals(setOf("A fruit", "A program"), data.toSet())
    }

    fun testAddAndSearchWithIntegerData() {
        intTrie.addWord("one", 1)
        intTrie.addWord("two", 2)
        intTrie.addWord("ten", 10)

        val results = intTrie.searchWord("t", "")
        val data = results.map { it.data }
        assertEquals(2, data.size)
        assertEquals(setOf(2, 10), data.toSet())
    }

    fun testAddAndSearchWithCustomDataClass() {
        val appleData = WordData("apple", 10)
        val applyData = WordData("apply", 5)
        dataClassTrie.addWord("apple", appleData)
        dataClassTrie.addWord("apply", applyData)

        val results = dataClassTrie.searchWord("appl", "")
        val data = results.map { it.data }
        assertEquals(2, data.size)
        assertEquals(setOf(appleData, applyData), data.toSet())
    }

    fun testSearchWithNoMatchingPrefix() {
        stringTrie.addWord("apple", "A fruit")
        val results = stringTrie.searchWord("b", "")
        assertTrue(results.isEmpty())
    }

    fun testSearchInEmptyTrie() {
        val results = stringTrie.searchWord("a", "")
        assertTrue(results.isEmpty())
    }

    fun testOverwriteDataForDuplicateWord() {
        stringTrie.addWord("apple", "Original data")
        stringTrie.addWord("apple", "Updated data")

        val results = stringTrie.searchWord("apple", "")
        val data = results.map { it.data }
        assertEquals(1, data.size)
        assertEquals("Updated data", data.first())
    }

    fun testSearchWithEmptyPrefixReturnsAllData() {
        stringTrie.addWord("one", "1")
        stringTrie.addWord("two", "2")

        val results = stringTrie.searchWord("", "")
        val data = results.map { it.data }
        assertEquals(2, data.size)
        assertEquals(setOf("1", "2"), data.toSet())
    }

    fun testAddEmptyWord() {
        stringTrie.addWord("", "Empty word data")
        val results = stringTrie.searchWord("", "")
        val data = results.map { it.data }
        assertEquals(1, data.size)
        assertEquals("Empty word data", data.first())
    }
}
