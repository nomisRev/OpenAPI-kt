package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.Dynamic
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName
import io.github.nomisrev.openapi.transformers.isTopLevel
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

private val JsonClassDiscriminatorType = ClassName("kotlinx.serialization.json", "JsonClassDiscriminator")
private val OptInType = ClassName("kotlin", "OptIn")
private val UuidType = ClassName("kotlin.uuid", "Uuid")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")
private val InstantType = ClassName("kotlin.time", "Instant")
private val ExperimentalTimeType = ClassName("kotlin.time", "ExperimentalTime")
private val KSerializerType = ClassName("kotlinx.serialization", "KSerializer")
private val SerialDescriptorType = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
private val DecoderType = ClassName("kotlinx.serialization.encoding", "Decoder")
private val EncoderType = ClassName("kotlinx.serialization.encoding", "Encoder")
private val JsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val JsonDecoderType = ClassName("kotlinx.serialization.json", "JsonDecoder")
private val PolymorphicKindType = ClassName("kotlinx.serialization.descriptors", "PolymorphicKind")
private val BuildSerialDescriptorMember = MemberName("kotlinx.serialization.descriptors", "buildSerialDescriptor")

fun Model.Union.toTypeSpec(
    config: RenderConfig,
    classNameOverride: ClassName? = null,
    externalTypeNames: Map<ClassName, TypeName> = emptyMap(),
): TypeSpec {
    val originalClassName = context.toClassName(config)
    val className = classNameOverride ?: originalClassName
    return if (discriminator != null) toDiscriminatedTypeSpec(config, originalClassName, className, externalTypeNames)
    else toNonDiscriminatedTypeSpec(config, originalClassName, className, externalTypeNames)
}

fun Model.Union.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .apply {
            if (this@toFileSpec.needsJsonObjectImport()) {
                addImport("kotlinx.serialization.json", "jsonObject")
            }
        }
        .build()
}

// ── Discriminated unions ────────────────────────────────────────────────────

private fun Model.Union.toDiscriminatedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val disc = requireNotNull(discriminator)
    val renderedCases = cases.map { it.renderDiscriminatedCase(config, originalClassName, className, disc, externalTypeNames) }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        .addAnnotation(
            AnnotationSpec.builder(JsonClassDiscriminatorType)
                .addMember("%S", disc)
                .build()
        )
        .addAnnotation(Serializable::class)

    val optInClasses = buildList {
        if (renderedCases.any { it.usesUuid }) add(ExperimentalUuidApiType)
        if (renderedCases.any { it.usesInstant }) add(ExperimentalTimeType)
    }
    if (optInClasses.isNotEmpty()) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .apply { optInClasses.forEach { addMember("%T::class", it) } }
                .build()
        )
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    renderedCases.forEach { builder.addType(it.typeSpec) }
    return builder.build()
}

private fun Model.Union.Case.renderDiscriminatedCase(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    discriminatorField: String,
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val serialName = serialNameOrNull(discriminatorField)
    val caseName = serialName?.toPascalCase() ?: caseSimpleName(config)
    return when (val caseModel = model) {
        is Model.Object ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(
                        config,
                        parentInterface,
                        serialName,
                        nameOverride = caseName,
                        classNameOverride = parentInterface.nestedClass(caseName),
                        externalTypeNames = externalTypeNames,
                    ),
                    usesUuid = false,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
            }

        is Model.Enum ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(caseModel.toTypeSpec(config, parentInterface, serialName, nameOverride = caseName), usesUuid = false)
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
            }

        is Model.Primitive.Unit -> {
            val typeSpec = TypeSpec.objectBuilder(caseName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(parentInterface)
                .addAnnotation(Serializable::class)
                .apply { serialName?.let(::addSerialNameAnnotation) }
                .build()
            RenderedUnionCase(typeSpec, usesUuid = false)
        }

        else -> renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
    }
}

// ── Non-discriminated unions ────────────────────────────────────────────────

