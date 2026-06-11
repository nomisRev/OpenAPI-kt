import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.assert)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
}

val assertId = libs.plugins.assert.get().pluginId
val spotlessId = libs.plugins.spotless.get().pluginId
val publishId = libs.plugins.publish.get().pluginId

dependencies {
    kover(projects.parser)
    dokka(projects.parser)
    dokka(projects.typed)
    dokka(projects.renderer)
}

dokka {
    moduleName = "OpenAPI-kt"
}

subprojects {
    apply(plugin = publishId)

    apply(plugin = assertId)
    @Suppress("OPT_IN_USAGE")
    configure<PowerAssertGradleExtension> {
        functions = listOf(
            "kotlin.test.assertEquals",
            "kotlin.test.assertTrue",
            "kotlin.test.assertFalse",
            "io.github.nomisrev.Eq.Companion.invoke"
        )
    }

    tasks {
        withType<JavaCompile> { options.release.set(11) }
        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
        withType<Test> {
            useJUnitPlatform()
            testLogging {
                exceptionFormat = TestExceptionFormat.FULL
                events("SKIPPED", "FAILED")
            }
        }
    }
}

/**
 * Inspect a stored test failure by name.
 *
 * Usage:
 *   ./gradlew inspectTest -PtestName="<substring>"
 *   ./gradlew inspectTest -PtestName="<substring>" -Pmodule=parser
 *   ./gradlew inspectTest -PtestName="<substring>" -Plines=20   (0 = unlimited)
 *
 * Failures are read from .pi/gradle-test-failures.json written by .pi/gradle-test.ts.
 */
tasks.register("inspectTest") {
    group = "verification"
    description = "Inspect a stored test failure by name substring. Requires -PtestName=<name>."

    doLast {
        val testName = (project.findProperty("testName") as String?)
            ?: error("Provide -PtestName=<substring>, e.g. ./gradlew inspectTest -PtestName=\"should parse\"")
        val moduleName = project.findProperty("module") as String?
        val traceLines = (project.findProperty("lines") as String?)?.toIntOrNull() ?: 10

        val storeFile = rootProject.file(".pi/gradle-test-failures.json")
        if (!storeFile.exists()) {
            error("No failure store at .pi/gradle-test-failures.json – run .pi/gradle-test.ts first.")
        }

        @Suppress("UNCHECKED_CAST")
        val json = groovy.json.JsonSlurper().parse(storeFile) as Map<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val failures = json["failures"] as List<Map<String, Any?>>

        val needle = testName.lowercase()
        val matches = failures.filter { f ->
            val name = (f["name"] as? String ?: "").lowercase()
            val fullName = (f["fullName"] as? String ?: "").lowercase()
            val nameMatch = name.contains(needle) || fullName.contains(needle)
            nameMatch && (moduleName == null || f["module"] == moduleName)
        }

        if (matches.isEmpty()) {
            error("No stored failure matching \"$testName\".")
        }

        // Deduplicate by module::name; prefer the jvm platform variant.
        val deduped = matches
            .groupBy { "${it["module"]}::${it["name"]}" }
            .values
            .map { group -> group.find { it["platform"] == "jvm" } ?: group[0] }

        for (m in deduped) {
            val trace = m["trace"] as? String ?: ""
            if (trace.isNotEmpty()) {
                val lines = trace.split("\n")
                val truncated = if (traceLines > 0 && lines.size > traceLines)
                    lines.take(traceLines) + listOf("        ... (${lines.size - traceLines} more lines)")
                else
                    lines
                println(truncated.joinToString("\n"))
            }
            println()
        }
    }
}
