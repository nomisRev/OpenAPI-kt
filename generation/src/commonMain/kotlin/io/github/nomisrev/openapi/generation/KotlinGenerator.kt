package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.NamingContext

public tailrec fun Templating.toPropertyCode(
  model: Model,
  naming: NamingStrategy = DefaultNamingStrategy
): Unit =
  when (model) {
    is Model.Binary -> Unit
    is Model.FreeFormJson -> Unit
    is Model.Primitive -> Unit
    is Collection -> toPropertyCode(model.value, naming)
    is Model.Enum -> toEnumCode(model, naming)
    is Model.Object -> toObjectCode(model, naming)
    is Model.Union -> {
      val isOpenEnumeration = model.isOpenEnumeration()
      if (isOpenEnumeration) toOpenEnumCode(model, naming)
      else toUnionCode(model, naming)
    }
  }

fun Templating.toOpenEnumCode(model: Model.Union, naming: NamingStrategy) {
  openEnumImports()
  val enum = model.schemas.firstNotNullOf { it.model as? Model.Enum }
  val rawToName = enum.values.map { rawName -> Pair(rawName, naming.toEnumValueName(rawName)) }
  val enumName = naming.toEnumClassName(model.context)
  Serializable()
  block("sealed interface $enumName {") {
    line("val value: String")
    line()
    rawToName.joinTo { (rawName, valueName) ->
      block("data object $valueName : $enumName {") {
        line("override val value: String = \"$rawName\"")
        line("override fun toString(): String = \"$rawName\"")
      }
    }
    line()
    line("@JvmInline")
    line("value class Custom(override val value: String) : $enumName")
    line()
    block("companion object {") {
      block("val defined: List<$enumName> =", closeAfter = false) {
        block("listOf(", closeAfter = false) {
          rawToName.indented(separator = ",\n") { (_, valueName) ->
            append(valueName)
          }
          line(")")
        }
      }
    }
    line()
    block("object Serializer : KSerializer<$enumName> {") {
      line("override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(\"$enumName\", PrimitiveKind.STRING)")
      line()
      block("override fun serialize(encoder: Encoder, value: $enumName) {") {
        line("encoder.encodeString(value.value)")
      }
      line()
      block("override fun deserialize(decoder: Decoder): $enumName {") {
        +"val value = decoder.decodeString()"
        +"return attemptDeserialize(value,"
        rawToName.indented(separator = ",\n", postfix = ",\n") { (raw, name) ->
          append("Pair($name::class) { defined.find { it.value == value } }")
        }
        line("  Pair(Custom::class) { Custom(value) }")
        +")"
      }
    }
  }
}

public fun Templating.Serializable() {
  addImport("kotlinx.serialization.Serializable")
  +"@Serializable"
}

public fun Templating.toEnumCode(enum: Model.Enum, naming: NamingStrategy) {
  val rawToName = enum.values.map { rawName -> Pair(rawName, naming.toEnumValueName(rawName)) }
  val isSimple = rawToName.all { (rawName, valueName) -> rawName == valueName }
  val constructor = if (isSimple) "" else "(val value: ${naming.typeName(enum.inner)})"
  if (!isSimple) addImport("kotlinx.serialization.SerialName")
  val enumName = naming.toEnumClassName(enum.context)
  Serializable()
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
    addImports(p.type)
  }

public tailrec fun Templating.addImports(model: Model): Boolean =
  when (model) {
//      is Model.Binary -> addImport("io.FileUpload")
    is Model.FreeFormJson ->
      addImport("kotlinx.serialization.json.JsonElement")

    is Collection -> addImports(model.value)
    else -> false
  }

public fun Templating.toObjectCode(obj: Model.Object, naming: NamingStrategy) {
  fun properties() =
    obj.properties.indented(separator = ",\n") { append(it.toPropertyCode(obj.context, naming)) }

  fun nested() =
    indented {
      obj.inline.indented(prefix = " {\n", postfix = "\n}") {
        toPropertyCode(it, naming)
      }
    }

//  description(obj)
  addImports(obj)
  Serializable()
  +"data class ${naming.toObjectClassName(obj.context)}("
  properties()
  append(")")
  nested()
}

