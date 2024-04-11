package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.isValidClassname

public fun Template.toCode(code: KModel) {
  when (code) {
    KModel.Binary -> Unit
    is KModel.Collection -> toCode(code.value)
    KModel.JsonObject -> Unit
    is KModel.Primitive -> Unit
    is KModel.Enum -> toCode(code)
    is KModel.Object -> toCode(code)
    is KModel.Union -> toCode(code)
  }
}

public fun Template.Serializable() {
  addImport("kotlinx.serialization.Serializable")
  +"@Serializable"
}

public fun Template.toCode(enum: KModel.Enum) {
  Serializable()
  val isSimple = enum.values.all { it.rawName == it.simpleName && it.rawName.isValidClassname() }
  val constructor = if (isSimple) "" else "(val value: ${enum.inner.typeName()})"
  if (!isSimple) addImport("kotlinx.serialization.SerialName")
  block("enum class ${enum.simpleName}$constructor {") {
    enum.values.indented(separator = ",\n", postfix = ";\n") { entry ->
      if (isSimple) append(entry.rawName)
      else append("@SerialName(\"${entry.rawName}\") ${entry.simpleName}(\"${entry.rawName}\")")
    }
  }
}

// TODO fix indentation, and multiline (param) descriptions.
public fun Template.description(obj: KModel.Object) {
  val descriptions = obj.properties.mapNotNull { p -> p.description?.let { Pair(p.name, it) } }
  when {
    obj.description.isNullOrBlank() && descriptions.isEmpty() -> Unit
    obj.description.isNullOrBlank() && descriptions.isNotEmpty() -> {
      +"/**"
      if (!obj.description.isNullOrBlank()) {
        +"  * ${obj.description}"
        +"  *"
      }
      descriptions.indented(prefix = "  *", separator = "\n  *") { (name, description) ->
        append("@param $name $description")
      }
      +"  */"
    }
  }
}

public fun Template.addImports(obj: KModel.Object) =
  obj.properties.forEach { p ->
    when (p.type) {
      KModel.Binary -> addImport("io.FileUpload")
      KModel.JsonObject -> addImport("kotlinx.serialization.json.JsonElement")
      else -> Unit
    }
  }

public fun Template.toCode(obj: KModel.Object) {
  fun properties() =
    obj.properties.indented(separator = ",\n") { append(it.toCode()) }

  fun nested() =
    indented { obj.inline.indented(prefix = " {\n", postfix = "}") { toCode(it) } }

//  description(obj)
  addImports(obj)
  Serializable()
  +"data class ${obj.simpleName}("
  properties()
  +")"
  nested()
}

public fun KModel.Object.Property.toCode(): String {
  val nullable = if (isNullable) "?" else ""
  val default = defaultValue?.let { " = $it" } ?: ""
  // import??? Could be done in typename.
  return "val $name: ${type.typeName()}$nullable$default"
}

public fun KModel.typeName(): String =
  when (this) {
    KModel.Binary -> "FileUpload"
    KModel.JsonObject -> "JsonElement"
    is KModel.Collection.List -> "List<${this@typeName.value.typeName()}>"
    is KModel.Collection.Map -> "Map<${this@typeName.key.typeName()}, ${this@typeName.value.typeName()}>"
    is KModel.Collection.Set -> "Set<${this@typeName.value.typeName()}>"
    is KModel.Primitive -> name
    is KModel.Enum -> simpleName
    is KModel.Object -> simpleName
    is KModel.Union -> simpleName
  }

public fun Template.toCode(union: KModel.Union) {
  unionImports()
  +"@Serializable(with = ${union.simpleName}.Serializer::class)"
  block("sealed interface ${union.simpleName} {") {
    union.schemas.joinTo {
      +"@JvmInline"
      +"value class ${it.caseName}(val value: ${it.model.typeName()}): ${union.simpleName}"
    }
    block("object Serializer : KSerializer<${union.simpleName}> {") {
      +"@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)"
      expression("override val descriptor: SerialDescriptor =") {
        block("buildSerialDescriptor(\"${union.simpleName}\", PolymorphicKind.SEALED) {") {
          union.schemas.indented {
            append("element(\"${it.caseName}\", ${serializer(it.model)}.descriptor)")
          }
        }
      }
      line()
      block("override fun deserialize(decoder: Decoder): ${union.simpleName} {") {
        +"val json = decoder.decodeSerializableValue(JsonElement.serializer())"
        +"return attemptDeserialize(json,"
        union.schemas.indented(separator = ",\n") {
          append("Pair(${it.caseName}::class) { ${it.caseName}(Json.decodeFromJsonElement(${serializer(it.model)}, json)) }")
        }
        +")"
      }
      line()
      block("override fun serialize(encoder: Encoder, value: ${union.simpleName}) {") {
        +"when(value) {"
        union.schemas.indented {
          append("is ${it.caseName} -> encoder.encodeSerializableValue(${serializer(it.model)}, value.value)")
        }
        +"}"
      }
    }
  }
}

private fun Template.serializer(model: KModel): String =
  when (model) {
    is KModel.Collection.List -> {
      addImport("kotlinx.serialization.builtins.ListSerializer")
      "ListSerializer(${serializer(model.value)})"
    }

    is KModel.Collection.Map -> {
      addImport("kotlinx.serialization.builtins.MapSerializer")
      "MapSerializer(${serializer(model.key)}, ${serializer(model.value)})"
    }

    is KModel.Collection.Set -> {
      addImport("kotlinx.serialization.builtins.SetSerializer")
      "SetSerializer(${serializer(model.value)})"
    }

    is KModel.Primitive -> {
      addImport("kotlinx.serialization.builtins.serializer")
      "${model.name}.serializer()"
    }

    is KModel.Enum -> "${model.simpleName}.serializer()"
    is KModel.Object -> "${model.simpleName}.serializer()"
    is KModel.Union -> "${model.simpleName}.serializer()"
    KModel.Binary -> TODO("Cannot serializer File?")
    KModel.JsonObject -> {
      addImport("kotlinx.serialization.json.JsonElement")
      "JsonElement.serializer()"
    }
  }

public fun Template.unionImports() {
  addImports(
    "kotlin.jvm.JvmInline",
    "kotlinx.serialization.Serializable",
    "kotlinx.serialization.KSerializer",
    "kotlinx.serialization.InternalSerializationApi",
    "kotlinx.serialization.ExperimentalSerializationApi",
    "kotlinx.serialization.descriptors.PolymorphicKind",
    "kotlinx.serialization.descriptors.SerialDescriptor",
    "kotlinx.serialization.descriptors.buildSerialDescriptor",
    "kotlinx.serialization.encoding.Decoder",
    "kotlinx.serialization.encoding.Encoder",
    "kotlinx.serialization.json.Json",
    "kotlinx.serialization.json.JsonElement",
  )
}

// TODO include nicer message about expected format
internal val predef: String = """
import kotlin.reflect.KClass
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement  
  
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