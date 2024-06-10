package io.github.nomisrev.openapi

import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

suspend fun main() {
  val rawSpec = FileSystem.SYSTEM.read("openai.json".toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  (openAPI.routes().toFileSpecs() + openAPI.models().toFileSpecs() + predef).forEach {
    it.writeTo(Path("../example/build/generated/openapi/src/commonMain/kotlin/"))
  }
}
