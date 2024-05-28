package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Model.Object.Property.DefaultArgument
import io.github.nomisrev.openapi.NamingContext

public tailrec fun Templating.toCode(
  model: Model,
  naming: NamingStrategy = DefaultNamingStrategy
): Unit =
  when (model) {
    is Model.Binary -> Unit
    is Model.FreeFormJson -> Unit
    is Model.Primitive -> Unit
    is Collection -> toCode(model.value, naming)
    is Model.Enum -> toCode(model, naming)
    is Model.Object -> toCode(model, naming)
    is Model.Union -> toCode(model, naming)
  }

public fun Templating.Serializable() {
  addImport("kotlinx.serialization.Serializable")
  +"@Serializable"
}

public fun Templating.toCode(enum: Model.Enum, naming: NamingStrategy) {
  Serializable()
  val rawToName = enum.values.map { rawName -> Pair(rawName, naming.toEnumValueName(rawName)) }
  val isSimple = rawToName.all { (rawName, valueName) -> rawName == valueName }
  val constructor = if (isSimple) "" else "(val value: ${naming.typeName(enum.inner)})"
  if (!isSimple) addImport("kotlinx.serialization.SerialName")
  val enumName = naming.toEnumClassName(enum.context)
  block("enum class $enumName$constructor {") {
    rawToName.indented(separator = ",\n", postfix = ";\n") { (rawName, valueName) ->
      if (isSimple) append(rawName)
      else append("@SerialName(\"${rawName}\") $valueName(\"${rawName}\")")
    }
  }
}

// TODO fix indentation, and multiline (param) descriptions.
public fun Templating.description(obj: Model.Object) {
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

public fun Templating.addImports(obj: Model.Object): Unit =
  obj.properties.forEach { p ->
    when (p.type) {
//      is Model.Binary -> addImport("io.FileUpload")
      is Model.FreeFormJson -> addImport("kotlinx.serialization.json.JsonElement")
      else -> Unit
    }
  }

/* Filter models that don't need to be generated */
private fun List<Model>.filterModels(): List<Model> =
  mapNotNull { model ->
    when (model) {
      is Model.Binary -> null
      is Model.FreeFormJson -> null
      is Model.Primitive -> null
      is Collection -> model
      is Model.Enum -> model
      is Model.Object -> model
      is Model.Union -> model
    }
  }

public fun Templating.toCode(obj: Model.Object, naming: NamingStrategy) {
  fun properties() =
    obj.properties.indented(separator = ",\n") { append(it.toCode(obj.context, naming)) }

  fun nested() =
    indented { obj.inline.filterModels().indented(prefix = " {\n", postfix = "\n}") { toCode(it, naming) } }

//  description(obj)
  addImports(obj)
  Serializable()
  +"data class ${naming.toObjectClassName(obj.context)}("
  properties()
  append(")")
  nested()
}

public fun Model.Object.Property.defaultValue(context: NamingContext, naming: NamingStrategy): String {
  val defaultValue = when (default) {
    is DefaultArgument.Enum ->
      "${naming.toEnumClassName(default.context)}.${naming.toEnumValueName(default.value)}"

    is DefaultArgument.Other -> if (default.value == "[]") "emptyList()" else default.value
    is DefaultArgument.Union -> {
      val unionName = naming.toUnionClassName(default.union)
      val caseName = naming.toUnionCaseName(context, default.case)
      "$unionName.$caseName(\"${default.value}\")"
    }
    is DefaultArgument.Double -> default.value.toString()
    is DefaultArgument.Int -> default.value.toString()
    is DefaultArgument.List -> default.value.joinToString(prefix = "listOf(", postfix = ")")
    null -> null
  }
  return defaultValue?.let { " = $it" } ?: ""
}

public fun Model.Object.Property.toCode(context: NamingContext, naming: NamingStrategy): String {
  val nullable = if (isNullable) "?" else ""
  val default = defaultValue(context, naming)
  val paramName = naming.toParamName(context, name)
  val typeName = naming.typeName(type)
  // TODO update defaultValue
  //   nullable + required + default = default
  //   non-nullable + required + default = default
  //   non-nullable + non-required = default
  //   nullable + non-required + default = null
  //   ==> This allows KotlinX `encodeDefaults = true` to safe on data
  return "val $paramName: $typeName$nullable$default"
}

public fun Templating.toCode(union: Model.Union, naming: NamingStrategy) {
  unionImports()
  val unionClassName = naming.typeName(union)
  +"@Serializable(with = $unionClassName.Serializer::class)"
  block("sealed interface $unionClassName {") {
    union.schemas.joinTo { (ctx, model) ->
      +"@JvmInline"
      +"value class ${naming.toUnionCaseName(ctx, model)}(val value: ${naming.typeName(model)}): $unionClassName"
    }

    union.inline.filterModels().indented { toCode(it, naming) }
    line()
    block("object Serializer : KSerializer<$unionClassName> {") {
      +"@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)"
      expression("override val descriptor: SerialDescriptor =") {
        block("buildSerialDescriptor(\"$unionClassName\", PolymorphicKind.SEALED) {") {
          union.schemas.indented { (ctx, model) ->
            append("element(\"${naming.toUnionCaseName(ctx, model)}\", ${serializer(model, naming)}.descriptor)")
          }
        }
      }
      line()
      block("override fun deserialize(decoder: Decoder): $unionClassName {") {
        +"val json = decoder.decodeSerializableValue(JsonElement.serializer())"
        +"return attemptDeserialize(json,"
        union.schemas.indented(separator = ",\n") { (ctx, model) ->
          val caseName = naming.toUnionCaseName(ctx, model)
          val serializer = serializer(model, naming)
          append(
            "Pair($caseName::class) { $caseName(Json.decodeFromJsonElement($serializer, json)) }"
          )
        }
        +")"
      }
      line()
      block("override fun serialize(encoder: Encoder, value: $unionClassName) {") {
        +"when(value) {"
        union.schemas.indented { (ctx, model) ->
          val caseName = naming.toUnionCaseName(ctx, model)
          val serializer = serializer(model, naming)
          append("is $caseName -> encoder.encodeSerializableValue($serializer, value.value)")
        }
        +"}"
      }
    }
  }
}

private fun Templating.serializer(model: Model, naming: NamingStrategy): String =
  when (model) {
    is Collection.List -> {
      addImport("kotlinx.serialization.builtins.ListSerializer")
      "ListSerializer(${serializer(model.value, naming)})"
    }

    is Collection.Map -> {
      addImport("kotlinx.serialization.builtins.MapSerializer")
      "MapSerializer(${serializer(model.key, naming)}, ${serializer(model.value, naming)})"
    }

    is Collection.Set -> {
      addImport("kotlinx.serialization.builtins.SetSerializer")
      "SetSerializer(${serializer(model.value, naming)})"
    }

    is Model.Primitive -> {
      addImport("kotlinx.serialization.builtins.serializer")
      "${naming.typeName(model)}.serializer()"
    }

    is Model.Enum -> "${naming.typeName(model)}.serializer()"
    is Model.Object -> "${naming.typeName(model)}.serializer()"
    is Model.Union -> "${naming.typeName(model)}.serializer()"
    is Model.Binary -> TODO("Cannot serializer File?")
    is Model.FreeFormJson -> {
      addImport("kotlinx.serialization.json.JsonElement")
      "JsonElement.serializer()"
    }
  }

public fun Templating.unionImports() {
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
