@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.Dynamic
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName
import kotlinx.serialization.ExperimentalSerializationApi

internal val UnionKSerializerType = ClassName("kotlinx.serialization", "KSerializer")
internal val UnionSerialDescriptorType = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
internal val UnionDecoderType = ClassName("kotlinx.serialization.encoding", "Decoder")
internal val UnionEncoderType = ClassName("kotlinx.serialization.encoding", "Encoder")
private val UnionJsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val UnionJsonDecoderType = ClassName("kotlinx.serialization.json", "JsonDecoder")
private val UnionJsonObjectType = ClassName("kotlinx.serialization.json", "JsonObject")
private val UnionJsonPrimitiveType = ClassName("kotlinx.serialization.json", "JsonPrimitive")
private val UnionPolymorphicKindType = ClassName("kotlinx.serialization.descriptors", "PolymorphicKind")
private val UnionBuildSerialDescriptorMember = MemberName("kotlinx.serialization.descriptors", "buildSerialDescriptor")
private val UnionSerializationExceptionType = ClassName("kotlinx.serialization", "SerializationException")

private const val OBJECT_WITH_ADDITIONAL_PROPERTIES_ALLOWED_PRIORITY = 200
private const val OBJECT_WITHOUT_ADDITIONAL_PROPERTIES_BASE_PRIORITY = 100
private const val OBJECT_WITHOUT_ADDITIONAL_PROPERTIES_MAX_PROPERTY_COUNT = 99
private const val OBJECT_WITH_TYPED_ADDITIONAL_PROPERTIES_PRIORITY = 150
private const val DISCRIMINATED_OBJECT_PRIORITY = 300
private const val UNION_PRIORITY = 400
private const val ENUM_PRIORITY = 500
private const val COLLECTION_PRIORITY = 600
private const val REFERENCE_PRIORITY = 700
private const val PRIMITIVE_INT_PRIORITY = 800
private const val PRIMITIVE_LONG_PRIORITY = 801
private const val PRIMITIVE_FLOAT_PRIORITY = 802
private const val PRIMITIVE_DOUBLE_PRIORITY = 803
private const val PRIMITIVE_BOOLEAN_PRIORITY = 804
private const val PRIMITIVE_UNIT_PRIORITY = 805
private const val UUID_PRIORITY = 900
private const val DATE_PRIORITY = 901
private const val DATE_TIME_PRIORITY = 902
private const val BYTE_ARRAY_PRIORITY = 903
private const val STRING_PRIORITY = 1000
private const val FREE_FORM_JSON_PRIORITY = 1100

internal fun Model.Union.buildSerializerObject(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    renderedCases: List<RenderedUnionCase>,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val declarationCases = cases.zip(renderedCases)
    val orderedCases = declarationCases.sortedByDeserializationOrder()
    val anyOfDispatchAnalysis = (this as? Model.AnyOf)?.uniqueKeyDispatchAnalysis()

    return TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(UnionKSerializerType.parameterizedBy(className))
        .addProperty(buildDescriptorProperty(config, originalClassName, className, declarationCases, externalTypeNames))
        .addFunction(
            buildDeserializeFunction(
                union = this,
                config = config,
                originalClassName = originalClassName,
                className = className,
                declarationCases = declarationCases,
                orderedCases = orderedCases,
                anyOfDispatchAnalysis = anyOfDispatchAnalysis,
                externalTypeNames = externalTypeNames,
            )
        )
        .addFunction(buildSerializeFunction(config, originalClassName, className, declarationCases, externalTypeNames))
        .build()
}

