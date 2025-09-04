import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
  id(libs.plugins.jvm.get().pluginId)
  `java-gradle-plugin`
  id(libs.plugins.publish.get().pluginId)
  alias(libs.plugins.dokka)
}

dependencies {
  implementation(projects.codegenClient)
  implementation(projects.generation)
  implementation(libs.stdlib)
  compileOnly(libs.gradle)
  compileOnly(libs.android.plugin)
  testImplementation(libs.test)
}

gradlePlugin {
  @Suppress("UnstableApiUsage") website.set(property("WEBSITE").toString())
  @Suppress("UnstableApiUsage") vcsUrl.set(property("VCS_URL").toString())
  plugins {
    create(property("ID").toString()) {
      id = property("ID").toString()
      implementationClass = property("IMPLEMENTATION_CLASS").toString()
      version = properties["version"].toString()
      description = property("DESCRIPTION").toString()
      displayName = property("DISPLAY_NAME").toString()
      @Suppress("UnstableApiUsage") tags.set(listOf("Kotlin", "OpenAPI", "Code Generation"))
    }
  }
}

tasks.withType<DokkaTaskPartial>().configureEach {
  moduleName.set("OpenAPI Kotlin Gradle Plugin")
  dokkaSourceSets {
    named("main") {
      includes.from("README.md")
      sourceLink {
        localDirectory.set(file("src/main/kotlin"))
        remoteUrl.set(
          uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/gradle-plugin/src/main").toURL()
        )
        remoteLineSuffix.set("#L")
      }
    }
  }
}
