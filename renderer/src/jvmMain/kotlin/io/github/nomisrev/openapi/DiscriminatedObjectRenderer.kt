package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

private val JsonClassDiscriminatorType = ClassName("kotlinx.serialization.json", "JsonClassDiscriminator")
private val OptInType = ClassName("kotlin", "OptIn")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")

fun Model.DiscriminatedObject.toTypeSpec(
    config: RenderConfig,
    classNameOverride: ClassName? = null,
): TypeSpec {
    val className = classNameOverride ?: context.toClassName(config)
    val abstractProperties = consistentAbstractProperties(config)
    val renderedAbstractProperties = abstractProperties.map { [jsonName, property] ->
        renderAbstractProperty(jsonName, property, config)
    }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        .addAnnotation(
            AnnotationSpec.builder(JsonClassDiscriminatorType)
                .addMember("%S", discriminator)
                .build()
        )
        .addAnnotation(Serializable::class)

    if (renderedAbstractProperties.any { it.usesUuid }) {
        builder.addAnnotation(
            AnnotationSpec.builder(OptInType)
                .addMember("%T::class", ExperimentalUuidApiType)
                .build()
        )
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    renderedAbstractProperties.forEach { builder.addProperty(it.propertySpec) }

    subtypes
        .map { subtype ->
            val discriminatorValue = subtype.discriminatorValue()
            subtype
                .copy(hadPropertiesBeforeStripping = true)
                .toTypeSpec(
                    config = config,
                    parentInterface = className,
                    serialName = discriminatorValue,
                    overridePropertyNames = abstractProperties.keys,
                )
        }
        .forEach(builder::addType)

    return builder.build()
}

fun Model.DiscriminatedObject.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .apply {
            if (needsJsonObjectImport()) {
                addImport("kotlinx.serialization.json", "jsonObject")
            }
        }
        .build()
}

private data class RenderedAbstractProperty(
    val propertySpec: PropertySpec,
    val usesUuid: Boolean,
)

private fun Model.DiscriminatedObject.consistentAbstractProperties(config: RenderConfig): Map<String, Model.Object.Property> =
    abstractProperties.filter { [name, property] ->
        val expected = property.renderedTypeName(config)
        subtypes.all { subtype ->
            val subtypeProperty = subtype.properties[name]
            subtypeProperty != null && subtypeProperty.renderedTypeName(config) == expected
        }
    }

private fun renderAbstractProperty(
    jsonName: String,
    property: Model.Object.Property,
    config: RenderConfig,
): RenderedAbstractProperty {
    val typeName = property.renderedTypeName(config)
    val propertySpec = PropertySpec.builder(jsonName.toParamName(), typeName)
        .addModifiers(KModifier.ABSTRACT)
        .build()
    return RenderedAbstractProperty(propertySpec, typeName.usesExperimentalUuid())
}

private fun Model.Object.Property.renderedTypeName(config: RenderConfig): TypeName =
    model.toTypeName(config).copy(nullable = model.isNullable || !isRequired)

private fun Model.Object.discriminatorValue(): String {
    val case =
        (context.nested.lastOrNull { it is NamingContext.DiscriminatedObjectCase } as? NamingContext.DiscriminatedObjectCase)
            ?: error("Expected discriminated object subtype naming context for $context")

    return when {
        case.discriminator != "Default" -> case.discriminator
        context.head is NamingContext.Reference -> (context.head as NamingContext.Reference).name
        else -> case.discriminator
    }
}

private fun String.escapeForKdoc(): String =
    replace("%", "%%")
