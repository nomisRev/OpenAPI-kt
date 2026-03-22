import com.vanniktech.maven.publish.MavenPublishBaseExtension
import dev.detekt.gradle.Detekt
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
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
//  alias(libs.plugins.spotless)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
    id("dev.detekt") version "2.0.0-alpha.2"
}

val assertId = libs.plugins.assert.get().pluginId
val spotlessId = libs.plugins.spotless.get().pluginId
val publishId = libs.plugins.publish.get().pluginId

dependencies {
    kover(projects.parser)
}

subprojects {
    apply(plugin = publishId)
    configure<MavenPublishBaseExtension> {
        publishToMavenCentral()
        val shouldSign =
            project.gradle.startParameter.taskNames.none {
                it.contains("publishToMavenLocal", ignoreCase = true)
            }
        if (shouldSign) signAllPublications()
    }

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

    apply(plugin = "dev.detekt")

    afterEvaluate {
        val detektTypeTasks = tasks.names.filter {
            it.matches(Regex("detekt(Main|Test)Jvm"))
        }
        if (detektTypeTasks.isNotEmpty()) {
            tasks.named("check") {
                dependsOn(detektTypeTasks)
            }
        }
        tasks.withType<Detekt>().configureEach {
            exclude { it.file.absolutePath.contains("kotlinTestData") }
            exclude { it.file.absolutePath.contains("/build/") }
        }
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

//tasks.dokkaHtmlMultiModule { moduleName.set("OpenAPI-kt") }
