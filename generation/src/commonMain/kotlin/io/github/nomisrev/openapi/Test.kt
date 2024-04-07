package io.github.nomisrev.openapi

import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

fun BufferedSink.writeUtf8Line(line: String) {
  writeUtf8("$line\n")
}

fun BufferedSink.writeUtf8Line() {
  writeUtf8("\n")
}

public fun FileSystem.program(
  pathSpec: String,
  `package`: String = "io.github.nomisrev.openapi",
  modelPackage: String = "$`package`.models",
  generationPath: String =
    "build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
  deleteRecursively(generationPath.toPath())
  fun file(name: String, imports: Set<Model.Import>, code: String) {
    write("$generationPath/models/$name.kt".toPath()) {
      writeUtf8Line("package $modelPackage")
      writeUtf8Line()
      writeUtf8Line(imports.joinToString("\n") { "import ${it.`package`}.${it.typeName}" })
      writeUtf8Line()
      writeUtf8Line(code)
    }
  }

  createDirectories("$generationPath/models".toPath())
  file(
    "predef.kt",
    setOf(
      Model.Import("kotlin.reflect", "KClass"),
      Model.Import("kotlinx.serialization", "SerializationException"),
      Model.Import("kotlinx.serialization.json", "JsonElement"),
    ),
    predef
  )

  val rawSpec = source(pathSpec.toPath()).buffer().use(BufferedSource::readUtf8)
  val openAPI = OpenAPI.fromJson(rawSpec)

  openAPI.models().forEach { model ->
    file(model.typeName, model.imports(), model.toKotlinCode(0))
  }
}

val predef = """
class OneOfSerializationException(
  val payload: JsonElement,
  val errors: Map<KClass<*>, SerializationException>,
  override val message: String =
    ${"\"\"\""}
    Failed to deserialize Json: ${'$'}payload.
    Errors: ${'$'}{
  errors.entries.joinToString(separator = "\n") { (type, error) ->
    "${'$'}type - failed to deserialize: ${'$'}{error.stackTraceToString()}"
  }
}
    ${"\"\"\""}.trimIndent()
) : SerializationException(message)

/**
 * OpenAI makes a lot of use of oneOf types (sum types, or unions types), but it **never** relies on
 * a discriminator field to differentiate between the types.
 *
 * Typically, what OpenAI does is attach a common field like `type` (a single value enum). I.e.
 * `MessageObjectContentInner` has a type field with `image` or `text`. Depending on the `type`
 * property, the other properties will be different.
 *
 * Due to the use of these fields, it **seems** there are no overlapping objects in the schema. So
 * to deserialize these types, we can try to deserialize each type and return the first one that
 * succeeds. In the case **all** fail, we throw [OneOfSerializationException] which includes all the
 * attempted types with their errors.
 *
 * This method relies on 'peeking', which is not possible in KotlinX Serialization. So to achieve
 * peeking, we first deserialize the raw Json to JsonElement, which safely consumes the buffer. And
 * then we can attempt to deserialize the JsonElement to the desired type, without breaking the
 * internal parser buffer.
 */
internal fun <A> attemptDeserialize(
  json: JsonElement,
  vararg block: Pair<KClass<*>, (json: JsonElement) -> A>
): A {
  val errors = linkedMapOf<KClass<*>, SerializationException>()
  block.forEach { (kclass, f) ->
    try {
      return f(json)
    } catch (e: SerializationException) {
      errors[kclass] = e
    }
  }
  throw OneOfSerializationException(json, errors)
}
""".trimIndent()