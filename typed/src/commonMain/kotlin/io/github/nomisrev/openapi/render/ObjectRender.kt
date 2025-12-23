package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import kotlin.text.isBlank
import kotlin.text.lineSequence

context(ctx: Renderer)
fun Model.Object.render(
    parentClass: TypeName.Class? = null,
    baseProperties: Set<String> = emptySet()
): String = buildString {
    import(properties.map { (_, prop) -> prop.model })

    if (needsSerializer()) {
        ctx.import(TypeName.ExperimentalSerializationApi, TypeName.KeepGeneratedSerializer)
        +"@OptIn(ExperimentalSerializationApi::class)"
        +"@KeepGeneratedSerializer"
        serializable(this@render)
    } else {
        serializable()
    }
    when (properties.size) {
        0 -> append("data object ${name().simpleName}${parentClass.renderAsSuperclass()}")
        1 if additionalProperties is Model.Object.AdditionalProperties.Allowed && !additionalProperties.value ->
            valueClass(parentClass, baseProperties)

        else -> dataClass(parentClass, baseProperties)
    }
    body()
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.valueClass(parentClass: TypeName.Class?, baseProperties: Set<String>) {
    if (ctx.jvm) jvmInline()
    append(
        "value class ${name().simpleName}(${
            properties.entries.single().render(baseProperties)
        })${parentClass.renderAsSuperclass()}"
    )
}


context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.dataClass(parentClass: TypeName.Class?, baseProperties: Set<String>) {
    val simpleName = name().simpleName
    val additionalLine = when (additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed -> if (additionalProperties.value) ", additional: JsonObject? = null" else ""
        is Model.Object.AdditionalProperties.Schema -> ""
    }
    val line =
        "data class $simpleName(${properties.joinToString { it.render(baseProperties) }}$additionalLine)${parentClass.renderAsSuperclass()}"
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class $simpleName("
        indented {
            properties.joinTo(separator = ",\n", postfix = ",\n") { it.render(baseProperties) }
            when (additionalProperties) {
                is Model.Object.AdditionalProperties.Schema -> TODO()
                is Model.Object.AdditionalProperties.Allowed ->
                    if (additionalProperties.value) +"val additional: JsonObject? = null," else Unit
            }
        }
        append(")${parentClass.renderAsSuperclass()}")
    }
}

private fun Model.hasDefault(): Boolean = when (this) {
    is Model.Enum -> default != null
    is Model.Collection -> default != null
    is Model.Primitive.Boolean -> default != null
    is Model.Primitive.Double -> default != null
    is Model.Primitive.Float -> default != null
    is Model.Primitive.Int -> default != null
    is Model.Primitive.Long -> default != null
    is Model.Primitive.String -> default != null
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive.Unit,
    is Model.Reference,
    is Model.Union,
    is Model.Uuid,
    is Model.DiscriminatedObject -> false
}

context(ctx: Renderer)
fun Map.Entry<String, Model.Object.Property>.render(
    baseProperties: Set<String>,
    defaultValue: Boolean = true
): String = render(key, value, baseProperties, defaultValue)

