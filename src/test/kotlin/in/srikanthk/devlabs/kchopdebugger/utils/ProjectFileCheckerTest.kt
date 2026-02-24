package `in`.srikanthk.devlabs.kchopdebugger.utils

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

class ProjectFileCheckerTest : BasePlatformTestCase() {
    private lateinit var tempDir: File
    private lateinit var checker: ProjectFileChecker
    private lateinit var srcTestJava: File
    private lateinit var srcTestResources: File

    override fun setUp() {
        super.setUp()
        tempDir = createTempDirectory()
        checker = ProjectFileChecker(tempDir.absolutePath)
        srcTestJava = File(tempDir, "src/test/java")
        srcTestResources = File(tempDir, "src/test/resources")
    }

    fun testNeedsMavenBuildWhenNoSourceDirectories() {
        assertTrue(checker.needsMavenBuild())
    }

    fun testNeedsMavenBuildWhenTimestampIsZero() {
        srcTestJava.mkdirs()
        assertTrue(checker.needsMavenBuild())
    }

    fun testNeedsMavenBuildWhenJavaFileModifiedAfterTimestamp() {
        srcTestJava.mkdirs()
        val javaFile = File(srcTestJava, "Test.java")
        javaFile.createNewFile()
        
        checker.setLastBuildTimestamp(System.currentTimeMillis() - 1000)
        
        assertTrue(checker.needsMavenBuild())
    }

    fun testNeedsMavenBuildWhenJavaFileNotModified() {
        srcTestJava.mkdirs()
        val javaFile = File(srcTestJava, "Test.java")
        javaFile.createNewFile()
        
        val timestamp = System.currentTimeMillis()
        javaFile.setLastModified(timestamp - 2000)
        checker.setLastBuildTimestamp(timestamp - 1000)
        
        assertFalse(checker.needsMavenBuild())
    }

    fun testNeedsMavenBuildWhenOnlyFeatureFileModified() {
        srcTestJava.mkdirs()
        val featureFile = File(srcTestJava, "test.feature")
        featureFile.createNewFile()
        
        val timestamp = System.currentTimeMillis()
        featureFile.setLastModified(timestamp - 500)
        checker.setLastBuildTimestamp(timestamp - 1000)
        
        assertFalse(checker.needsMavenBuild())
    }

    fun testNeedsFeatureReloadWhenNoSourceDirectories() {
        assertFalse(checker.needsFeatureReload())
    }

    fun testNeedsFeatureReloadWhenTimestampIsZero() {
        srcTestJava.mkdirs()
        assertFalse(checker.needsFeatureReload())
    }

    fun testNeedsFeatureReloadWhenFeatureFileModifiedAfterTimestamp() {
        srcTestJava.mkdirs()
        val featureFile = File(srcTestJava, "test.feature")
        featureFile.createNewFile()
        
        checker.setLastBuildTimestamp(System.currentTimeMillis() - 1000)
        
        assertTrue(checker.needsFeatureReload())
    }

    fun testNeedsFeatureReloadWhenFeatureFileNotModified() {
        srcTestJava.mkdirs()
        val featureFile = File(srcTestJava, "test.feature")
        featureFile.createNewFile()
        
        val timestamp = System.currentTimeMillis()
        featureFile.setLastModified(timestamp - 2000)
        checker.setLastBuildTimestamp(timestamp - 1000)
        
        assertFalse(checker.needsFeatureReload())
    }

    fun testNeedsFeatureReloadInResourcesDirectory() {
        srcTestResources.mkdirs()
        val featureFile = File(srcTestResources, "test.feature")
        featureFile.createNewFile()
        
        checker.setLastBuildTimestamp(System.currentTimeMillis() - 1000)
        
        assertTrue(checker.needsFeatureReload())
    }

    fun testSourceDirectoriesExistWhenNeitherExists() {
        assertFalse(checker.sourceDirectoriesExist())
    }

    fun testSourceDirectoriesExistWhenSrcTestJavaExists() {
        srcTestJava.mkdirs()
        assertTrue(checker.sourceDirectoriesExist())
    }

    fun testSourceDirectoriesExistWhenSrcTestResourcesExists() {
        srcTestResources.mkdirs()
        assertTrue(checker.sourceDirectoriesExist())
    }

    fun testGetSourceDirectoriesReturnsCorrectDirs() {
        srcTestJava.mkdirs()
        srcTestResources.mkdirs()
        
        val directories = checker.getSourceDirectories()
        
        assertEquals(2, directories.size)
        assertTrue(directories.contains(srcTestJava))
        assertTrue(directories.contains(srcTestResources))
    }

    fun testGetSourceDirectoriesReturnsOnlyExisting() {
        srcTestJava.mkdirs()
        
        val directories = checker.getSourceDirectories()
        
        assertEquals(1, directories.size)
        assertEquals(srcTestJava, directories[0])
    }

    fun testGetSourceDirectoriesReturnsEmptyWhenNoneExist() {
        val directories = checker.getSourceDirectories()
        
        assertTrue(directories.isEmpty())
    }

    fun testGetLastBuildTimestamp() {
        val timestamp = 12345L
        checker.setLastBuildTimestamp(timestamp)
        
        assertEquals(timestamp, checker.getLastBuildTimestamp())
    }

    fun testGetSourceTestJavaDir() {
        assertEquals(srcTestJava.absolutePath, checker.getSourceTestJavaDir().absolutePath)
    }

    fun testGetSourceTestResourcesDir() {
        assertEquals(srcTestResources.absolutePath, checker.getSourceTestResourcesDir().absolutePath)
    }

    fun testGetTargetTestClassesDir() {
        val expected = File(tempDir, "target/test-classes")
        assertEquals(expected.absolutePath, checker.getTargetTestClassesDir().absolutePath)
    }

    fun testNeedsMavenBuildWhenJavaFileInSubdirectoryModified() {
        val subDir = File(srcTestJava, "com/test")
        subDir.mkdirs()
        val javaFile = File(subDir, "Test.java")
        javaFile.createNewFile()
        
        checker.setLastBuildTimestamp(System.currentTimeMillis() - 1000)
        
        assertTrue(checker.needsMavenBuild())
    }

    fun testNeedsFeatureReloadWhenFeatureFileInSubdirectoryModified() {
        val subDir = File(srcTestJava, "features")
        subDir.mkdirs()
        val featureFile = File(subDir, "test.feature")
        featureFile.createNewFile()
        
        checker.setLastBuildTimestamp(System.currentTimeMillis() - 1000)
        
        assertTrue(checker.needsFeatureReload())
    }
}