private fun Model.Union.toNonDiscriminatedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {

    // Check for open enum pattern: exactly 2 cases, one Enum + one String
    val openEnum = detectOpenEnum()
    if (openEnum != null) return buildOpenEnumTypeSpec(config, className, openEnum)

    val renderedCases = cases.map { it.renderNonDiscriminatedCase(config, originalClassName, className, externalTypeNames) }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", className)
                .build()
        )

    val optInClasses = buildList {
        if (renderedCases.any { it.usesUuid }) add(ExperimentalUuidApiType)
        if (renderedCases.any { it.usesInstant }) add(ExperimentalTimeType)
    }
    if (optInClasses.isNotEmpty()) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .apply { optInClasses.forEach { addMember("%T::class", it) } }
                .build()
        )
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    renderedCases.forEach { builder.addType(it.typeSpec) }
    collectionNestedTypeSpecs(config, externalTypeNames).forEach(builder::addType)
    builder.addType(buildSerializerObject(config, originalClassName, className, renderedCases, externalTypeNames))
    return builder.build()
}

private fun Model.Union.collectionNestedTypeSpecs(
    config: RenderConfig,
    externalTypeNames: Map<ClassName, TypeName>,
): List<TypeSpec> =
    cases.asSequence()
        .mapNotNull { (it.model as? Model.Collection)?.inner?.nestedOrNull() }
        .filterIsInstance<Model.ContextHolder>()
        .distinctBy { it.context.toClassName(config).canonicalName }
        .mapNotNull { model ->
            when (model) {
                is Model.Enum -> model.toTypeSpec(config)
                is Model.Object -> model.toTypeSpec(config, externalTypeNames = externalTypeNames)
                is Model.Union -> model.toTypeSpec(config, externalTypeNames = externalTypeNames)
                is Model.DiscriminatedObject -> model.toTypeSpec(config)
                is Model.Reference -> null
            }
        }
        .toList()

private fun Model.Union.Case.renderNonDiscriminatedCase(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val simpleName = caseSimpleName(config)
    return when (val caseModel = model) {
        is Model.Object ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(
                        config,
                        parentInterface,
                        classNameOverride = parentInterface.nestedClass(simpleName),
                        externalTypeNames = externalTypeNames,
                    ),
                    usesUuid = false,
                    isInlined = true,
                    caseSimpleName = simpleName,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        is Model.Enum ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(config, parentInterface),
                    usesUuid = false,
                    isInlined = true,
                    caseSimpleName = simpleName,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        is Model.Primitive.Unit -> {
            val typeSpec = TypeSpec.objectBuilder(simpleName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(parentInterface)
                .addAnnotation(Serializable::class)
                .build()
            RenderedUnionCase(typeSpec, usesUuid = false, isInlined = true, caseSimpleName = simpleName)
        }

        // Nested non-discriminated union → inlined
        is Model.Union ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(config, externalTypeNames = externalTypeNames),
                    usesUuid = false,
                    isInlined = false,
                    caseSimpleName = simpleName,
                    isNestedUnion = true,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        // Nested discriminated object → inlined
        is Model.DiscriminatedObject ->
            if (!caseModel.context.isTopLevel()) {
                // TODO: DiscriminatedObject rendering not yet implemented
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        else -> renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
    }
}

// ── Serializer generation ───────────────────────────────────────────────────

private fun Model.Union.buildSerializerObject(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    renderedCases: List<RenderedUnionCase>,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val declarationCases = cases.zip(renderedCases)
    val orderedCases = declarationCases.sortedByDeserializationOrder()
    val isAnyOf = this is Model.AnyOf

    return TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(KSerializerType.parameterizedBy(className))
        .addProperty(buildDescriptorProperty(config, originalClassName, className, declarationCases, externalTypeNames))
        .addFunction(buildDeserializeFunction(config, originalClassName, className, orderedCases, isAnyOf, externalTypeNames))
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
            BuildSerialDescriptorMember,
            className.canonicalName,
            PolymorphicKindType,
        )
        .apply {
            cases.forEach { (case, rendered) ->
                addStatement(
                    "element(%S, %L.descriptor)",
                    rendered.caseSimpleName,
                    caseSerializerCode(case, rendered, config, originalClassName, className, externalTypeNames)
                )
            }
        }
        .endControlFlow()
        .build()

    return PropertySpec.builder("descriptor", SerialDescriptorType)
        .addModifiers(KModifier.OVERRIDE)
        .addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ClassName("kotlinx.serialization", "InternalSerializationApi"))
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        .initializer(descriptorCode)
        .build()
}

