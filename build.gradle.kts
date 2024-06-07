import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
  alias(libs.plugins.multiplatform) apply false
  alias(libs.plugins.assert)
  alias(libs.plugins.publish)
  alias(libs.plugins.spotless)
}

configure<SpotlessExtension> {
  kotlin {
    target("**/*.kt")
    ktfmt().kotlinlangStyle().configure {
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImport(true)
    }
    trimTrailingWhitespace()
    endWithNewline()
  }
}

@Suppress("OPT_IN_USAGE")
configure<PowerAssertGradleExtension> {
  functions = listOf("kotlin.test.assertEquals")
}
