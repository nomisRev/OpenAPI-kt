package io.github.nomisrev.openapi

import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

fun main() {
  generate(
    GenerationConfig(
      "openai-api.yaml",
      "generation/build/generated",
      "io.github.nomisrev.openapi",
      "OpenAI"
    )
  )
}

data class GenerationConfig(
  val path: String,
  val output: String,
  val `package`: String,
  val name: String
)

@JvmOverloads
fun generate(config: GenerationConfig, fileSystem: FileSystem = FileSystem.SYSTEM) {
  val rawSpec = fileSystem.read(config.path.toPath()) { readUtf8() }
  val openAPI =
    when (val extension = config.path.substringAfterLast(".")) {
      "json" -> OpenAPI.fromJson(rawSpec)
      "yaml",
      "yml" -> OpenAPI.fromYaml(rawSpec)
      else -> throw IllegalArgumentException("Unsupported file extension: $extension")
    }
  with(OpenAPIContext(config.`package`)) {
    val root = openAPI.root(config.name)
    val models = openAPI.models()
    val modelFileSpecs = models.toFileSpecs()
    val rootFileSpecs = root.toFileSpecs()
    val files = rootFileSpecs + modelFileSpecs + predef() + additionalFiles()
    files.forEach { it.writeTo(Path(config.output)) }
  }
}
