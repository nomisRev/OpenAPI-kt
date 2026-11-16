package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

private val JsonContentPolymorphicSerializerType =
    ClassName("kotlinx.serialization.json", "JsonContentPolymorphicSerializer")
private val JsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val DeserializationStrategyType = ClassName("kotlinx.serialization", "DeserializationStrategy")
private val SerializationExceptionType = ClassName("kotlinx.serialization", "SerializationException")
private val OptInType = ClassName("kotlin", "OptIn")
private val ExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")

fun Model.DiscriminatedObject.toTypeSpec(
    config: RenderConfig,
    classNameOverride: ClassName? = null,
): TypeSpec {
    val className = classNameOverride ?: context.toClassName(config)
    val abstractProperties = consistentAbstractProperties(config, className.packageName)
    val renderedAbstractProperties = abstractProperties.map { [jsonName, property] ->
        renderAbstractProperty(jsonName, property, config, className.packageName)
    }

    val cases = subtypes.map { it to it.discriminatorValue() }
    val serializerName = freeNestedName("Serializer", cases.mapTo(mutableSetOf()) { [subtype, _] ->
        subtype.context.toClassName(config).simpleName
    })

    // The tag is written and checked by each case's serializer, so that a case serialized on its own (an operation's
    // body, a property, a union branch) carries it too; the base only selects the case by it.
    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.%N::class", className, serializerName)
                .build()
        )

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

    cases
        .map { [subtype, tag] ->
            subtype.toTypeSpec(
                config = config,
                parentInterface = className,
                serialName = tag,
                overridePropertyNames = abstractProperties.keys,
                discriminatorTag = DiscriminatorTag(discriminator, tag),
            )
        }
        .forEach(builder::addType)

    builder.addType(baseSerializerTypeSpec(config, serializerName, className, cases))

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

/** Selects the case of a value by its tag; the case's serializer writes and checks the tag. */
private fun Model.DiscriminatedObject.baseSerializerTypeSpec(
    config: RenderConfig,
    name: String,
    className: ClassName,
    cases: List<Pair<Model.Object, String>>,
): TypeSpec {
    val select = CodeBlock.builder()
        .beginControlFlow(
            "return when (val tag = element.%M(%S))",
            MemberName(config.modelPackage.toPackageName(), DiscriminatorValueName),
            discriminator,
        )
        .apply {
            cases.forEach { [subtype, tag] ->
                addStatement("%S -> %T.serializer()", tag, subtype.context.toClassName(config))
            }
        }
        .addStatement(
            "else -> throw %T(%S + tag)",
            SerializationExceptionType,
            "'$discriminator' of ${className.simpleName} selects none of ${cases.joinToString(", ") { it.second }}: ",
        )
        .endControlFlow()
        .build()
    return TypeSpec.objectBuilder(name)
        .superclass(JsonContentPolymorphicSerializerType.parameterizedBy(className))
        .addSuperclassConstructorParameter("%T::class", className)
        .addFunction(
            FunSpec.builder("selectDeserializer")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("element", JsonElementType)
                .returns(DeserializationStrategyType.parameterizedBy(className))
                .addCode(select)
                .build()
        )
        .build()
}

private data class RenderedAbstractProperty(
    val propertySpec: PropertySpec,
    val usesUuid: Boolean,
)

private fun Model.DiscriminatedObject.consistentAbstractProperties(
    config: RenderConfig,
    packageName: String,
): Map<String, Model.Object.Property> =
    abstractProperties.filter { [name, property] ->
        val expected = property.renderedTypeName(config, packageName)
        subtypes.all { subtype ->
            val subtypeProperty = subtype.properties[name]
            subtypeProperty != null && subtypeProperty.renderedTypeName(config, packageName) == expected
        }
    }

private fun renderAbstractProperty(
    jsonName: String,
    property: Model.Object.Property,
    config: RenderConfig,
    packageName: String,
): RenderedAbstractProperty {
    val typeName = property.renderedTypeName(config, packageName)
    val propertySpec = PropertySpec.builder(jsonName.toParamName(), typeName)
        .addModifiers(KModifier.ABSTRACT)
        .build()
    return RenderedAbstractProperty(propertySpec, typeName.usesExperimentalUuid())
}

private fun Model.Object.Property.renderedTypeName(config: RenderConfig, packageName: String): TypeName =
    if (isOptionalNullable) config.optionalOf(packageName, model.toTypeName(config))
    else model.toTypeName(config).copy(nullable = model.isNullable || !isRequired)

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
