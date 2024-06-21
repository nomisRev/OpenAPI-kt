package io.github.nomisrev.openapi

import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.io.path.Path

fun generate(path: String, output: String) {
  val rawSpec = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  with(OpenAPIContext("io.github.nomisrev.openapi")) {
    val root = openAPI.root()
    val models = openAPI.models()
    val modelFileSpecs = models.toFileSpecs()
    val rootFileSpecs = root.toFileSpecs()
    val files = rootFileSpecs + modelFileSpecs + predef() + additionalFiles()
    files.forEach { it.writeTo(Path(output)) }
  }
}
