plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.jakubjirak.dpcpp"

version = project.findProperty("pluginVersion").toString()

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

intellij {
    version.set("2023.2.5")
    type.set("CL")
    plugins.set(listOf("com.intellij.cidr.lang", "com.intellij.cidr.base"))
    updateSinceUntilBuild.set(false)
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        version.set(project.version.toString())
        sinceBuild.set("232")
        untilBuild.set("")
        
        changeNotes.set("""
            <h3>Intel DPC++ Support - Beta Release</h3>
            <ul>
              <li>SYCL 2020 syntax highlighting</li>
              <li>Code completion for SYCL APIs</li>
              <li>Live templates for common patterns</li>
              <li>CMake integration with oneAPI detection</li>
              <li>Documentation and samples browser</li>
            </ul>
        """.trimIndent())
    }
    
    register("updateReleaseVersion") {
        doFirst {
            val versionParts = version.toString().split(".")
            val releaseVersion = "${versionParts[0]}${versionParts[1]}"
            val pluginXml = file("src/main/resources/META-INF/plugin.xml")
            val content = pluginXml.readText()
            val updatedContent = content.replace(
                Regex("""release-version="\d+""""),
                """release-version="$releaseVersion""""
            )
            pluginXml.writeText(updatedContent)
        }
    }
    
    named("patchPluginXml") {
        dependsOn("updateReleaseVersion")
    }

    runPluginVerifier {
        ideVersions.set(listOf("IC-2023.2.8", "IC-2024.3"))
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PLUGIN_TOKEN"))
        channels.set(listOf(if (project.version.toString().contains("beta")) "beta" else "default"))
    }

    buildSearchableOptions {
        enabled = false
    }
}
