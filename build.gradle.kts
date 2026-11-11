plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij.platform)
    alias(libs.plugins.detekt)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
    jvmToolchain(providers.gradleProperty("javaVersion").get().toInt())
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create(
            providers.gradleProperty("platformType"),
            providers.gradleProperty("platformVersion"),
        )
        bundledPlugins("com.intellij.cidr.lang", "com.intellij.cidr.base")
        pluginVerifier()
        zipSigner()
    }

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

// Exclude IDE-provided libraries from plugin distribution
configurations {
    runtimeClasspath {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

intellijPlatform {
    buildSearchableOptions = false

    pluginConfiguration {
        id = providers.gradleProperty("pluginGroup")
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }

        changeNotes = """
            <h3>2026.8.0</h3>
            <ul>
              <li>The plugin is now completely free — all features unlocked, no license needed.</li>
              <li>Source code is now open source under Apache-2.0: https://github.com/JirakJ/intel-dpcpp-plugin</li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        ides {
            ide("CL", "2023.2.5")
            ide("CL", "2024.3.6")
        }
    }

    signing {
        // Configure via environment variables:
        // ORG_GRADLE_PROJECT_signCertificateChain
        // ORG_GRADLE_PROJECT_signPrivateKey
        // ORG_GRADLE_PROJECT_signPassword
    }

    publishing {
        token = providers.environmentVariable("PLUGIN_TOKEN")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.12"
    }
}

detekt {
    config.setFrom(files("detekt.yml"))
    buildUponDefaultConfig = true
}
