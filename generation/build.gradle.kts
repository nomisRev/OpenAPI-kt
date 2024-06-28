import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
  id(libs.plugins.jvm.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
  alias(libs.plugins.dokka)
}

kotlin { compilerOptions.freeCompilerArgs.add("-Xcontext-receivers") }

dependencies {
  api(libs.okio)
  api(libs.ktor.client)
  api(projects.typed)
  api(libs.kasechange)
  api(libs.kotlinpoet)
}

tasks.withType<DokkaTaskPartial>().configureEach {
  moduleName.set("OpenAPI Kotlin Generator")
  dokkaSourceSets {
    named("main") {
      includes.from("README.md")
      sourceLink {
        localDirectory.set(file("src/main/kotlin"))
        remoteUrl.set(
          uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/generation/src/main").toURL()
        )
        remoteLineSuffix.set("#L")
      }
    }
  }
}
