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
  alias(libs.plugins.dokka)
  alias(libs.plugins.spotless)
}

@Suppress("OPT_IN_USAGE")
configure<PowerAssertGradleExtension> {
  functions = listOf(
    "kotlin.test.assertEquals",
    "kotlin.test.assertTrue"
  )
}

subprojects {
  tasks {
    withType(Jar::class.java) {
      manifest {
        attributes("Automatic-Module-Name" to "io.github.nomisrev")
      }
    }
    withType<JavaCompile> {
      options.release.set(8)
    }
    withType<KotlinCompile> {
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
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

configure<SpotlessExtension> {
  kotlin {
    target("**/*.kt")
    // can't deal with context receivers
    targetExclude("/generation/src/**")
    ktfmt().kotlinlangStyle().configure {
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImport(true)
    }
    trimTrailingWhitespace()
    endWithNewline()
  }
}