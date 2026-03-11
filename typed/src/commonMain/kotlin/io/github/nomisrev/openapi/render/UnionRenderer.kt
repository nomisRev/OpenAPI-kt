package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName.Companion.ExperimentalSerializationApi
import io.github.nomisrev.openapi.render.TypeName.Companion.InternalSerializationApi
import io.github.nomisrev.openapi.render.TypeName.Companion.PolymorphicKind
import io.github.nomisrev.openapi.transformers.isTopLevel
import io.github.nomisrev.openapi.transformers.nestedOrNull

context(ctx: Renderer)
fun Model.Union.render(): String = buildString {
    if (discriminator != null) {
        jsonClassDiscriminator(discriminator)
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
private fun Model.Union.Case.unionClassName(): String =
    (discriminator() ?: when (model) {
        is Model.ByteArray -> "CaseBinary"
        is Model.Date -> "CaseDate"
        is Model.DateTime -> "CaseDateTime"
        is Model.FreeFormJson -> "CaseJsonElement"
        is Model.Primitive.Boolean -> "CaseBoolean"
        is Model.Primitive.Double -> "CaseDouble"
        is Model.Primitive.Float -> "CaseFloat"
        is Model.Primitive.Long -> "CaseLong"
        is Model.Collection -> {
            var depth = 1
            var inner = model.inner
            while (inner is Model.Collection) {
                inner = inner.inner
                depth++
            }
            val name = copy(model = inner).unionClassName()

            val postfix =
                if (name.endsWith("s")) (0..depth).joinToString(separator = "") { "List" }
                else {
                    val postfix = (0..depth - 2).joinToString(separator = "") { "List" }
                    "s$postfix"
                }

            "$name$postfix"
        }

        is Model.Object if model.context.isTopLevel() -> "Case${model.name().simpleName}"
        is Model.Enum if model.context.isTopLevel() -> "Case${model.name().simpleName}"
        is Model.Enum -> {
            val unionCaseCtx = model.context.nested.last()
            require(unionCaseCtx is NamingContext.UnionCase)
            unionCaseCtx.value
        }

        is Model.Object -> {
            val unionCaseCtx = model.context.nested.last()
            require(unionCaseCtx is NamingContext.UnionCase)
            unionCaseCtx.value
        }

        is Model.Primitive.Int -> "CaseInt"
        is Model.Primitive.String -> "CaseString"
        is Model.Primitive.Unit -> "CaseUnit"
        is Model.Reference -> "Case${model.name().simpleName}"
        is Model.DiscriminatedObject -> "Case${model.name().simpleName}"
        is Model.Union -> "Case${model.name().simpleName}"
        is Model.Uuid -> "CaseUuid"
    }).toPascalCase()

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.valueClass(): String {
    val typeName = model.toTypeName()
    import(model)

    return buildString {
        serializable()
        if (ctx.jvm) jvmInline()
        val nullable = if (model.isNullable) "?" else ""
        append("value class ${unionClassName()}(val value: ${typeName.type()}$nullable) : ${union.name().simpleName}")
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
        ctx.import(TypeName.KSerializer, TypeName.SerialDescriptor, TypeName.Encoder, TypeName.Decoder)
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
                ctx.import(
                    ExperimentalSerializationApi,
                    InternalSerializationApi,
                    PolymorphicKind,
                    Import.buildSerialDescriptor
                )

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
                    +"return json.attemptDeserialize("
                    indented {
                        +"value,"
                        cases.sortedWith(unionCaseComparator).joinTo(separator = ",\n", postfix = ",\n") { case ->
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

        is Model.DiscriminatedObject,
        is Model.Union -> valueClass()
        is Model.Collection -> valueClass()

        // need to be generated with `unionNameCase()`
        is Model.Object -> model.render(union.name())
        is Model.Enum -> model.render(union.name())
    }

context(ctx: Renderer, union: Model.Union)
private fun Model.Union.Case.renderDeserializeAttempt(): String =
    when (model) {
        is Model.ContextHolder if model.context.isTopLevel() ->
            "${unionClassName()}::class to { ${unionClassName()}(decodeFromJsonElement(${model.serializer()}, it)) }"

        is Model.Primitive,
        is Model.DateTime,
        is Model.ByteArray,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Date,
        is Model.Collection,
        is Model.Reference ->
            "${unionClassName()}::class to { ${unionClassName()}(decodeFromJsonElement(${model.serializer()}, it)) }"

        is Model.DiscriminatedObject,
        is Model.Union ->
            "${unionClassName()}::class to { ${unionClassName()}(decodeFromJsonElement(${model.serializer()}, it)) }"

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

        is Model.DiscriminatedObject,
        is Model.Union ->
            "encoder.encodeSerializableValue(${model.serializer()}, value.value)"

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

/**
 * Comparator for ordering union cases to ensure proper deserialization.
 *
 * The ordering prevents "wider" types from swallowing "narrow" types:
 * 1. Objects without additional properties (more properties first to prevent overlapping schemas issues)
 * 2. Objects with additional properties schema (typed additional props, more properties first)
 * 3. Objects with additional properties allowed (swallow extra keys, more properties first)
 * 4. DiscriminatedObjects (inheritance-based, should be tried early)
 * 5. Nested Unions (tricky, should be tried early)
 * 6. Enums (before primitives or they get swallowed by String)
 * 7. Collections/Arrays (don't conflict with other types)
 * 8. Primitives in order: Int → Long → Float → Double → Boolean (narrower numeric types first)
 * 9. String-like types: Uuid, Date, DateTime, ByteArray (before String or get swallowed)
 * 10. String (swallows other string-representable types)
 * 11. FreeFormJson (dead last - swallows everything)
 */
val unionCaseComparator = Comparator<Model.Union.Case> { case1, case2 ->
    fun Model.Object.additionalPropertiesPriority(): Int = when (additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed -> if (additionalProperties.value) 2 else 0
        is Model.Object.AdditionalProperties.Schema -> 1
    }

    fun Model.priority(): Int = when (this) {
        // Objects: lower priority number = tried first
        // Objects without additional properties come first, sorted by property count (more = first)
        is Model.Object -> additionalPropertiesPriority() * 1000 - properties.size

        // DiscriminatedObjects should be tried early (they have discriminator field)
        is Model.DiscriminatedObject -> 3000

        // Nested unions are tricky, try early
        is Model.Union -> 4000

        // Enums before primitives (or String swallows them)
        is Model.Enum -> 5000

        // Collections don't conflict
        is Model.Collection -> 6000

        // References - depends on what they reference, treat as medium priority
        is Model.Reference -> 7000

        // Primitives in order: narrower numeric types first
        is Model.Primitive.Int -> 8000
        is Model.Primitive.Long -> 8100
        is Model.Primitive.Float -> 8200
        is Model.Primitive.Double -> 8300
        is Model.Primitive.Boolean -> 8400
        is Model.Primitive.Unit -> 8500

        // String-like types before String (or get swallowed)
        is Model.Uuid -> 9000
        is Model.Date -> 9100
        is Model.DateTime -> 9200
        is Model.ByteArray -> 9300

        // String swallows other string types
        is Model.Primitive.String -> 10000

        // FreeFormJson is dead last - swallows everything
        is Model.FreeFormJson -> Int.MAX_VALUE
    }

    case1.model.priority().compareTo(case2.model.priority())
}
