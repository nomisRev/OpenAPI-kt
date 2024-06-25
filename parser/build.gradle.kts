import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
  alias(libs.plugins.dokka)
}

kotlin {
  explicitApi()

  jvm()
//  macosArm64()
//  linuxX64()

  sourceSets {
    commonMain {
      dependencies {
        api(libs.json)
        api("com.charleskorn.kaml:kaml:0.60.0")
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.test)
      }
    }
  }
}

tasks.withType<DokkaTaskPartial>().configureEach {
  moduleName.set("OpenAPI Kotlin Parser")
  dokkaSourceSets {
    named("commonMain") {
      includes.from("README.md")
      sourceLink {
        localDirectory.set(file("src/commonMain/kotlin"))
        remoteUrl.set(uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/parser/src/commonMain").toURL())
        remoteLineSuffix.set("#L")
      }
    }
  }
}
