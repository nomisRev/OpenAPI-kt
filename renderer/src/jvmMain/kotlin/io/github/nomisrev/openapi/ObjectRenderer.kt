package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
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
import com.squareup.kotlinpoet.joinToCode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

private val UuidType = ClassName("kotlin.uuid", "Uuid")
private val LocalDateType = ClassName("kotlinx.datetime", "LocalDate")
private val LocalDateTimeType = ClassName("kotlinx.datetime", "LocalDateTime")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")
private val JsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val JsonArrayType = ClassName("kotlinx.serialization.json", "JsonArray")
private val JsonObjectType = ClassName("kotlinx.serialization.json", "JsonObject")
private val JsonEncoderType = ClassName("kotlinx.serialization.json", "JsonEncoder")
private val JsonDecoderType = ClassName("kotlinx.serialization.json", "JsonDecoder")
private val KSerializerType = ClassName("kotlinx.serialization", "KSerializer")
private val KeepGeneratedSerializerType = ClassName("kotlinx.serialization", "KeepGeneratedSerializer")
private val SerialDescriptorType = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
private val DecoderType = ClassName("kotlinx.serialization.encoding", "Decoder")
private val EncoderType = ClassName("kotlinx.serialization.encoding", "Encoder")
private val OptInType = ClassName("kotlin", "OptIn")
private val MapType = ClassName("kotlin.collections", "Map")
private val SerializerMember = MemberName("kotlinx.serialization.builtins", "serializer")
private val NullableMember = MemberName("kotlinx.serialization.builtins", "nullable")
private val ListSerializerMember = MemberName("kotlinx.serialization.builtins", "ListSerializer")
private val ByteArraySerializerMember = MemberName("kotlinx.serialization.builtins", "ByteArraySerializer")

fun Model.Object.toTypeSpec(
    config: RenderConfig,
    parentInterface: ClassName? = null,
    serialName: String? = null,
    nameOverride: String? = null,
    overridePropertyNames: Set<String> = emptySet(),
    classNameOverride: ClassName? = null,
): TypeSpec {
    val className = context.toClassName(config)
    val effectiveClassName = classNameOverride ?: if (nameOverride != null) {
        val enclosing = className.enclosingClassName()
        enclosing?.nestedClass(nameOverride) ?: ClassName(className.packageName, nameOverride)
    } else className
    val simpleName = classNameOverride?.simpleName ?: nameOverride ?: className.simpleName
    // When the class is renamed or moved, nested type references still use the old name.
    // Build a corrected ClassName so nested types resolve against the rendered owner.
    val renderedProperties = properties.map { (jsonName, prop) ->
        renderProperty(
            jsonName = jsonName,
            property = prop,
            config = config,
            originalClassName = className,
            effectiveClassName = effectiveClassName,
            isOverride = jsonName in overridePropertyNames,
        )
    }
    val additionalProperty = renderAdditionalProperty(config)
    val allProperties = renderedProperties + listOfNotNull(additionalProperty?.rendered)

    val builder = when {
        allProperties.isEmpty() -> TypeSpec.objectBuilder(simpleName).addModifiers(KModifier.DATA)
        allProperties.size == 1 && additionalProperty == null -> {
            TypeSpec.classBuilder(simpleName)
                .addModifiers(KModifier.VALUE)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(allProperties.single().parameter)
                        .build()
                )
                .apply {
                    if (KmpTarget.JVM in config.targets) {
                        addAnnotation(JvmInline::class)
                    }
                }
        }

        else -> {
            TypeSpec.classBuilder(simpleName)
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .apply { allProperties.forEach { addParameter(it.parameter) } }
                        .build()
                )
        }
    }

    if (allProperties.isNotEmpty()) {
        allProperties.forEach { builder.addProperty(it.property) }
    }

    serialName?.let { value ->
        builder.addAnnotation(
            AnnotationSpec.builder(SerialName::class)
                .addMember("%S", value)
                .build()
        )
    }
    parentInterface?.let(builder::addSuperinterface)

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    if (allProperties.any { it.usesUuid }) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalUuidApiType)
                .build()
        )
    }

    if (additionalProperty != null) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        builder.addAnnotation(AnnotationSpec.builder(KeepGeneratedSerializerType).build())
        builder.addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", effectiveClassName)
                .build()
        )
    } else {
        builder.addAnnotation(Serializable::class)
    }

    inline
        .toList()
        .sortedBy { (it as Model.ContextHolder).context.toClassName(config).canonicalName }
        .mapNotNull { model ->
            val childClassNameOverride = if (effectiveClassName != className) {
                ((model as? Model.ContextHolder)?.context?.toClassName(config))
                    ?.remapNestedClassName(className, effectiveClassName) as? ClassName
            } else null
            when (model) {
                is Model.Enum -> model.toTypeSpec(config, nameOverride = childClassNameOverride?.simpleName)
                is Model.Object -> model.toTypeSpec(config, classNameOverride = childClassNameOverride)
                else -> null
            }
        }
        .forEach(builder::addType)

    additionalProperty
        ?.let { builder.addType(serializerTypeSpec(config, effectiveClassName, renderedProperties, it.kind)) }

    return builder.build()
}

