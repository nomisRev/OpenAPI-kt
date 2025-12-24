package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.render.TypeName.Companion.ExperimentalSerializationApi
import io.github.nomisrev.openapi.render.TypeName.Companion.InternalSerializationApi
import io.github.nomisrev.openapi.render.TypeName.Companion.PolymorphicKind
import io.github.nomisrev.openapi.render.openEnum
import io.github.nomisrev.openapi.transformers.isTopLevel
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.descriptors.PolymorphicKind

context(ctx: Renderer)
fun Model.Union.render(): String = buildString {
    if (discriminator != null) {
        ctx.import(TypeName.JsonClassDiscriminator)
        +"@JsonClassDiscriminator(${discriminator.stringValue()})"
        serializable()
    } else {
        serializable(this@render)
    }
    +"sealed interface ${name().simpleName} {"
    indented {
        body()
    }
    append("}")
}

context(union: Model.Union)
private fun Model.Union.Case.discriminator(): String? =
    if (union.discriminator != null) {
        requireNotNull(discriminator) { "Discriminator is required for union with discriminator" }
        discriminator
    } else discriminator

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.unionClassName(): String {
    val typeName = model.toTypeName()
    return discriminator()?.toPascalCase() ?: "Case${typeName.name()}"
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.valueClass(): String {
    val typeName = model.toTypeName()
    import(model)

    return buildString {
        serializable()
        if (ctx.jvm) jvmInline()
        append("value class ${unionClassName()}(val value: ${typeName.type()}) : ${union.name().simpleName}")
        when (val nestedOrNull = model.nestedOrNull()?.render()) {
            null -> {}
            else -> {
                append(" {\n")
                +nestedOrNull.prepend()
                append("}")
            }
        }
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun Model.Union.body() {
    cases.joinTo("\n") { case ->
        buildString {
            if (case.discriminator() != null) serialName(case.discriminator!!)
            +case.render()
        }
    }

    if (discriminator == null) {
        newLine()
        +"object Serializer : KSerializer<${name().simpleName}> {"
        indented {
            val isOpenEnum = cases.size == 2 &&
                    cases.singleOrNull { it.model is Model.Enum } != null &&
                    cases.singleOrNull { it.model is Model.Primitive.String } != null
            if (isOpenEnum) {
                openEnum(
                    cases.single { it.model is Model.Primitive.String },
                    cases.firstNotNullOf { it.model as? Model.Enum }
                )
            } else {
                ctx.import(ExperimentalSerializationApi, InternalSerializationApi, PolymorphicKind)
                +"@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)"
                +"override val descriptor: SerialDescriptor ="
                indented {
                    +"buildSerialDescriptor(\"${name().fqName}\", PolymorphicKind.SEALED) {"
                    indented {
                        cases.forEach { case ->
                            +"element(\"${case.unionClassName()}\", ${case.model.serializer()}.descriptor)"
                        }
                    }
                    +"}"
                }
                newLine()
                +"override fun deserialize(decoder: Decoder): ${name().simpleName} {"
                indented {
                    ctx.import(TypeName.JsonElement, TypeName.JsonDecoder)
                    +"val value = decoder.decodeSerializableValue(JsonElement.serializer())"
                    +"val json = requireNotNull(decoder as? JsonDecoder) { \"Complex unions currently only supported for Json\" }.json"
                    +"return attemptDeserialize("
                    indented {
                        +"value,"
                        cases.joinTo(separator = ",\n", postfix = ",\n") { case ->
                            case.renderDeserializeAttempt()
                        }
                    }
                    +")"
                }
                +"}"
                newLine()
                +"override fun serialize(encoder: Encoder, value: ${name().simpleName}) = when(value) {"
                indented {
                    cases.forEach { case ->
                        +"is ${case.unionClassName()} -> ${case.serialiseCase()}"
                    }
                }
                +"}"
            }
        }
        +"}"
    }
}



context(builder: StringBuilder, ctx: Renderer)
private fun Model.Union.openEnum(
    primitive: Model.Union.Case,
    enum: Model.Enum
) {
    +"override val descriptor: SerialDescriptor = ${primitive.model.serializer()}.descriptor"
    newLine()
    +"override fun serialize(encoder: Encoder, value: ${name().simpleName}) = when(value) {"
    indented {
        enum.valueNames().forEach { (raw, name) ->
            +"${enum.name().simpleName}.$name -> encoder.encodeString(\"$raw\")"
        }
        +"is ${primitive.unionClassName()} -> encoder.encodeString(value.value)"
    }
    +"}"
    newLine()
    +"override fun deserialize(decoder: Decoder): Union ="
    indented {
        +"when(val value = decoder.decodeString()) {"
        indented {
            enum.valueNames().forEach { (raw, name) ->
                +"\"$raw\" -> ${enum.name().simpleName}.$name"
            }
            +"else -> ${primitive.unionClassName()}(value)"
        }
        +"}"
    }
}

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.render(): String =
    when (model) {
        is Model.ContextHolder if model.context.isTopLevel() -> valueClass()
        is Model.Primitive.Unit -> """
                   |@Serializable
                   |data object Empty : ${union.name().simpleName}
                """.trimMargin()

        is Model.Reference -> valueClass()
        is Model.Primitive,
        is Model.DateTime,
        is Model.ByteArray,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Date -> valueClass()

        is Model.DiscriminatedObject -> TODO("Nested DiscriminatedObject not supported in Union")
        is Model.Union -> TODO("Inline defined nested Union not yet supported in Union")
        is Model.Collection -> valueClass()

        is Model.Object -> model.render(union.name())
        is Model.Enum -> model.render(union.name())
    }

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.renderDeserializeAttempt(): String =
    when (model) {
        is Model.ContextHolder if model.context.isTopLevel() ->
            "${unionClassName()} to { ${unionClassName()}(decodeFromJsonElement(${model.serializer()}, it)) }"

        is Model.Primitive,
        is Model.DateTime,
        is Model.ByteArray,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Date,
        is Model.Collection,
        is Model.Reference ->
            "${unionClassName()}::class to { ${unionClassName()}(decodeFromJsonElement(${model.serializer()}, it)) }"

        is Model.DiscriminatedObject -> TODO("Nested DiscriminatedObject not supported in Union")
        is Model.Union -> TODO("Inline defined nested Union not yet supported in Union")

        is Model.Primitive.Unit,
        is Model.Object,
        is Model.Enum ->
            "${unionClassName()}::class to { decodeFromJsonElement(${model.serializer()}, it) }"
    }

context(ctx: Renderer)
private fun Model.Union.Case.serialiseCase(): String =
    when (model) {
        is Model.ContextHolder if model.context.isTopLevel() ->
            "encoder.encodeSerializableValue(${model.serializer()}, value.value)"

        is Model.Date,
        is Model.DateTime,
        is Model.ByteArray,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Primitive,
        is Model.Collection,
        is Model.Reference ->
            "encoder.encodeSerializableValue(${model.serializer()}, value.value)"

        is Model.DiscriminatedObject -> TODO("Nested DiscriminatedObject not supported in Union")
        is Model.Union -> TODO("Inline defined nested Union not yet supported in Union")

        is Model.Primitive.Unit,
        is Model.Object,
        is Model.Enum -> "encoder.encodeSerializableValue(${model.serializer()}, value)"
    }

private fun TypeName.name(depth: Int = 0): String = when (this) {
    is TypeName.Collection -> type.name(depth + 1)
    is TypeName.Class -> {
        val postfix =
            if (depth == 0) ""
            else if (simpleName.endsWith("s")) (0..depth).joinToString(separator = "") { "List" }
            else {
                val postfix = (0..depth - 2).joinToString(separator = "") { "List" }
                "s$postfix"
            }
        simpleName + postfix
    }
}
