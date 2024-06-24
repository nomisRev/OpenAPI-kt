import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
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
//  macosArm64()
//  linuxX64()

  sourceSets {
    commonMain {
      dependencies {
        api(projects.parser)
        api(libs.ktor.client)
      }
    }
  }
}

tasks.withType<DokkaTaskPartial>().configureEach {
  moduleName.set("OpenAPI Kotlin Typed")
  dokkaSourceSets {
    named("commonMain") {
      includes.from("README.md")
      sourceLink {
        localDirectory.set(file("src/commonMain/kotlin"))
        remoteUrl.set(uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/typed/src/commonMain").toURL())
        remoteLineSuffix.set("#L")
      }
    }
  }
}