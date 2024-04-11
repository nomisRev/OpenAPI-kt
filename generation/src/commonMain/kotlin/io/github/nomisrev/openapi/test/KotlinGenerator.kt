package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.isValidClassname

public fun Template.toCode(code: KModel) {
  when (code) {
    KModel.Binary -> Unit
    is KModel.Collection -> Unit
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
  fun Template.serialNameEnumCase(name: String) {
    addImport("kotlinx.serialization.SerialName")
    append("@SerialName(\"$name\") `$name`(\"$name\")")
  }

  Serializable()
  block("enum class ${enum.simpleName} {") {
    enum.values.indented(separator = ",\n", postfix = ";\n") {
      if (it.isValidClassname()) append(it)
      else serialNameEnumCase(it)
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

public fun Template.toCode(obj: KModel.Object) {
//  description(obj)
  Serializable()
  +"data class ${obj.simpleName}("
  obj.properties.indented(separator = ",\n") { append(it.toCode()) }
  append(")")
  indented {
    obj.inline.indented(
      prefix = " {\n",
      postfix = "}"
    ) { toCode(it) }
  }
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
    is KModel.Collection.List -> "List<${value.typeName()}>"
    is KModel.Collection.Map -> "Map<${key.typeName()}, ${value.typeName()}>"
    is KModel.Collection.Set -> "Set<${value.typeName()}>"
    KModel.JsonObject -> "JsonObject"
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
      +"value class ${it.caseName}(val value: ${it.caseName}): ${union.simpleName}"
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
    "kotlinx.serialization.Serializable",
    "kotlinx.serialization.KSerializer",
    "kotlinx.serialization.InternalSerializationApi",
    "kotlinx.serialization.descriptors.PolymorphicKind",
    "kotlinx.serialization.descriptors.SerialDescriptor",
    "kotlinx.serialization.descriptors.buildSerialDescriptor",
    "kotlinx.serialization.encoding.Decoder",
    "kotlinx.serialization.encoding.Encoder",
    "kotlinx.serialization.json.Json",
    "kotlinx.serialization.json.JsonElement",
  )
}

//import kotlin.jvm.JvmInline
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.InternalSerializationApi
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.builtins.ListSerializer
//import kotlinx.serialization.builtins.serializer
//import kotlinx.serialization.descriptors.PolymorphicKind
//import kotlinx.serialization.descriptors.SerialDescriptor
//import kotlinx.serialization.descriptors.buildSerialDescriptor
//import kotlinx.serialization.encoding.Decoder
//import kotlinx.serialization.encoding.Encoder
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonElement
