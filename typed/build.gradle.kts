import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
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

kotlin {
  jvm()
  macosArm64()
  linuxX64()

  sourceSets {
    commonMain {
      dependencies {
        api(projects.parser)
        api(libs.ktor.client)
      }
    }
  }
}
