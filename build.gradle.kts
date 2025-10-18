import java.nio.file.Files
import java.nio.file.Paths

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "in.srikanthk.devlabs"
version = "2.3.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2025.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        bundledPlugin("org.jetbrains.idea.maven")
        bundledPlugin("com.intellij.modules.json")
    }

    implementation("com.intuit.karate:karate-junit5:1.4.1")
    implementation(project(":debug-agent"))
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
            <html>
              <body>
                <ul>
                  <li><b>Run individual scenarios</b> directly from the editor with the Run icon.</li>
                  <li><b>Seamless IntelliJ breakpoint integration</b> – no manual setup needed.</li>
                  <li>Added <b>Step Into</b> support to move through tests step by step during debugging.</li>
                  <li>Improved <b>syntax highlighting</b> and <b>newline handling</b> in Karate feature files.</li>
                  <li>Removed outdated breakpoint panels for a <b>cleaner, simpler interface</b>.</li>
                </ul>
                <h3>Bug Fixes</h3>
                <ul>
                  <li>Resolved issues with parsing scenarios separated by blank lines.</li>
                  <li>Improved syntax highlighting stability.</li>
                  <li>Fixed rare crash when re-running tests.</li>
                </ul>
              </body>
            </html>
        """.trimIndent()
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }

    signPlugin {
        val certPath = System.getenv("CERTIFICATE_CHAIN_PATH")
        val keyPath = System.getenv("PRIVATE_KEY_PATH")
        val certPassword = System.getenv("PRIVATE_KEY_PASSWORD")

        println("🔐 Starting plugin signing process...")
        println("🔍 CERTIFICATE_CHAIN_PATH = $certPath")
        println("🔍 PRIVATE_KEY_PATH = $keyPath")
        println("🔍 PRIVATE_KEY_PASSWORD = ${if (certPassword != null) "***" else "NOT SET"}")

        if(certPath != null && keyPath != null) {
            val certFile = Paths.get(certPath)
            val keyFile = Paths.get(keyPath)

            val certContent = Files.readString(certFile).trim()
            val keyContent = Files.readString(keyFile).trim()

            println("✅ Certificate and key files loaded successfully.")
            certificateChain.set(certContent)
            privateKey.set(keyContent)
            password.set(certPassword)
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":debug-agent:build")

    from("debug-agent/build/libs/debug-agent-${project.version}.jar") {
        into("lib")
        rename { "agent.jar" }
    }
}