fun Model.Object.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .build()
}

private data class RenderedProperty(
    val jsonName: String,
    val parameter: ParameterSpec,
    val property: PropertySpec,
    val usesUuid: Boolean,
)

private sealed interface AdditionalPropertyKind {
    data object Json : AdditionalPropertyKind

    data class Typed(val valueType: Model) : AdditionalPropertyKind
}

private data class RenderedAdditionalProperty(
    val rendered: RenderedProperty,
    val kind: AdditionalPropertyKind,
)

private fun Model.Object.renderProperty(
    jsonName: String,
    property: Model.Object.Property,
    config: RenderConfig,
    originalClassName: ClassName? = null,
    effectiveClassName: ClassName? = null,
    isOverride: Boolean = false,
): RenderedProperty {
    val paramName = jsonName.toParamName()
    val unescapedParamName = paramName.unescapeBackticks()
    val rawTypeName = property.model.toTypeName(config)
    val typeName = (if (originalClassName != null && effectiveClassName != null && originalClassName != effectiveClassName)
        rawTypeName.remapNestedClassName(originalClassName, effectiveClassName)
    else rawTypeName)
        .copy(nullable = property.model.isNullable || !property.isRequired)
    val literalDefault = property.model.defaultLiteral(config)

    val parameter = ParameterSpec.builder(paramName, typeName)
        .apply {
            if (unescapedParamName != jsonName) {
                addAnnotation(
                    AnnotationSpec.builder(SerialName::class)
                        .addMember("%S", jsonName)
                        .build()
                )
            }
            if (property.isRequired && literalDefault != null) {
                addAnnotation(Required::class)
            }

            when {
                !property.isRequired -> defaultValue(CodeBlock.of("null"))
                property.model.isNullable && literalDefault != null -> defaultValue(CodeBlock.of("null"))
                property.model.isNullable -> {}
                literalDefault != null -> defaultValue(literalDefault)
            }
        }
        .build()

    val property = PropertySpec.builder(paramName, typeName)
        .apply { if (isOverride) addModifiers(KModifier.OVERRIDE) }
        .initializer(paramName)
        .build()

    return RenderedProperty(jsonName, parameter, property, typeName.usesUuid())
}