context(ctx: Renderer)
fun render(
    baseName: String,
    prop: Model.Object.Property,
    baseProperties: Set<String>,
    defaultValue: Boolean = true
): String = buildString {
    val paramName = baseName.toParamName()

    if (paramName != baseName) {
        ctx.import(TypeName.SerialName)
        append("@SerialName(${baseName.stringValue()}) ")
    }

    val hasDefault = prop.model.hasDefault()
    if (prop.isRequired && hasDefault) {
        ctx.import(TypeName.Required)
        append("@Required ")
    }

    if (baseName in baseProperties) append("override ")

    append("val $paramName: ${prop.model.toTypeName().type()}")

    if (prop.model.isNullable || !prop.isRequired) append("?")

    when {
        !defaultValue -> {}
        prop.model.isNullable && prop.isRequired && !hasDefault -> {}
        prop.model.isNullable || !prop.isRequired -> append(" = null")
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.body() {
    if (inline.isNotEmpty()) {
        +" {"
        indented {
            inline.joinTo(separator = "\n\n", postfix = "\n") { it.render() }
            serializer()
        }
        append("}")
    }
}

private fun Model.Object.needsSerializer(): Boolean = when (additionalProperties) {
    is Model.Object.AdditionalProperties.Allowed -> additionalProperties.value
    is Model.Object.AdditionalProperties.Schema -> true
}

context(ctx: Renderer, builder: StringBuilder)
fun indented(block: StringBuilder.() -> Unit) {
    val string = buildString(block)
    string.lineSequence().joinTo(builder, "\n") {
        when {
            it.isBlank() -> it
            else -> ctx.indent + it
        }
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Object.serializer() = if (needsSerializer()) {
    newLine()
    +"companion object Serializer : KSerializer<${name().simpleName}> {"
    indented {
        +"override val descriptor: SerialDescriptor = generatedSerializer().descriptor"
        newLine()
        +"override fun serialize(encoder: Encoder, value: ${name().simpleName}) {"
        indented {
            +"val json = (encoder as JsonEncoder).json"
            +"return encoder.encodeSerializableValue("
            indented {
                +"JsonObject.serializer(),"
                +"buildJsonObject {"
                indented {
                    properties.forEach { (name, prop) ->
                        +"put(\"$name\", json.encodeToJsonElement(${prop.model.serializer()}, value.$name))"
                    }

                    when (additionalProperties) {
                        is Model.Object.AdditionalProperties.Allowed if additionalProperties.value -> +"putAll(value.additional)"
                        is Model.Object.AdditionalProperties.Allowed -> {}
                        is Model.Object.AdditionalProperties.Schema -> TODO()
                    }

                }
                +"})"
            }
        }
        +"}"
        newLine()
        +"override fun deserialize(decoder: Decoder): ${name().simpleName} {"
        indented {
            +"val json = (decoder as JsonDecoder).json"
            +"val element = decoder.decodeSerializableValue(JsonObject.serializer())"
            append("val names = ")
            properties.keys.joinTo(prefix = "setOf(", postfix = ")\n") { "\"$it\"" }
            +"require(element.keys.containsAll(names)) { \"Missing required properties: \${names - element.keys}\" }"
            +"return ${name().simpleName}("
            indented {
                properties.joinTo(separator = ",\n", postfix = ",\n") { (name, prop) ->
                    if (!prop.isRequired) {
                        "$name = if(element.containsKey(\"$name\")) json.decodeFromJsonElement(${prop.model.serializer()}, element[\"$name\"]!!) else null"
                    } else {
                        "$name = json.decodeFromJsonElement(${prop.model.serializer()}, element[\"$name\"]!!)"
                    }
                }
                when (additionalProperties) {
                    is Model.Object.AdditionalProperties.Allowed if additionalProperties.value ->
                        +"additional = JsonObject(element - names).ifEmpty { null }"

                    is Model.Object.AdditionalProperties.Allowed -> {}
                    is Model.Object.AdditionalProperties.Schema -> TODO()
                }
            }
            +")"
        }
        +"}"
    }
} else Unit

context(ctx: Renderer)
fun Model.serializer(): String = when (this) {
    is Model.Primitive -> {
        ctx.import(Import.serializer)
        when (this) {
            is Model.Primitive.Boolean -> "Boolean.serializer()"
            is Model.Primitive.Double -> "Double.serializer()"
            is Model.Primitive.Float -> "Float.serializer()"
            is Model.Primitive.Int -> "Int.serializer()"
            is Model.Primitive.Long -> "Long.serializer()"
            is Model.Primitive.String -> "String.serializer()"
            is Model.Primitive.Unit -> "Unit.serializer()"
        }
    }

    is Model.Uuid -> "Uuid.serializer()"
    is Model.Date -> "LocalDate.serializer()"
    is Model.DateTime -> "LocalDateTime.serializer()"
    is Model.FreeFormJson -> "JsonElement.serializer()"

    is Model.ByteArray -> {
        ctx.import(TopLevelFunction("kotlinx.serialization.builtins", "ByteArraySerializer"))
        "ByteArraySerializer()"
    }

    is Model.Collection if inner is Model.FreeFormJson -> "JsonArray.serializer()"
    is Model.Collection -> {
        ctx.import(TopLevelFunction("kotlinx.serialization.builtins", "ListSerializer"))
        "ListSerializer(${inner.serializer()})"
    }

    is Model.Object if properties.isEmpty() && additionalProperties is Allowed && additionalProperties.value -> "JsonObject.serializer()"
    is Model.Object if properties.isEmpty() && additionalProperties is Schema -> additionalProperties.value.serializer()

    is Model.ContextHolder -> "${name().simpleName}.serializer()"
}.let { serializer ->
    if (isNullable) {
        ctx.import(Import.nullable)
        "$serializer.nullable"
    } else serializer
}

// TODO: Wip
context(ctx: Renderer)
suspend fun Constraints.Number.requirements(
    name: String
) {
    val min = if (exclusiveMinimum == true) "<" else "<="
    val max = if (exclusiveMaximum == true) "<" else "<="
    val rangeCheck = "$minimum $min $name $maximum $max"
    val rangeMessage = "$name must be $minimum $min $name $maximum $max"
    val multipleOf = "$name % $multipleOf == 0"
    val multipleOfMessage = "$name must be a multiple of $multipleOf"
}