private fun buildDeserializeFunction(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    isAnyOf: Boolean,
    externalTypeNames: Map<ClassName, TypeName>,
): FunSpec {
    val code = if (isAnyOf) {
        buildAnyOfDeserializeCode(config, originalClassName, className, orderedCases, externalTypeNames)
    } else {
        buildAttemptDeserializeCode(config, originalClassName, className, orderedCases, externalTypeNames)
    }

    return FunSpec.builder("deserialize")
        .addModifiers(KModifier.OVERRIDE)
        .returns(className)
        .addParameter("decoder", DecoderType)
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
    .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", JsonElementType)
    .addStatement(
        "val json = requireNotNull(decoder as? %T) { %S }.json",
        JsonDecoderType,
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
        orderedCases.forEach { (case, rendered) ->
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

private fun buildAnyOfDeserializeCode(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    orderedCases: List<Pair<Model.Union.Case, RenderedUnionCase>>,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock {
    data class DispatchCase(
        val case: Model.Union.Case,
        val rendered: RenderedUnionCase,
        val model: Model.Object,
        val uniqueKeys: Set<String>,
    )

    val objectCases = orderedCases.mapNotNull { (case, rendered) ->
        (case.model as? Model.Object)?.let { model ->
            DispatchCase(case, rendered, model, emptySet())
        }
    }

    if (objectCases.size != orderedCases.size) {
        return buildAttemptDeserializeCode(config, originalClassName, className, orderedCases, externalTypeNames)
    }

    val uniqueKeysByCase = objectCases.map { dispatchCase ->
        val keys = dispatchCase.model.properties.keys
        val otherKeys = objectCases
            .asSequence()
            .filterNot { it.case == dispatchCase.case }
            .flatMapTo(mutableSetOf()) { it.model.properties.keys }
        dispatchCase.copy(uniqueKeys = keys - otherKeys)
    }

    if (uniqueKeysByCase.count { it.uniqueKeys.isEmpty() } > 1) {
        return buildAttemptDeserializeCode(config, originalClassName, className, orderedCases, externalTypeNames)
    }

    val fallbackCase = uniqueKeysByCase.singleOrNull { it.uniqueKeys.isEmpty() }
    val code = CodeBlock.builder()
        .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", JsonElementType)
        .addStatement(
            "val json = requireNotNull(decoder as? %T) { %S }.json",
            JsonDecoderType,
            "Complex unions currently only supported for Json"
        )
        .addStatement("val keys = value.jsonObject.keys")
        .apply {
            uniqueKeysByCase.filter { it.uniqueKeys.isNotEmpty() }.forEach { dispatchCase ->
                val caseClassName = className.nestedClass(dispatchCase.rendered.caseSimpleName)
                val serializerCode = caseSerializerCode(
                    dispatchCase.case,
                    dispatchCase.rendered,
                    config,
                    originalClassName,
                    className,
                    externalTypeNames
                )
                val condition = dispatchCase.uniqueKeys.joinToString(separator = " || ") { "\"${it.ktStringLiteral()}\" in keys" }
                beginControlFlow("if ($condition)")
                if (dispatchCase.rendered.isInlined || dispatchCase.rendered.isNestedUnion) {
                    addStatement("return json.decodeFromJsonElement(%L, value)", serializerCode)
                } else {
                    addStatement("return %T(json.decodeFromJsonElement(%L, value))", caseClassName, serializerCode)
                }
                endControlFlow()
            }

            if (fallbackCase != null) {
                val serializerCode = caseSerializerCode(
                    fallbackCase.case,
                    fallbackCase.rendered,
                    config,
                    originalClassName,
                    className,
                    externalTypeNames
                )
                if (fallbackCase.rendered.isInlined || fallbackCase.rendered.isNestedUnion) {
                    addStatement("return json.decodeFromJsonElement(%L, value)", serializerCode)
                } else {
                    val caseClassName = className.nestedClass(fallbackCase.rendered.caseSimpleName)
                    addStatement("return %T(json.decodeFromJsonElement(%L, value))", caseClassName, serializerCode)
                }
            } else {
                add(buildAttemptDeserializeBody(config, originalClassName, className, orderedCases, externalTypeNames))
            }
        }
        .build()

    return code
}

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
            allCases.forEach { (case, rendered) ->
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
        .addParameter("encoder", EncoderType)
        .addParameter("value", className)
        .addCode(code)
        .build()
}

// ── Open Enum Pattern ───────────────────────────────────────────────────────

private data class OpenEnumParts(
    val enumCase: Model.Union.Case,
    val enumModel: Model.Enum,
    val stringCase: Model.Union.Case,
)

private fun Model.Union.detectOpenEnum(): OpenEnumParts? {
    if (cases.size != 2) return null
    val (a, b) = cases
    val aEnum = a.model as? Model.Enum
    val bEnum = b.model as? Model.Enum
    val aString = a.model as? Model.Primitive.String
    val bString = b.model as? Model.Primitive.String

    return when {
        aEnum != null && bString != null -> OpenEnumParts(a, aEnum, b)
        bEnum != null && aString != null -> OpenEnumParts(b, bEnum, a)
        else -> null
    }
}

private fun Model.Union.buildOpenEnumTypeSpec(
    config: RenderConfig,
    className: ClassName,
    parts: OpenEnumParts,
): TypeSpec {
    val enumSimpleName = parts.enumCase.caseSimpleName(config)
    val stringSimpleName = parts.stringCase.caseSimpleName(config)

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", className)
                .build()
        )
        .addProperty(PropertySpec.builder("value", STRING).build())

    // String wrapper case
    val stringTypeSpec = TypeSpec.classBuilder(stringSimpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("value", com.squareup.kotlinpoet.STRING)
                .build()
        )
        .addProperty(
            PropertySpec.builder("value", com.squareup.kotlinpoet.STRING)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("value")
                .build()
        )
        .addSuperinterface(className)
        .addAnnotation(Serializable::class)
        .apply {
            if (KmpTarget.JVM in config.targets) addAnnotation(JvmInline::class)
        }
        .build()

    // Enum case (inlined — implements union directly)
    val enumTypeSpec = parts.enumModel.toTypeSpec(config, className, overrideValueProperty = true)

    builder.addType(stringTypeSpec)
    builder.addType(enumTypeSpec)

    // Open enum serializer
    builder.addType(buildOpenEnumSerializer(config, className, parts, enumSimpleName, stringSimpleName))

    return builder.build()
}

private fun buildOpenEnumSerializer(
    config: RenderConfig,
    className: ClassName,
    parts: OpenEnumParts,
    enumSimpleName: String,
    stringSimpleName: String,
): TypeSpec {
    val enumClassName = className.nestedClass(enumSimpleName)
    val stringClassName = className.nestedClass(stringSimpleName)

    val serializeCode = CodeBlock.builder()
        .beginControlFlow("when(value)")
        .apply {
            parts.enumModel.values.forEach { rawValue ->
                val v = rawValue ?: "null"
                val entryName = toEnumValueName(v)
                addStatement("%T.%L -> encoder.encodeString(%S)", enumClassName, entryName, v)
            }
            addStatement("is %T -> encoder.encodeString(value.value)", stringClassName)
        }
        .endControlFlow()
        .build()

    val deserializeCode = CodeBlock.builder()
        .add("return ")
        .beginControlFlow("when(val value = decoder.decodeString())")
        .apply {
            parts.enumModel.values.forEach { rawValue ->
                val v = rawValue ?: "null"
                val entryName = toEnumValueName(v)
                addStatement("%S -> %T.%L", v, enumClassName, entryName)
            }
            addStatement("else -> %T(value)", stringClassName)
        }
        .endControlFlow()
        .build()

    return TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(KSerializerType.parameterizedBy(className))
        .addProperty(
            PropertySpec.builder("descriptor", SerialDescriptorType)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%T.%M().descriptor", kotlin.String::class, SerializerMember)
                .build()
        )
        .addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", EncoderType)
                .addParameter("value", className)
                .addCode(serializeCode)
                .build()
        )
        .addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .returns(className)
                .addParameter("decoder", DecoderType)
                .addCode(deserializeCode)
                .build()
        )
        .build()
}

// ── Deserialization ordering ────────────────────────────────────────────────

private fun List<Pair<Model.Union.Case, RenderedUnionCase>>.sortedByDeserializationOrder():
    List<Pair<Model.Union.Case, RenderedUnionCase>> = sortedBy { (case, _) ->
    case.model.deserializationPriority()
}

private fun Model.deserializationPriority(): Int = when (this) {
    // Objects without additionalProperties (more properties → higher priority)
    is Model.Object -> when (val ap = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed ->
            if (ap.value) 200 // with additionalProperties allowed
            else 100 - properties.size.coerceAtMost(99) // without: more props = lower number = higher priority
        is Model.Object.AdditionalProperties.Schema -> 150 // typed additionalProperties
    }
    is Model.DiscriminatedObject -> 300
    is Model.Union -> 400
    is Model.Enum -> 500
    is Model.Collection -> 600
    is Model.Reference -> 700
    is Model.Primitive.Int -> 800
    is Model.Primitive.Long -> 801
    is Model.Primitive.Float -> 802
    is Model.Primitive.Double -> 803
    is Model.Primitive.Boolean -> 804
    is Model.Primitive.Unit -> 805
    is Model.Uuid -> 900
    is Model.Date -> 901
    is Model.DateTime -> 902
    is Model.ByteArray -> 903
    is Model.Primitive.String -> 1000
    is Model.FreeFormJson -> 1100
}

// ── Shared helpers ──────────────────────────────────────────────────────────

private data class RenderedUnionCase(
    val typeSpec: TypeSpec,
    val usesUuid: Boolean,
    val usesInstant: Boolean = false,
    val isInlined: Boolean = false,
    val caseSimpleName: String = typeSpec.name.orEmpty(),
    val isNestedUnion: Boolean = false,
)

private fun Model.Union.Case.renderWrappedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    simpleName: String,
    serialName: String?,
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val valueType = model.toTypeName(config)
        .remapNestedClassName(originalClassName, parentInterface)
        .remapTypeNames(externalTypeNames)
    val typeSpec = TypeSpec.classBuilder(simpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder("value", valueType).build())
                .build()
        )
        .addProperty(
            PropertySpec.builder("value", valueType)
                .initializer("value")
                .build()
        )
        .addSuperinterface(parentInterface)
        .addAnnotation(Serializable::class)
        .apply {
            if (KmpTarget.JVM in config.targets) {
                addAnnotation(JvmInline::class)
            }
            serialName?.let(::addSerialNameAnnotation)
        }
        .build()
    return RenderedUnionCase(typeSpec, valueType.usesUuid(), usesInstant = valueType.usesInstant(), caseSimpleName = simpleName)
}