private fun Model.Object.renderAdditionalProperty(config: RenderConfig): RenderedAdditionalProperty? =
    when (val ap = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed ->
            if (ap.value) {
                val typeName = JsonObjectType.copy(nullable = true)
                val parameter = ParameterSpec.builder("additional", typeName)
                    .defaultValue(CodeBlock.of("null"))
                    .build()
                val property = PropertySpec.builder("additional", typeName)
                    .initializer("additional")
                    .build()
                RenderedAdditionalProperty(
                    RenderedProperty("additional", parameter, property, usesUuid = false),
                    AdditionalPropertyKind.Json
                )
            } else {
                null
            }

        is Model.Object.AdditionalProperties.Schema -> {
            val valueType = ap.value.toTypeName(config)
            val typeName = MapType.parameterizedBy(STRING, valueType).copy(nullable = true)
            val parameter = ParameterSpec.builder("additional", typeName)
                .defaultValue(CodeBlock.of("null"))
                .build()
            val property = PropertySpec.builder("additional", typeName)
                .initializer("additional")
                .build()
            RenderedAdditionalProperty(
                RenderedProperty("additional", parameter, property, typeName.usesUuid()),
                AdditionalPropertyKind.Typed(ap.value)
            )
        }
    }

private fun Model.Object.serializerTypeSpec(
    config: RenderConfig,
    className: ClassName,
    renderedProperties: List<RenderedProperty>,
    additionalPropertyKind: AdditionalPropertyKind
): TypeSpec =
    TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(KSerializerType.parameterizedBy(className))
        .addProperty(
            PropertySpec.builder("descriptor", SerialDescriptorType)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("generatedSerializer().descriptor")
                .build()
        )
        .addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", EncoderType)
                .addParameter("value", className)
                .addCode(
                    serializerSerializeCode(config, additionalPropertyKind)
                )
                .build()
        )
        .addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .returns(className)
                .addParameter("decoder", DecoderType)
                .addCode(
                    serializerDeserializeCode(config, renderedProperties, additionalPropertyKind)
                )
                .build()
        )
        .build()

private fun serializerSerializeCode(
    config: RenderConfig,
    additionalPropertyKind: AdditionalPropertyKind
): CodeBlock =
    CodeBlock.builder()
        .addStatement("val json = (encoder as %T).json", JsonEncoderType)
        .addStatement(
            "val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as %T",
            JsonObjectType
        )
        .addStatement("val content = mutableMapOf<%T, %T>()", String::class, JsonElementType)
        .beginControlFlow("known.forEach { (key, jsonElement) ->")
        .beginControlFlow("if (key != %S)", "additional")
        .addStatement("content[key] = jsonElement")
        .endControlFlow()
        .endControlFlow()
        .apply {
            when (additionalPropertyKind) {
                AdditionalPropertyKind.Json -> {
                    beginControlFlow("value.additional?.forEach { (key, jsonElement) ->")
                    addStatement("content[key] = jsonElement")
                    endControlFlow()
                }

                is AdditionalPropertyKind.Typed -> {
                    beginControlFlow("value.additional?.forEach { (key, additionalValue) ->")
                    addStatement(
                        "content[key] = json.encodeToJsonElement(%L, additionalValue)",
                        additionalPropertyKind.valueType.serializerCode(config)
                    )
                    endControlFlow()
                }
            }
        }
        .addStatement("encoder.encodeSerializableValue(%T.serializer(), %T(content))", JsonObjectType, JsonObjectType)
        .build()

