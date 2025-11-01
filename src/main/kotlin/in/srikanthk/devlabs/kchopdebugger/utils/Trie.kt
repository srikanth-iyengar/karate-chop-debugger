package `in`.srikanthk.devlabs.kchopdebugger.utils

data class TrieResult<T>(var word: String, var data: T)

class Trie<T> {
    val children: HashMap<Char, Trie<T>?> = HashMap()
    var isEnd = false;
    var data: T? = null;

    fun addWord(word: String, data: T? = null) {
        if (word.isEmpty()) {
            isEnd = true
            this.data = data
            return
        }

        val firstChar = word[0]
        if (children[firstChar] == null) {
            children[firstChar] = Trie()
        }
        children[firstChar]?.addWord(word.substring(1), data)
    }

    fun searchWord(word: String, prefix: String): List<TrieResult<T>> {
        if (word.isEmpty()) {
            val result = this.collectNode(prefix)
            result.sortWith(Comparator { a, b -> a.word.length.compareTo(b.word.length) })
            return result
        }

        val firstChar = word[0]
        if (children[firstChar] == null) {
            return emptyList()
        }

        return children[firstChar]?.searchWord(word.substring(1), prefix + firstChar) ?: emptyList()
    }

    fun collectNode(prefix: String): ArrayList<TrieResult<T>> {
        if (this.children.isEmpty() && this.isEnd) {
            return ArrayList<TrieResult<T>>().apply { this@apply.add(TrieResult(prefix, this@Trie.data!!)) }
        }
        val result: HashSet<TrieResult<T>> = HashSet()
        if (this.isEnd) {
            result.add(TrieResult<T>(prefix, data!!))
        }
        for (entry in children) {
            val childResult = entry.value?.collectNode(entry.key.toString())
            if (entry.value?.isEnd == true) {
                result.add(TrieResult<T>(prefix + entry.key, entry.value?.data!!))
            }
            childResult?.forEach {
                result.add(TrieResult(prefix + it.word, it.data))
            }
        }
        return ArrayList(result)
    }
}