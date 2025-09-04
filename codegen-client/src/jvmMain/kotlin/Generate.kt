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

  // Emit Predef utilities required by generated Models/APIs
  add(
    GeneratedFile(
      path =
        (if (config.`package`.isEmpty()) "" else config.`package`.replace('.', '/') + "/") +
          "Predef.kt",
      content = predefContent(config.`package`),
    )
  )

  // Build API IR and emit
  val registry = ModelRegistry.from(models)
  val apiIrFiles =
    ApiToIr.generate(
      root,
      ApiToIr.Ctx(pkg = config.`package`, name = config.name, registry = registry),
    )
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

fun predefContent(pkg: String): String =
  """package $pkg
    
import kotlinx.serialization.serializerOrNull
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.append
import io.ktor.http.ContentType
import io.ktor.http.Headers
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

@Serializable
public data object EmptyObject

public class UnionSerializationException(
    public val payload: JsonElement,
    public val errors: Map<KClass<*>, SerializationException>,
) : SerializationException()

public fun <A> attemptDeserialize(
    json: JsonElement,
    vararg block: Pair<KClass<*>, (JsonElement) -> A>,
): A {
    val errors = linkedMapOf<KClass<*>, SerializationException>()
    block.forEach { (kclass, f) ->
        try {
            return f(json)
        } catch (e: SerializationException) {
            errors[kclass] = e
        }
    }
    throw UnionSerializationException(json, errors)
}

public fun <A> deserializeOpenEnum(
    value: String,
    open: (String) -> A,
    vararg block: Pair<KClass<*>, (String) -> A?>,
): A {
    val errors = linkedMapOf<KClass<*>, SerializationException>()
    block.forEach { (kclass, f) ->
        try {
            f(value)?.let { res -> return res }
        } catch (e: SerializationException) {
            errors[kclass] = e
        }
    }
    return open(value)
}

public data class UploadFile(
    public val filename: String,
    public val bodyBuilder: Sink.() -> Unit,
    public val contentType: ContentType? = null,
    public val size: Long? = null,
)

public fun <T : Any> FormBuilder.appendAll(
    key: String,
    value: T?,
    headers: Headers = Headers.Empty,
) {
    when (value) {
        is String -> append(key, value, headers)
        is Number -> append(key, value, headers)
        is Boolean -> append(key, value, headers)
        is ByteArray -> append(key, value, headers)
        is Source -> append(key, value, headers)
        is InputProvider -> append(key, value, headers)
        is ChannelProvider -> append(key, value, headers)
        is UploadFile -> appendUploadedFile(key, value)
        is Enum<*> -> append(key, serialNameOrEnumValue(value), headers)
        null -> Unit
        else -> append(FormPart(key, value, headers))
    }
}

private fun FormBuilder.appendUploadedFile(key: String, file: UploadFile) {
    append(
        key = key,
        filename = file.filename,
        contentType = file.contentType ?: ContentType.Application.OctetStream,
        size = file.size,
        bodyBuilder = file.bodyBuilder,
    )
}

@kotlin.OptIn(
    kotlinx.serialization.ExperimentalSerializationApi::class,
    kotlinx.serialization.InternalSerializationApi::class,
)
private fun <T : Enum<T>> serialNameOrEnumValue(enum: Enum<T>): String =
    enum::class.serializerOrNull()?.descriptor?.getElementName(enum.ordinal) ?: enum.toString()
"""