private fun Model.Union.Case.caseSimpleName(config: RenderConfig): String =
    model.unionCaseValueOrNull()?.toPascalCase()?.takeIf { it.isNotBlank() }
        ?: when (val caseModel = model) {
            is Model.Reference -> "Case${caseModel.context.toClassName(config).simpleName}"
            is Model.Primitive.String -> "CaseString"
            is Model.Primitive.Int -> "CaseInt"
            is Model.Primitive.Long -> "CaseLong"
            is Model.Primitive.Float -> "CaseFloat"
            is Model.Primitive.Double -> "CaseDouble"
            is Model.Primitive.Boolean -> "CaseBoolean"
            is Model.Primitive.Unit -> "Empty"
            is Model.ByteArray -> "CaseBinary"
            is Model.Date -> "CaseDate"
            is Model.DateTime -> "CaseDateTime"
            is Model.Uuid -> "CaseUuid"
            is Model.FreeFormJson -> "CaseJsonElement"
            is Model.Collection -> caseModel.collectionCaseSimpleName(config)
            is Model.Object -> {
                val name = caseModel.context.toClassName(config).simpleName
                if (caseModel.context.isTopLevel()) "Case$name" else name
            }

            is Model.Enum -> {
                val name = caseModel.context.toClassName(config).simpleName
                if (caseModel.context.isTopLevel()) "Case$name" else name
            }

            is Model.Union -> "Case${caseModel.context.toClassName(config).simpleName}"
            is Model.DiscriminatedObject -> "Case${caseModel.context.toClassName(config).simpleName}"
        }

