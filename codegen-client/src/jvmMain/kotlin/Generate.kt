import io.github.nomisrev.codegen.api.GeneratedFile
import io.github.nomisrev.codegen.emit.generate
import io.github.nomisrev.codegen.transform.ApiToIr
import io.github.nomisrev.codegen.transform.ModelRegistry
import io.github.nomisrev.codegen.transform.toIrFile
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.models
import io.github.nomisrev.openapi.root
import io.ktor.utils.io.readText
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

data class GenerationConfig(
  val path: String,
  val output: String,
  val `package`: String,
  val name: String,
)

fun generate(config: GenerationConfig) {
  val generatedFiles = files(config)
  writeGenerated(generatedFiles, config.output)
}

fun files(config: GenerationConfig): List<GeneratedFile> = buildList {
  val rawSpec = SystemFileSystem.source(Path(config.path)).buffered().use { it.readText() }
  val openAPI =
    when (val extension = config.path.substringAfterLast(".")) {
      "json" -> OpenAPI.fromJson(rawSpec)
      "yaml",
      "yml" -> OpenAPI.fromYaml(rawSpec)

      else -> throw IllegalArgumentException("Unsupported file extension: $extension")
    }

  val root = openAPI.root(config.name)
  val models = openAPI.models()

  // Build model IR and emit
  val modelIr = models.toIrFile(fileName = "Models.kt", pkg = config.`package`)
  add(generate(modelIr))

  // Build API IR and emit
  val registry = ModelRegistry.from(models)
  val apiIrFiles =
    ApiToIr.generate(root, ApiToIr.Ctx(pkg = config.`package`, name = config.name, registry = registry))
  for (file in apiIrFiles) {
    add(generate(file))
  }
}

private fun writeGenerated(files: List<GeneratedFile>, outputDir: String) {
  val base = Paths.get(outputDir)
  Files.createDirectories(base)
  for (f in files) {
    val out = base.resolve(f.path)
    val parent = out.parent
    if (parent != null) Files.createDirectories(parent)
    Files.writeString(
      out,
      f.content,
      java.nio.charset.StandardCharsets.UTF_8,
      StandardOpenOption.CREATE,
      StandardOpenOption.WRITE,
    )
  }
}
