plugins {
  id(libs.plugins.jvm.get().pluginId)
  `java-gradle-plugin`
  id(libs.plugins.publish.get().pluginId)
}

dependencies {
  implementation(libs.stdlib)
  implementation(libs.gradle)
  implementation(projects.generation)
  testImplementation(libs.test)
}

gradlePlugin {
  @Suppress("UnstableApiUsage")
  website.set(property("WEBSITE").toString())
  @Suppress("UnstableApiUsage")
  vcsUrl.set(property("VCS_URL").toString())
  plugins {
    create(property("ID").toString()) {
      id = property("ID").toString()
      implementationClass = property("IMPLEMENTATION_CLASS").toString()
      version = property("VERSION").toString()
      description = property("DESCRIPTION").toString()
      displayName = property("DISPLAY_NAME").toString()
      @Suppress("UnstableApiUsage")
      tags.set(listOf("Kotlin", "OpenAPI", "Code Generation"))
    }
  }
}