private fun serializerDeserializeCode(
    config: RenderConfig,
    renderedProperties: List<RenderedProperty>,
    additionalPropertyKind: AdditionalPropertyKind
): CodeBlock =
    CodeBlock.builder()
        .addStatement("val json = (decoder as %T).json", JsonDecoderType)
        .addStatement("val element = decoder.decodeSerializableValue(%T.serializer())", JsonObjectType)
        .apply {
            if (renderedProperties.isEmpty()) {
                addStatement("val knownNames = emptySet<%T>()", String::class)
            } else {
                addStatement(
                    "val knownNames = setOf(%L)",
                    renderedProperties.joinToCode(separator = ", ") { CodeBlock.of("%S", it.jsonName) }
                )
            }
        }
        .addStatement("val known = json.decodeFromJsonElement(generatedSerializer(), %T(element.filterKeys { it in knownNames }))", JsonObjectType)
        .apply {
            when (additionalPropertyKind) {
                AdditionalPropertyKind.Json ->
                    addStatement("val additional = %T(element - knownNames).ifEmpty { null }", JsonObjectType)

                is AdditionalPropertyKind.Typed -> add(
                    "%L",
                    CodeBlock.builder()
                        .addStatement("val additional = (element - knownNames)")
                        .indent()
                        .addStatement(
                            ".mapValues { (_, jsonElement) -> json.decodeFromJsonElement(%L, jsonElement) }",
                            additionalPropertyKind.valueType.serializerCode(config)
                        )
                        .addStatement(".ifEmpty { null }")
                        .unindent()
                        .build()
                )
            }
        }
        .addStatement("return known.copy(additional = additional)")
        .build()

private fun Model.serializerCode(config: RenderConfig): CodeBlock {
    val nonNullable = nonNullableSerializerCode(config)
    return if (isNullable) CodeBlock.of("%L.%M", nonNullable, NullableMember) else nonNullable
}

private fun Model.nonNullableSerializerCode(config: RenderConfig): CodeBlock =
    when (this) {
        is Model.Primitive.String -> CodeBlock.of("%T.%M()", kotlin.String::class, SerializerMember)
        is Model.Primitive.Int -> CodeBlock.of("%T.%M()", kotlin.Int::class, SerializerMember)
        is Model.Primitive.Long -> CodeBlock.of("%T.%M()", kotlin.Long::class, SerializerMember)
        is Model.Primitive.Float -> CodeBlock.of("%T.%M()", kotlin.Float::class, SerializerMember)
        is Model.Primitive.Double -> CodeBlock.of("%T.%M()", kotlin.Double::class, SerializerMember)
        is Model.Primitive.Boolean -> CodeBlock.of("%T.%M()", kotlin.Boolean::class, SerializerMember)
        is Model.Primitive.Unit -> CodeBlock.of("%T.%M()", kotlin.Unit::class, SerializerMember)
        is Model.ByteArray -> CodeBlock.of("%M()", ByteArraySerializerMember)
        is Model.Uuid -> CodeBlock.of("%T.serializer()", UuidType)
        is Model.Date -> CodeBlock.of("%T.serializer()", LocalDateType)
        is Model.DateTime -> CodeBlock.of("%T.serializer()", LocalDateTimeType)
        is Model.FreeFormJson -> CodeBlock.of("%T.serializer()", JsonElementType)
        is Model.Collection ->
            if (inner is Model.FreeFormJson) {
                CodeBlock.of("%T.serializer()", JsonArrayType)
            } else {
                CodeBlock.of("%M(%L)", ListSerializerMember, inner.serializerCode(config))
            }

        is Model.Object -> CodeBlock.of("%T.serializer()", context.toClassName(config))
        is Model.Enum -> CodeBlock.of("%T.serializer()", context.toClassName(config))
        is Model.Reference -> CodeBlock.of("%T.serializer()", context.toClassName(config))
        is Model.Union -> CodeBlock.of("%T.serializer()", context.toClassName(config))
        is Model.DiscriminatedObject -> CodeBlock.of("%T.serializer()", context.toClassName(config))
    }

internal fun Model.defaultLiteral(config: RenderConfig): CodeBlock? =
    when (this) {
        is Model.Primitive.String -> default.toLiteral { value -> CodeBlock.of("%S", value) }
        is Model.Primitive.Int -> default.toLiteral { value -> CodeBlock.of("%L", value) }
        is Model.Primitive.Long -> default.toLiteral { value -> CodeBlock.of("%LL", value) }
        is Model.Primitive.Float -> default.toLiteral { value -> CodeBlock.of("%Lf", value) }
        is Model.Primitive.Double -> default.toLiteral { value -> CodeBlock.of("%L", value) }
        is Model.Primitive.Boolean -> default.toLiteral { value -> CodeBlock.of("%L", value) }
        is Model.Enum -> default.toLiteral { value ->
            CodeBlock.of("%T.%L", context.toClassName(config), toEnumValueName(value))
        }

        is Model.Collection -> default.toLiteral { values -> values.toListLiteral(inner, config) }
        else -> null
    }