private fun Model.Union.Case.serialNameOrNull(discriminatorField: String): String? =
    discriminator ?: discriminatorValueFromInlineObject(discriminatorField) ?: model.unionCaseValueOrNull()

private fun Model.Union.Case.discriminatorValueFromInlineObject(discriminatorField: String): String? {
    val objectCase = model as? Model.Object ?: return null
    val discriminatorProperty = objectCase.properties[discriminatorField] ?: return null
    val discriminatorModel = discriminatorProperty.model as? Model.Enum ?: return null
    return discriminatorModel.values.singleOrNull()
}

private fun Model.unionCaseValueOrNull(): String? =
    (((this as? Model.ContextHolder)?.context?.nested?.lastOrNull()) as? NamingContext.UnionCase)?.value

private fun Model.Collection.collectionCaseSimpleName(config: RenderConfig): String =
    "Case${inner.collectionEntryName(config)}"

private fun Model.collectionEntryName(config: RenderConfig): String =
    when (this) {
        is Model.Primitive.String -> "Strings"
        is Model.Primitive.Int -> "Ints"
        is Model.Primitive.Long -> "Longs"
        is Model.Primitive.Float -> "Floats"
        is Model.Primitive.Double -> "Doubles"
        is Model.Primitive.Boolean -> "Booleans"
        is Model.Primitive.Unit -> "Units"
        is Model.ByteArray -> "Binaries"
        is Model.Date -> "Dates"
        is Model.DateTime -> "DateTimes"
        is Model.Uuid -> "Uuids"
        is Model.FreeFormJson -> "JsonElements"
        is Model.Collection -> "${inner.collectionEntryName(config)}List"
        is Model.Reference -> "${context.toClassName(config).simpleName}List"
        is Model.Object -> "${context.toClassName(config).simpleName}List"
        is Model.Enum -> "${context.toClassName(config).simpleName}List"
        is Model.Union -> "${context.toClassName(config).simpleName}List"
        is Model.DiscriminatedObject -> "${context.toClassName(config).simpleName}List"
    }

