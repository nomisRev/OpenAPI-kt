import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
  alias(libs.plugins.multiplatform) apply false
  alias(libs.plugins.publish) apply false
  alias(libs.plugins.assert)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
}

val assertId =
  libs.plugins.assert.get().pluginId
val spotlessId =
  libs.plugins.spotless.get().pluginId

subprojects {
  apply(plugin = assertId)
  @Suppress("OPT_IN_USAGE")
  configure<PowerAssertGradleExtension> {
    functions = listOf(
      "kotlin.test.assertEquals",
      "kotlin.test.assertTrue"
    )
  }
  apply(plugin = spotlessId)
  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      ktfmt().kotlinlangStyle().configure {
        it.setBlockIndent(2)
        it.setContinuationIndent(2)
        it.setRemoveUnusedImport(true)
      }
    }
  }
  tasks {
//    withType(Jar::class.java) {
//      manifest {
//        attributes("Automatic-Module-Name" to "io.github.nomisrev")
//      }
//    }
    withType<JavaCompile> {
      options.release.set(11)
    }
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

tasks.dokkaHtmlMultiModule {
  moduleName.set("OpenAPI-kt")
}
