import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
  alias(libs.plugins.multiplatform) apply false
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