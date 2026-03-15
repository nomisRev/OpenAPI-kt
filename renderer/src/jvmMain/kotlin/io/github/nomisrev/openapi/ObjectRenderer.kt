package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.WildcardTypeName
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

private val UuidType = ClassName("kotlin.uuid", "Uuid")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")
private val OptInType = ClassName("kotlin", "OptIn")

fun Model.Object.toTypeSpec(config: RenderConfig): TypeSpec {
    require(hasNoAdditionalProperties()) {
        "Phase 2 object rendering only supports additionalProperties=false. Context: $context"
    }

    val className = context.toClassName(config)
    val renderedProperties = properties.map { (jsonName, prop) ->
        renderProperty(jsonName, prop, config)
    }

    val builder = when {
        properties.isEmpty() -> TypeSpec.objectBuilder(className.simpleName).addModifiers(KModifier.DATA)
        properties.size == 1 -> {
            TypeSpec.classBuilder(className.simpleName)
                .addModifiers(KModifier.VALUE)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(renderedProperties.single().parameter)
                        .build()
                )
                .apply {
                    if (KmpTarget.JVM in config.targets) {
                        addAnnotation(JvmInline::class)
                    }
                }
        }

        else -> {
            TypeSpec.classBuilder(className.simpleName)
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .apply { renderedProperties.forEach { addParameter(it.parameter) } }
                        .build()
                )
        }
    }

    if (properties.isNotEmpty()) {
        renderedProperties.forEach { builder.addProperty(it.property) }
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    if (renderedProperties.any { it.usesUuid }) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalUuidApiType)
                .build()
        )
    }

    inline
        .toList()
        .sortedBy { (it as Model.ContextHolder).context.toClassName(config).canonicalName }
        .mapNotNull { model ->
            when (model) {
                is Model.Enum -> model.toTypeSpec(config)
                is Model.Object -> model.takeIf(Model.Object::hasNoAdditionalProperties)?.toTypeSpec(config)
                else -> null
            }
        }
        .forEach(builder::addType)

    return builder
        .addAnnotation(Serializable::class)
        .build()
}

fun Model.Object.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .build()
}

private data class RenderedProperty(
    val parameter: ParameterSpec,
    val property: PropertySpec,
    val usesUuid: Boolean,
)

private fun Model.Object.renderProperty(
    jsonName: String,
    property: Model.Object.Property,
    config: RenderConfig
): RenderedProperty {
    val paramName = jsonName.toParamName()
    val unescapedParamName = paramName.unescapeBackticks()
    val typeName = property.model
        .toTypeName(config)
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
        .initializer(paramName)
        .build()

    return RenderedProperty(parameter, property, typeName.usesUuid())
}

private fun Model.defaultLiteral(config: RenderConfig): CodeBlock? =
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

private fun Model.Object.hasNoAdditionalProperties(): Boolean =
    (additionalProperties as? Model.Object.AdditionalProperties.Allowed)?.value == false

private fun String.unescapeBackticks(): String =
    if (startsWith("`") && endsWith("`") && length >= 2) substring(1, length - 1) else this

private fun String.escapeForKdoc(): String =
    replace("%", "%%")
