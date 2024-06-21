plugins {
  id(libs.plugins.jvm.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
// Failing on Interceptors.kt
//  alias(libs.plugins.spotless)
}

kotlin {
  compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
}

dependencies {
  implementation(libs.okio)
  api(libs.ktor.client)
  api(projects.typed)
  api(libs.kasechange)
  api(libs.kotlinpoet)
}