private fun TypeSpec.Builder.addSerialNameAnnotation(value: String): TypeSpec.Builder =
    addAnnotation(
        AnnotationSpec.builder(SerialName::class)
            .addMember("%S", value)
            .build()
    )

// For inlined cases, use the nested type's own serializer; for wrapped, use the model's serializer
private fun caseSerializerCode(
    case: Model.Union.Case,
    rendered: RenderedUnionCase,
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): CodeBlock =
    if (rendered.isInlined) CodeBlock.of("%T.serializer()", className.nestedClass(rendered.caseSimpleName))
    else case.model.serializerCode(config, originalClassName, className, externalTypeNames)

private fun TypeName.usesUuid(): Boolean =
    when (this) {
        is ClassName -> copy(nullable = false) == UuidType
        is ParameterizedTypeName -> rawType == UuidType || typeArguments.any(TypeName::usesUuid)
        is TypeVariableName -> bounds.any(TypeName::usesUuid)
        is WildcardTypeName -> inTypes.any(TypeName::usesUuid) || outTypes.any(TypeName::usesUuid)
        Dynamic,
        is LambdaTypeName -> false
    }

private fun TypeName.usesInstant(): Boolean =
    when (this) {
        is ClassName -> copy(nullable = false) == InstantType
        is ParameterizedTypeName -> rawType == InstantType || typeArguments.any(TypeName::usesInstant)
        is TypeVariableName -> bounds.any(TypeName::usesInstant)
        is WildcardTypeName -> inTypes.any(TypeName::usesInstant) || outTypes.any(TypeName::usesInstant)
        Dynamic,
        is LambdaTypeName -> false
    }

private fun String.escapeForKdoc(): String =
    replace("%", "%%")

private fun String.ktStringLiteral(): String =
    replace("\\", "\\\\").replace("\"", "\\\"")