private fun buildDescriptorProperty(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    cases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): PropertySpec {
    val descriptorCode = CodeBlock.builder()
        .beginControlFlow(
            "%M(%S, %T.SEALED)",
            UnionBuildSerialDescriptorMember,
            className.canonicalName,
            UnionPolymorphicKindType,
        )
        .apply {
            cases.forEach { [case, rendered] ->
                addStatement(
                    "element(%S, %L.descriptor)",
                    rendered.caseSimpleName,
                    caseSerializerCode(case, rendered, config, originalClassName, className, externalTypeNames)
                )
            }
        }
        .endControlFlow()
        .build()

    return PropertySpec.builder("descriptor", UnionSerialDescriptorType)
        .addModifiers(KModifier.OVERRIDE)
        .addAnnotation(
            AnnotationSpec.builder(UnionOptInType)
                .addMember("%T::class", ClassName("kotlinx.serialization", "InternalSerializationApi"))
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        .initializer(descriptorCode)
        .build()
}

@Suppress("LongParameterList")
private fun buildDeserializeFunction(
    union: Model.Union,
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    declarationCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    anyOfDispatchAnalysis: AnyOfUniqueKeyDispatchAnalysis?,
    externalTypeNames: Map<ClassName, TypeName>,
): FunSpec {
    val code = when (val dispatch = union.dispatch) {
        is UnionDispatch.TaggedCustom ->
            buildTaggedCustomDeserializeCode(
                propertyName = dispatch.propertyName,
                config = config,
                originalClassName = originalClassName,
                className = className,
                declarationCases = declarationCases,
                externalTypeNames = externalTypeNames,
            )

        UnionDispatch.Structural ->
            if (anyOfDispatchAnalysis != null) {
                buildAnyOfDeserializeCode(
                    config = config,
                    originalClassName = originalClassName,
                    className = className,
                    orderedCases = orderedCases,
                    anyOfDispatchAnalysis = anyOfDispatchAnalysis,
                    externalTypeNames = externalTypeNames,
                )
            } else {
                buildAttemptDeserializeCode(config, originalClassName, className, orderedCases, externalTypeNames)
            }

        is UnionDispatch.NativeDiscriminator ->
            error("Custom serializer renderer does not support native discriminator dispatch: $dispatch")
    }

    return FunSpec.builder("deserialize")
        .addModifiers(KModifier.OVERRIDE)
        .returns(className)
        .addParameter("decoder", UnionDecoderType)
        .addCode(code)
        .build()
}

private fun buildAttemptDeserializeCode(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock = CodeBlock.builder()
    .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", UnionJsonElementType)
    .addStatement(
        "val json = requireNotNull(decoder as? %T) { %S }.json",
        UnionJsonDecoderType,
        "Complex unions currently only supported for Json"
    )
    .add(buildAttemptDeserializeBody(config, originalClassName, className, orderedCases, externalTypeNames))
    .build()

private fun buildAttemptDeserializeBody(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock = CodeBlock.builder()
    .add("return json.attemptDeserialize(\n")
    .indent()
    .addStatement("value,")
    .apply {
        orderedCases.forEach { [case, rendered] ->
            val caseClassName = className.nestedClass(rendered.caseSimpleName)
            val serializerCode = caseSerializerCode(case, rendered, config, originalClassName, className, externalTypeNames)
            if (rendered.isInlined || rendered.isNestedUnion) {
                addStatement(
                    "%T::class to { decodeFromJsonElement(%L, it) },",
                    caseClassName,
                    serializerCode,
                )
            } else {
                addStatement(
                    "%T::class to { %T(decodeFromJsonElement(%L, it)) },",
                    caseClassName,
                    caseClassName,
                    serializerCode,
                )
            }
        }
    }
    .unindent()
    .addStatement(")")
    .build()

@Suppress("LongMethod", "LongParameterList")
private fun buildTaggedCustomDeserializeCode(
    propertyName: String,
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    declarationCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock {
    val casesByTag = linkedMapOf<String, MutableList<Pair<Model.Union.Case, RenderedUnionCase>>>()
    val untaggedCases = mutableListOf<Pair<Model.Union.Case, RenderedUnionCase>>()

    declarationCases.forEach { declarationCase ->
        val discriminatorValues = declarationCase.first.discriminatorValues
        if (discriminatorValues.isEmpty()) {
            untaggedCases += declarationCase
        } else {
            discriminatorValues.forEach { tag ->
                casesByTag.getOrPut(tag) { mutableListOf() } += declarationCase
            }
        }
    }

    return CodeBlock.builder()
        .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", UnionJsonElementType)
        .addStatement(
            "val json = requireNotNull(decoder as? %T) { %S }.json",
            UnionJsonDecoderType,
            "Complex unions currently only supported for Json"
        )
        .addStatement("val obj = value as? %T", UnionJsonObjectType)
        .addStatement("val tag = (obj?.get(%S) as? %T)?.content", propertyName, UnionJsonPrimitiveType)
        .beginControlFlow("when(tag)")
        .apply {
            declarationCases.forEach { declarationCase ->
                val uniqueTags = declarationCase.first.discriminatorValues
                    .filter { tag -> casesByTag.getValue(tag).size == 1 }
                if (uniqueTags.isNotEmpty()) {
                    addStatement(
                        "%L -> return %L",
                        tagLabelsCode(uniqueTags),
                        decodeCaseExpression(
                            case = declarationCase.first,
                            rendered = declarationCase.second,
                            config = config,
                            originalClassName = originalClassName,
                            className = className,
                            externalTypeNames = externalTypeNames,
                        )
                    )
                }
            }

            casesByTag.forEach { [tag, collisionCases] ->
                if (collisionCases.size > 1) {
                    beginControlFlow("%S ->", tag)
                    add(
                        buildTaggedCustomCollisionDispatchCode(
                            config = config,
                            originalClassName = originalClassName,
                            className = className,
                            collisionCases = collisionCases,
                            externalTypeNames = externalTypeNames,
                        )
                    )
                    endControlFlow()
                }
            }

            if (untaggedCases.isNotEmpty()) {
                beginControlFlow("else ->")
                add(
                    buildAttemptDeserializeBody(
                        config = config,
                        originalClassName = originalClassName,
                        className = className,
                        orderedCases = untaggedCases.sortedByDeserializationOrder(),
                        externalTypeNames = externalTypeNames,
                    )
                )
                endControlFlow()
            } else {
                addStatement(
                    "else -> throw %T(%S + tag + %S)",
                    UnionSerializationExceptionType,
                    "Unknown tag: ",
                    " for ${className.canonicalName}",
                )
            }
        }
        .endControlFlow()
        .build()
}

private fun tagLabelsCode(tags: List<String>): CodeBlock =
    CodeBlock.builder()
        .apply {
            tags.forEachIndexed { index, tag ->
                if (index > 0) add(", ")
                add("%S", tag)
            }
        }
        .build()

@Suppress("LongParameterList")
private fun decodeCaseExpression(
    case: Model.Union.Case,
    rendered: RenderedUnionCase,
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock {
    val serializerCode = caseSerializerCode(case, rendered, config, originalClassName, className, externalTypeNames)
    return if (rendered.isInlined || rendered.isNestedUnion) {
        CodeBlock.of("json.decodeFromJsonElement(%L, value)", serializerCode)
    } else {
        CodeBlock.of(
            "%T(json.decodeFromJsonElement(%L, value))",
            className.nestedClass(rendered.caseSimpleName),
            serializerCode,
        )
    }
}

@Suppress("LongParameterList")
private fun buildTaggedCustomCollisionDispatchCode(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    collisionCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock {
    val orderedCollisionCases = collisionCases.sortedByDeserializationOrder()
    val anyOfDispatchAnalysis = collisionCases.map { it.first }.uniqueKeyDispatchAnalysisOrNull()

    if (anyOfDispatchAnalysis == null) {
        return buildAttemptDeserializeBody(
            config = config,
            originalClassName = originalClassName,
            className = className,
            orderedCases = orderedCollisionCases,
            externalTypeNames = externalTypeNames,
        )
    }

    val uniqueKeysByCase = orderedCollisionCases.map { [case, rendered] ->
        AnyOfDispatchCase(
            case = case,
            rendered = rendered,
            uniqueKeys = anyOfDispatchAnalysis.uniqueKeysFor(case),
        )
    }
    return buildAnyOfUniqueKeyCollisionDispatchCode(
        AnyOfUniqueKeyDispatchContext(
            config = config,
            originalClassName = originalClassName,
            className = className,
            orderedCases = orderedCollisionCases,
            uniqueKeysByCase = uniqueKeysByCase,
            externalTypeNames = externalTypeNames,
        )
    )
}

private fun buildAnyOfDeserializeCode(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    anyOfDispatchAnalysis: AnyOfUniqueKeyDispatchAnalysis,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock {
    val uniqueKeysByCase = orderedCases.map { [case, rendered] ->
        AnyOfDispatchCase(
            case = case,
            rendered = rendered,
            uniqueKeys = anyOfDispatchAnalysis.uniqueKeysFor(case),
        )
    }
    return buildAnyOfUniqueKeyDispatchCode(
        AnyOfUniqueKeyDispatchContext(
            config = config,
            originalClassName = originalClassName,
            className = className,
            orderedCases = orderedCases,
            uniqueKeysByCase = uniqueKeysByCase,
            externalTypeNames = externalTypeNames,
        )
    )
}

private data class AnyOfUniqueKeyDispatchContext(
    val config: RenderConfig,
    val originalClassName: ClassName,
    val className: ClassName,
    val orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    val uniqueKeysByCase: List<AnyOfDispatchCase>,
    val externalTypeNames: Map<ClassName, TypeName>,
)

private fun buildAnyOfUniqueKeyDispatchCode(
    context: AnyOfUniqueKeyDispatchContext,
): CodeBlock {
    val fallbackCase = context.uniqueKeysByCase.singleOrNull { it.uniqueKeys.isEmpty() }
    return CodeBlock.builder()
        .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", UnionJsonElementType)
        .addStatement(
            "val json = requireNotNull(decoder as? %T) { %S }.json",
            UnionJsonDecoderType,
            "Complex unions currently only supported for Json"
        )
        .addStatement("val keys = value.jsonObject.keys")
        .apply {
            context.uniqueKeysByCase.filter { it.uniqueKeys.isNotEmpty() }.forEach { dispatchCase ->
                val condition = dispatchCase.uniqueKeys.joinToString(separator = " || ") { "\"${it.ktStringLiteral()}\" in keys" }
                beginControlFlow("if ($condition)")
                addStatement(
                    "return %L",
                    decodeCaseExpression(
                        case = dispatchCase.case,
                        rendered = dispatchCase.rendered,
                        config = context.config,
                        originalClassName = context.originalClassName,
                        className = context.className,
                        externalTypeNames = context.externalTypeNames,
                    )
                    )
                endControlFlow()
            }

            if (fallbackCase != null) {
                addStatement(
                    "return %L",
                    decodeCaseExpression(
                        case = fallbackCase.case,
                        rendered = fallbackCase.rendered,
                        config = context.config,
                        originalClassName = context.originalClassName,
                        className = context.className,
                        externalTypeNames = context.externalTypeNames,
                    )
                )
            } else {
                add(
                    buildAttemptDeserializeBody(
                        context.config,
                        context.originalClassName,
                        context.className,
                        context.orderedCases,
                        context.externalTypeNames,
                    )
                )
            }
        }
        .build()
}

private fun buildAnyOfUniqueKeyCollisionDispatchCode(
    context: AnyOfUniqueKeyDispatchContext,
): CodeBlock {
    return CodeBlock.builder()
        .addStatement("val keys = obj?.keys.orEmpty()")
        .apply {
            context.uniqueKeysByCase.filter { it.uniqueKeys.isNotEmpty() }.forEach { dispatchCase ->
                val condition = dispatchCase.uniqueKeys.joinToString(separator = " || ") { "\"${it.ktStringLiteral()}\" in keys" }
                beginControlFlow("if ($condition)")
                addStatement(
                    "return %L",
                    decodeCaseExpression(
                        case = dispatchCase.case,
                        rendered = dispatchCase.rendered,
                        config = context.config,
                        originalClassName = context.originalClassName,
                        className = context.className,
                        externalTypeNames = context.externalTypeNames,
                    )
                    )
                endControlFlow()
            }

            add(
                buildAttemptDeserializeBody(
                    config = context.config,
                    originalClassName = context.originalClassName,
                    className = context.className,
                    orderedCases = context.orderedCases,
                    externalTypeNames = context.externalTypeNames,
                )
            )
        }
        .build()
}

private data class AnyOfDispatchCase(
    val case: Model.Union.Case,
    val rendered: RenderedUnionCase,
    val uniqueKeys: Set<String>,
)

private fun buildSerializeFunction(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    allCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): FunSpec {
    val code = CodeBlock.builder()
        .beginControlFlow("when(value)")
        .apply {
            allCases.forEach { [case, rendered] ->
                val caseClassName = className.nestedClass(rendered.caseSimpleName)
                val serializerCode = caseSerializerCode(case, rendered, config, originalClassName, className, externalTypeNames)
                if (rendered.isInlined || rendered.isNestedUnion) {
                    addStatement(
                        "is %T -> encoder.encodeSerializableValue(%L, value)",
                        caseClassName,
                        serializerCode,
                    )
                } else {
                    addStatement(
                        "is %T -> encoder.encodeSerializableValue(%L, value.value)",
                        caseClassName,
                        serializerCode,
                    )
                }
            }
        }
        .endControlFlow()
        .build()

    return FunSpec.builder("serialize")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter("encoder", UnionEncoderType)
        .addParameter("value", className)
        .addCode(code)
        .build()
}

internal fun List<Pair<Model.Union.Case, RenderedUnionCase>>.sortedByDeserializationOrder():
    List<Pair<Model.Union.Case, RenderedUnionCase>> = sortedBy { [case, _] ->
    case.model.deserializationPriority()
}

internal fun Model.deserializationPriority(): Int = when (this) {
    is Model.Object -> when (val ap = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed ->
            if (ap.value) OBJECT_WITH_ADDITIONAL_PROPERTIES_ALLOWED_PRIORITY
            else {
                val propertyCountPriority = properties.size.coerceAtMost(
                    OBJECT_WITHOUT_ADDITIONAL_PROPERTIES_MAX_PROPERTY_COUNT,
                )
                OBJECT_WITHOUT_ADDITIONAL_PROPERTIES_BASE_PRIORITY - propertyCountPriority
            }
        is Model.Object.AdditionalProperties.Schema -> OBJECT_WITH_TYPED_ADDITIONAL_PROPERTIES_PRIORITY
    }
    is Model.DiscriminatedObject -> DISCRIMINATED_OBJECT_PRIORITY
    is Model.Union -> UNION_PRIORITY
    is Model.Enum -> ENUM_PRIORITY
    is Model.Collection -> COLLECTION_PRIORITY
    is Model.Reference -> REFERENCE_PRIORITY
    is Model.Primitive.Int -> PRIMITIVE_INT_PRIORITY
    is Model.Primitive.Long -> PRIMITIVE_LONG_PRIORITY
    is Model.Primitive.Float -> PRIMITIVE_FLOAT_PRIORITY
    is Model.Primitive.Double -> PRIMITIVE_DOUBLE_PRIORITY
    is Model.Primitive.Boolean -> PRIMITIVE_BOOLEAN_PRIORITY
    is Model.Primitive.Unit -> PRIMITIVE_UNIT_PRIORITY
    is Model.Uuid -> UUID_PRIORITY
    is Model.Date -> DATE_PRIORITY
    is Model.DateTime -> DATE_TIME_PRIORITY
    is Model.ByteArray -> BYTE_ARRAY_PRIORITY
    is Model.Primitive.String -> STRING_PRIORITY
    is Model.FreeFormJson -> FREE_FORM_JSON_PRIORITY
}

internal fun caseSerializerCode(
    case: Model.Union.Case,
    rendered: RenderedUnionCase,
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock =
    if (rendered.isInlined) CodeBlock.of("%T.serializer()", className.nestedClass(rendered.caseSimpleName))
    else case.model.serializerCode(config, originalClassName, className, externalTypeNames)

internal fun TypeName.usesInstant(): Boolean =
    when (this) {
        is ClassName -> copy(nullable = false) == UnionInstantType
        is ParameterizedTypeName -> rawType == UnionInstantType || typeArguments.any(TypeName::usesInstant)
        is TypeVariableName -> bounds.any(TypeName::usesInstant)
        is WildcardTypeName -> inTypes.any(TypeName::usesInstant) || outTypes.any(TypeName::usesInstant)
        Dynamic,
        is LambdaTypeName -> false
    }