fun Model.default(naming: NamingStrategy): String? = when (this) {
  Model.Primitive.Unit -> "Unit"
  is Collection.List ->
    default?.joinToString(prefix = "listOf(", postfix = ")") {
      if (value is Model.Enum) {
        "${naming.toEnumClassName(value.context)}.${naming.toEnumValueName(it)}"
      } else it
    }

  is Collection.Set ->
    default?.joinToString(prefix = "setOf(", postfix = ")") {
      if (value is Model.Enum) {
        "${naming.toEnumClassName(value.context)}.${naming.toEnumValueName(it)}"
      } else it
    }

  is Model.Enum ->
    (default ?: values.singleOrNull())?.let {
      "${naming.toEnumClassName(context)}.${naming.toEnumValueName(it)}"
    }

  is Model.Primitive -> default()
  is Model.Union.AnyOf -> when {
    default == null -> null
    isOpenEnumeration() -> {
      val case = schemas.firstNotNullOf { it.model as? Model.Enum }
      val defaultEnum = case.values.find { it == default }
        ?.let { naming.toEnumClassName(case.context) }
      defaultEnum ?: "Custom(\"${default}\")"
    }

    else -> schemas.find { it.model is Model.Primitive.String }
      ?.let { case ->
        "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")"
      }
  }

  is Model.Union.OneOf ->
    schemas.find { it.model is Model.Primitive.String }
      ?.takeIf { default != null }
      ?.let { case -> "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")" }

  else -> null
}

public fun Model.Object.Property.toPropertyCode(context: NamingContext, naming: NamingStrategy): String {
  val nullable = if (isNullable) "?" else ""
  val default = type.default(naming)?.let { " = $it" } ?: ""
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

public fun Templating.toUnionCode(union: Model.Union, naming: NamingStrategy) {
  unionImports()
  val unionClassName = naming.typeName(union)
  +"@Serializable(with = $unionClassName.Serializer::class)"
  block("sealed interface $unionClassName {") {
    union.schemas.joinTo { (_, model) ->
      +"@JvmInline"
      +"value class ${naming.toUnionCaseName(model)}(val value: ${naming.typeName(model)}): $unionClassName"
    }

    union.inline.indented { toPropertyCode(it, naming) }

    block("object Serializer : KSerializer<$unionClassName> {") {
      +"@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)"
      descriptor(unionClassName, union, naming)
      line()
      deserializer(unionClassName, union, naming)
      line()
      serializer(unionClassName, union, naming)
    }
  }
}

private fun Templating.descriptor(
  unionClassName: String,
  union: Model.Union,
  naming: NamingStrategy
) {
  expression("override val descriptor: SerialDescriptor =") {
    block("buildSerialDescriptor(\"$unionClassName\", PolymorphicKind.SEALED) {") {
      union.schemas.indented { (ctx, model) ->
        append(
          "element(\"${naming.toUnionCaseName(model)}\", ${
            serializer(
              model,
              naming
            )
          }.descriptor)"
        )
      }
    }
  }
}

private fun Templating.serializer(
  unionClassName: String,
  union: Model.Union,
  naming: NamingStrategy
) {
  block("override fun serialize(encoder: Encoder, value: $unionClassName) {") {
    +"when(value) {"
    union.schemas.indented { (ctx, model) ->
      val caseName = naming.toUnionCaseName(model)
      val serializer = serializer(model, naming)
      append("is $caseName -> encoder.encodeSerializableValue($serializer, value.value)")
    }
    +"}"
  }
}

private fun Templating.deserializer(
  unionClassName: String,
  union: Model.Union,
  naming: NamingStrategy
) {
  block("override fun deserialize(decoder: Decoder): $unionClassName {") {
    +"val json = decoder.decodeSerializableValue(JsonElement.serializer())"
    +"return attemptDeserialize(json,"
    union.schemas.indented(separator = ",\n") { (ctx, model) ->
      val caseName = naming.toUnionCaseName(model)
      val serializer = serializer(model, naming)
      append(
        "Pair($caseName::class) { $caseName(Json.decodeFromJsonElement($serializer, json)) }"
      )
    }
    +")"
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

public fun Templating.openEnumImports() {
  addImports(
    "kotlin.jvm.JvmInline",
    "kotlinx.serialization.Serializable",
    "kotlinx.serialization.KSerializer",
    "kotlinx.serialization.descriptors.PrimitiveKind",
    "kotlinx.serialization.descriptors.SerialDescriptor",
    "kotlinx.serialization.descriptors.PrimitiveSerialDescriptor",
    "kotlinx.serialization.encoding.Decoder",
    "kotlinx.serialization.encoding.Encoder",
    "kotlinx.serialization.json.Json",
    "kotlinx.serialization.json.JsonElement",
  )
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
