package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

suspend fun main() {
  //    FileSystem.SYSTEM.generateModel("openai.json")
  val rawSpec = FileSystem.SYSTEM.read("openai.json".toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  val root = openAPI.routes()
  (apis(root, DefaultNamingStrategy) +
      openAPI.models().mapNotNull { it.toFileSpec(DefaultNamingStrategy) } +
      predef)
    .forEach { it.writeTo(Path("../example/build/generated/openapi/src/commonMain/kotlin/")) }
}
