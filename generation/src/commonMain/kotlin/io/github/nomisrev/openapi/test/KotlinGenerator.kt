package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.isValidClassname
import io.github.nomisrev.openapi.sanitize

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

public fun Template.toCode(enum: KModel.Enum) {
  +"@Serializable"
  block("enum class ${enum.simpleName} {") {
    enum.values.indented(separator = ",\n", postfix = ";\n") {
      if (it.isValidClassname()) append(it)
      else append("@SerialName(\"$it\") `${it.sanitize()}`(\"$it\")")
    }
  }
}

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
  description(obj)
  +"@Serializable"
  +"data class ${obj.simpleName}("
  obj.properties.indented(separator = ",\n") { append(it.toCode()) }
  +") ${if (obj.inline.isNotEmpty()) "{" else ""}"
  if (obj.inline.isNotEmpty()) indented {
    obj.inline.forEach {
      toCode(it)
      line()
    }
  }
}

public fun KModel.Object.Property.toCode(): String {
  val nullable = if (isNullable) "?" else ""
  val default = defaultValue?.let { " = $it" } ?: ""
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
  +"@Serializable(with = ${union.simpleName}.Serializer::class)"
  block("sealed interface ${union.simpleName} {") {
    union.schemas.joinTo {
      +"@JvmInline"
      +"value class Case${it.oneOfName()}(val value: ${it.typeName()}): ${union.simpleName}"
    }
    block("object Serializer : KSerializer<${union.simpleName}> {") {
      +"@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)"
      expression("override val descriptor: SerialDescriptor =") {
        block("buildSerialDescriptor(\"${union.simpleName}\", PolymorphicKind.SEALED) {") {
          union.schemas.indented {
            append("element(\"Case${it.oneOfName()}\", ${it.serializer()}.descriptor)")
          }
        }
      }
      line()
      block("override fun deserialize(decoder: Decoder): ${union.simpleName} {") {
        +"val json = decoder.decodeSerializableValue(JsonElement.serializer())"
        +"return attemptDeserialize(json,"
        union.schemas.indented(separator = ",\n") {
          val className = it.oneOfName()
          append("Pair(Case$className::class) { Case$className(Json.decodeFromJsonElement(${it.serializer()}, json)) }")
        }
        +")"
      }
      line()
      block("override fun serialize(encoder: Encoder, value: ${union.simpleName}) {") {
        +"when(value) {"
        union.schemas.indented {
          append("is Case${it.typeName()} -> encoder.encodeSerializableValue(${it.serializer()}, value.value)")
        }
        +"}"
      }
    }
  }
}

private fun KModel.serializer(): String =
  when (this) {
    is KModel.Collection.List -> "ListSerializer(${value.serializer()})"
    is KModel.Collection.Map -> "MapSerializer(${key.serializer()}, ${value.serializer()})"
    is KModel.Collection.Set -> "SetSerializer(${value.serializer()})"
    is KModel.Primitive -> "$name.serializer()"
    is KModel.Enum -> "$simpleName.serializer()"
    is KModel.Object -> "$simpleName.serializer()"
    is KModel.Union -> "$simpleName.serializer()"
    KModel.Binary -> TODO("Cannot serializer File?")
    KModel.JsonObject -> "JsonElement.serializer()"
  }

private fun KModel.oneOfName(depth: List<KModel> = emptyList()): String =
  when (this) {
    is KModel.Collection.List -> value.oneOfName(depth + listOf(this))
    is KModel.Collection.Map -> value.oneOfName(depth + listOf(this))
    is KModel.Collection.Set -> value.oneOfName(depth + listOf(this))
    else -> {
      val head = depth.firstOrNull()
      val s = when (head) {
        is KModel.Collection.List -> "s"
        is KModel.Collection.Set -> "s"
        is KModel.Collection.Map -> "Map"
        else -> ""
      }
      val postfix = depth.drop(1).joinToString(separator = "") {
        when (it) {
          is KModel.Collection.List -> "List"
          is KModel.Collection.Map -> "Map"
          is KModel.Collection.Set -> "Set"
          else -> ""
        }
      }
      val typeName = when (this) {
        is KModel.Collection.List -> "List"
        is KModel.Collection.Map -> "Map"
        is KModel.Collection.Set -> "Set"
        else -> typeName()
      }
      "$typeName${s}$postfix"
    }
  }
