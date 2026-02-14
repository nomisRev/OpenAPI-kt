package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.TypedApiContext
import io.github.nomisrev.openapi.root
import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

data class GenerationConfig(
  val path: String,
  val output: String,
  val `package`: String,
  val name: String,
  val isK2: Boolean,
)

@JvmOverloads
fun generate(config: GenerationConfig, fileSystem: FileSystem = FileSystem.SYSTEM) {
  val rawSpec = fileSystem.read(config.path.toPath()) { readUtf8() }
  val openAPI =
    when (val extension = config.path.substringAfterLast(".")) {
      "json" -> OpenAPI.Companion.fromJson(rawSpec)
      "yaml",
      "yml" -> OpenAPI.Companion.fromYaml(rawSpec)

      else -> throw IllegalArgumentException("Unsupported file extension: $extension")
    }
  val ctx = TypedApiContext(openAPI)
  val root = ctx.root(config.name)
  val models = ctx.models()

  with(OpenAPIContext(config)) {
    val modelFileSpecs = models.toFileSpecs()
    val rootFileSpecs = root.toFileSpecs()
    val files = rootFileSpecs + modelFileSpecs + predef() + additionalFiles()
    files.forEach { it.writeTo(Path(config.output)) }
  }
}
