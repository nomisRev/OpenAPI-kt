package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.transformers.isTopLevel

context(ctx: Renderer)
fun Model.Object.render(
    parentClass: TypeName.Class? = null,
    baseProperties: Set<String> = emptySet()
): String = buildString {
    import(properties.map { (_, prop) -> prop.model })

    if (properties.any { it.value.model is Model.Uuid }) {
        experimentalUuidApi()
    }

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

context(ctx: Renderer)
fun Map.Entry<String, Model.Object.Property>.render(
    baseProperties: Set<String>,
    defaultValue: Boolean = true
): String = renderProperty(key, value, baseProperties, defaultValue)

// This is the other way around for Unions...
fun Model.isWrapped(): Boolean = when(this) {
    is Model.ContextHolder -> false
    else -> true
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

    val line = dataClassSingleLine(baseProperties, simpleName, parentClass)
    if (line.length <= ctx.maxLineLength) append(line)
    else {
        +"data class $simpleName("
        indented {
            properties.joinTo(separator = ",\n", postfix = ",\n") { it.render(baseProperties) }
            additionalProperty()?.let { +"$it," }
        }
        append(")${parentClass.renderAsSuperclass()}")
    }
}

context(ctx: Renderer)
private fun Model.Object.dataClassSingleLine(
    baseProperties: Set<String>,
    simpleName: String,
    parentClass: TypeName.Class?
): String {
    val additionalLine = additionalProperty()?.let { ", $it" } ?: ""
    val propertiesLine = "${properties.joinToString { it.render(baseProperties) }}$additionalLine"
    return "data class $simpleName($propertiesLine)${parentClass.renderAsSuperclass()}"
}

context(ctx: Renderer)
private fun Model.Object.additionalProperty(): String? =
    when (additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed -> if (additionalProperties.value) {
            ctx.import(TypeName.JsonObject)
            "val additional: JsonObject? = null"
        } else null

        is Model.Object.AdditionalProperties.Schema -> {
            import(additionalProperties.value)
            val typeName = additionalProperties.value.toTypeName().type()
            "val additional: Map<String, $typeName>? = null"
        }
    }

context(ctx: Renderer)
private fun renderProperty(
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
    if (inline.isNotEmpty() || needsSerializer()) {
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
private fun Model.Object.serializer() {
    if (!needsSerializer()) return
    newLine()
    ctx.import(
        TypeName.KSerializer,
        TypeName.SerialDescriptor,
        TypeName.Encoder,
        TypeName.Decoder,
        TypeName.JsonDecoder,
        TypeName.JsonEncoder,
        TypeName.JsonObject,
        Import.buildJsonObject
    )
    +"object Serializer : KSerializer<${name().simpleName}> {"
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
                    properties
                        .mapKeys { (name, _) -> name.toParamName() }
                        .forEach { (name, prop) ->
                            +"put(\"$name\", json.encodeToJsonElement(${prop.serializer()}, value.$name))"
                        }

                    when (additionalProperties) {
                        is Model.Object.AdditionalProperties.Allowed if additionalProperties.value -> {
                            ctx.import(TopLevelFunction.putAll())
                            +"putAll(value.additional)"
                        }
                        is Model.Object.AdditionalProperties.Allowed -> {}
                        is Model.Object.AdditionalProperties.Schema -> {
                            +"value.additional?.forEach { (key, value) ->"
                            +"${ctx.indent}put(key, json.encodeToJsonElement(${additionalProperties.value.serializer()}, value))"
                            +"}"
                        }
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
            +$$"""require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }"""
            +"return ${name().simpleName}("
            indented {
                properties
                    .mapKeys { (name, _) -> name.toParamName() }
                    .joinTo(separator = ",\n", postfix = ",\n") { (name, prop) ->
                        if (!prop.isRequired) {
                            "$name = if(element.containsKey(\"$name\")) json.decodeFromJsonElement(${prop.serializer()}, element[\"$name\"]!!) else null"
                        } else {
                            "$name = json.decodeFromJsonElement(${prop.serializer()}, element[\"$name\"]!!)"
                        }
                    }
                when (additionalProperties) {
                    is Model.Object.AdditionalProperties.Allowed if additionalProperties.value ->
                        +"additional = JsonObject(element - names).ifEmpty { null }"

                    is Model.Object.AdditionalProperties.Allowed -> {}
                    is Model.Object.AdditionalProperties.Schema -> {
                        +"additional = (element - names)"
                        +"${ctx.indent}.mapValues { (_, value) -> json.decodeFromJsonElement(${additionalProperties.value.serializer()}, value) }"
                    }
                }
            }
            +")"
        }
        +"}"
    }
    +"}"
}