private fun <A : Any> Model.Default<A>?.toLiteral(literal: (A) -> CodeBlock?): CodeBlock? =
    when (this) {
        null -> null
        Model.Default.Null -> CodeBlock.of("null")
        is Model.Default.Value -> literal(value)
    }

private fun List<String>.toListLiteral(inner: Model, config: RenderConfig): CodeBlock? {
    if (isEmpty()) return CodeBlock.of("emptyList()")
    val entries = map { value -> inner.collectionEntryLiteral(value, config) }
    if (entries.any { it == null }) return null
    return CodeBlock.builder()
        .add("listOf(")
        .apply {
            entries.filterNotNull().forEachIndexed { index, code ->
                if (index > 0) add(", ")
                add("%L", code)
            }
        }
        .add(")")
        .build()
}

private fun Model.collectionEntryLiteral(raw: String, config: RenderConfig): CodeBlock? =
    when (this) {
        is Model.Primitive.String -> CodeBlock.of("%S", raw)
        is Model.Primitive.Int -> raw.toIntOrNull()?.let { CodeBlock.of("%L", it) }
        is Model.Primitive.Long -> raw.toLongOrNull()?.let { CodeBlock.of("%LL", it) }
        is Model.Primitive.Float -> raw.toFloatOrNull()?.let { CodeBlock.of("%Lf", it) }
        is Model.Primitive.Double -> raw.toDoubleOrNull()?.let { CodeBlock.of("%L", it) }
        is Model.Primitive.Boolean -> raw.toBooleanStrictOrNull()?.let { CodeBlock.of("%L", it) }
        is Model.Enum -> CodeBlock.of("%T.%L", context.toClassName(config), toEnumValueName(raw))
        else -> null
    }

private fun TypeName.usesUuid(): Boolean =
    when (this) {
        is ClassName -> copy(nullable = false) == UuidType
        is ParameterizedTypeName -> rawType == UuidType || typeArguments.any(TypeName::usesUuid)
        is TypeVariableName -> bounds.any(TypeName::usesUuid)
        is WildcardTypeName -> inTypes.any(TypeName::usesUuid) || outTypes.any(TypeName::usesUuid)
        else -> false
    }

// When a class is renamed (nameOverride), nested type references still use the old class name path.
// This replaces the old class name with the new one in nested ClassName references.
internal fun TypeName.remapNestedClassName(oldClassName: ClassName, newClassName: ClassName): TypeName =
    when (this) {
        is ClassName -> {
            val oldPrefix = oldClassName.canonicalName + "."
            val canonical = this.canonicalName
            if (canonical == oldClassName.canonicalName) {
                newClassName.copy(nullable = isNullable)
            } else if (canonical.startsWith(oldPrefix)) {
                val suffix = canonical.removePrefix(oldPrefix)
                val parts = suffix.split(".")
                var result: ClassName = newClassName
                for (part in parts) result = result.nestedClass(part)
                result.copy(nullable = isNullable)
            } else this
        }
        is ParameterizedTypeName -> {
            (rawType.remapNestedClassName(oldClassName, newClassName) as ClassName)
                .parameterizedBy(typeArguments.map { it.remapNestedClassName(oldClassName, newClassName) })
                .copy(nullable = isNullable)
        }
        else -> this
    }

private fun String.unescapeBackticks(): String =
    if (startsWith("`") && endsWith("`") && length >= 2) substring(1, length - 1) else this

private fun String.escapeForKdoc(): String =
    replace("%", "%%")
