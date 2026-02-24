package `in`.srikanthk.devlabs.kchopdebugger.utils

import java.io.File

class ProjectFileChecker(
    private val basePath: String,
    private var lastBuildTimestamp: Long = 0
) {
    fun setLastBuildTimestamp(timestamp: Long) {
        lastBuildTimestamp = timestamp
    }

    fun getLastBuildTimestamp(): Long = lastBuildTimestamp

    fun getSourceTestJavaDir(): File = File(basePath, "src/test/java")

    fun getSourceTestResourcesDir(): File = File(basePath, "src/test/resources")

    fun getTargetTestClassesDir(): File = File(basePath, "target/test-classes")

    fun sourceDirectoriesExist(): Boolean {
        return getSourceTestJavaDir().exists() || getSourceTestResourcesDir().exists()
    }

    fun needsMavenBuild(): Boolean {
        if (!sourceDirectoriesExist()) {
            return true
        }

        if (lastBuildTimestamp == 0L) {
            return true
        }

        val srcTestJava = getSourceTestJavaDir()
        if (srcTestJava.exists()) {
            val hasJavaChanges = srcTestJava.walkTopDown()
                .filter { it.isFile && it.extension == "java" }
                .any { it.lastModified() > lastBuildTimestamp }
            if (hasJavaChanges) {
                return true
            }
        }

        return false
    }

    fun needsFeatureReload(): Boolean {
        if (!sourceDirectoriesExist()) {
            return false
        }

        if (lastBuildTimestamp == 0L) {
            return false
        }

        var hasFeatureChanges = false

        val srcTestJava = getSourceTestJavaDir()
        if (srcTestJava.exists()) {
            hasFeatureChanges = srcTestJava.walkTopDown()
                .filter { it.isFile && it.extension == "feature" }
                .any { it.lastModified() > lastBuildTimestamp }
        }

        if (!hasFeatureChanges) {
            val srcTestResources = getSourceTestResourcesDir()
            if (srcTestResources.exists()) {
                hasFeatureChanges = srcTestResources.walkTopDown()
                    .filter { it.isFile && it.extension == "feature" }
                    .any { it.lastModified() > lastBuildTimestamp }
            }
        }

        return hasFeatureChanges
    }

    fun getSourceDirectories(): List<File> {
        val directories = mutableListOf<File>()
        
        val srcTestJava = getSourceTestJavaDir()
        if (srcTestJava.exists()) {
            directories.add(srcTestJava)
        }

        val srcTestResources = getSourceTestResourcesDir()
        if (srcTestResources.exists()) {
            directories.add(srcTestResources)
        }

        return directories
    }
}
