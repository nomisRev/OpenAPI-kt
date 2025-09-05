import com.diffplug.gradle.spotless.SpotlessExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
  alias(libs.plugins.multiplatform) apply false
  alias(libs.plugins.publish) apply false
  alias(libs.plugins.assert)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kover)
}

val assertId = libs.plugins.assert.get().pluginId
val spotlessId = libs.plugins.spotless.get().pluginId
val publishId = libs.plugins.publish.get().pluginId

dependencies {
  kover(projects.parser)
  kover(projects.typed)
  kover(projects.generation)
  kover(projects.codegen)
}

subprojects {
  apply(plugin = assertId)
  @Suppress("OPT_IN_USAGE")
  configure<PowerAssertGradleExtension> {
    functions = listOf(
      "kotlin.test.assertEquals",
      "kotlin.test.assertTrue",
      "kotlin.test.assertFalse"
    )
  }

  apply(plugin = spotlessId)
  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt", "**/*.kts")
      ktfmt().kotlinlangStyle().configure {
        it.setBlockIndent(2)
        it.setContinuationIndent(2)
        it.setRemoveUnusedImports(true)
        it.setManageTrailingCommas(true)
      }
    }
  }
  apply(plugin = publishId)
  configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
    val shouldSign =
      project.gradle.startParameter.taskNames.none {
        it.contains("publishToMavenLocal", ignoreCase = true)
      }
    if (shouldSign) signAllPublications()
  }
  tasks {
    withType<JavaCompile> { options.release.set(11) }
    withType<KotlinCompile> { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
    withType<Test> {
      useJUnitPlatform()
      testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("SKIPPED", "FAILED")
      }
    }
  }
}

tasks.dokkaHtmlMultiModule { moduleName.set("OpenAPI-kt") }
