package io.github.nomisrev.openapi

import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

fun generate(path: String, output: String) {
  val rawSpec = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  (openAPI.routes().toFileSpecs() + openAPI.models().toFileSpecs() + predef).forEach {
    it.writeTo(Path(output))
  }
}
