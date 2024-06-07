import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id(libs.plugins.jvm.get().pluginId)
  `java-gradle-plugin`
  id(libs.plugins.publish.get().pluginId)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.stdlib)
  implementation(libs.gradle)
  implementation(projects.generation)
  testImplementation(libs.test)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
  compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
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

