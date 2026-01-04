plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "in.srikanthk.devlabs"
version = "2.4.11"

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

    implementation(project(":debug-agent"))
    testImplementation("junit:junit:4.13.2")
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
                        <li><b>Run individual scenarios</b> directly from the editor using the Run icon.</li>
                        <li>Enhanced <b>syntax highlighting</b> and improved <b>newline handling</b> in Karate feature files.</li>
                        <li>Introduced a <b>Step Back</b> feature for debugging.</li>
                        <li>Added <b>Hot Reload</b> support for reloading the current scenario during a debugging session.</li>
                        <li>Enabled the ability to <b>Step Over</b> and <b>Step Into</b> scenarios while debugging.</li>
                        <li>Added keyboard shortcuts <b>F8</b> (Step Over) and <b>F7</b> (Step Into).</li>
                        <li>Improved <b>Step Into</b> functionality for more precise test navigation during debugging.</li>
                        <li>Seamless <b>IntelliJ breakpoint integration</b> – no manual configuration required.</li>
                    </ul>
                    <h3>Bug Fixes</h3>
                    <ul>
                        <li>Resolved issues with parsing scenarios separated by blank lines.</li>
                        <li>Improved syntax highlighting stability and accuracy.</li>
                        <li>Fixed a rare crash occurring when re-running tests.</li>
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
        val certContent = System.getenv("CERTIFICATE_CHAIN")
        val keyContent = System.getenv("PRIVATE_KEY")
        val certPassword = System.getenv("PRIVATE_KEY_PASSWORD")

        println("🔍 PRIVATE_KEY_PASSWORD = ${if (certPassword != null) "***" else "NOT SET"}")

        if (certContent != null && keyContent != null) {
            println("✅ Certificate and key files loaded successfully.")
            certificateChain.set(certContent)
            privateKey.set(keyContent)
            password.set(certPassword)
        }
    }

    publishPlugin {
        token = providers.environmentVariable("JETBRAINS_TOKEN")
            .orElse(providers.systemProperty("JETBRAINS_TOKEN"))
            .orElse("")
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":debug-agent:build")

    from("debug-agent/build/libs/debug-agent-${project.version}.jar") {
        into("lib")
        rename { "agent.jar" }
    }
}
