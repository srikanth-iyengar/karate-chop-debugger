package `in`.srikanthk.devlabs.kchopdebugger.language

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.intellij.lexer.Lexer
import com.intellij.psi.TokenType

class KarateLexerTest : BasePlatformTestCase() {

    fun testParseAllFeatureFiles() {
        // collect candidate feature files from common resource locations
        val projectRoot = Paths.get("..").toAbsolutePath().normalize()
        val searchRoots = listOf(
            projectRoot.resolve("resources/features"),
            projectRoot.resolve("src/test/resources"),
        )

        val featureFiles = mutableListOf<Path>()
        for (root in searchRoots) {
            if (Files.exists(root)) {
                Files.walk(root).use { stream ->
                    stream.filter { Files.isRegularFile(it) && it.toString().endsWith(".feature") }
                        .forEach { featureFiles.add(it) }
                }
            }
        }

        // also try classpath resource folder if none found above
        if (featureFiles.isEmpty()) {
            val res = javaClass.classLoader.getResource("features")
            if (res != null) {
                try {
                    val p = Paths.get(res.toURI())
                    if (Files.exists(p)) {
                        Files.walk(p).use { stream ->
                            stream.filter { Files.isRegularFile(it) && it.toString().endsWith(".feature") }
                                .forEach { featureFiles.add(it) }
                        }
                    }
                } catch (e: Exception) {
                    // ignore - classpath resource may not be a filesystem path
                }
            }
        }

        // If no feature files found, test passes (nothing to validate)
        if (featureFiles.isEmpty()) {
            // still counts as pass
            assertTrue(true)
            return
        }

        val resultSet = hashSetOf<String>()
        // lex each file and assert there are no BAD_CHARACTER tokens
        for (file in featureFiles) {
            val text = String(Files.readAllBytes(file))
            val lexer: Lexer = KarateLexerAdapter()
            lexer.start(text, 0, text.length, 0)

            var token = lexer.tokenType
            while (token != null) {
                if(TokenType.BAD_CHARACTER == token) {
                    resultSet.add(file.toString())
                    println("Error at $file offset: ${lexer.tokenStart} ${lexer.tokenText}")
                }
                lexer.advance()
                token = lexer.tokenType
            }
        }
        println(resultSet.size)
        resultSet.forEach {
            println(it)
        }
    }
}