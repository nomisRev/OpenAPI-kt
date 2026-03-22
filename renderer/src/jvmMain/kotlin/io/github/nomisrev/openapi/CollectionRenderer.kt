package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

internal fun Model.Object.isTopLevelCollectionWrapper(): Boolean =
    context.head is NamingContext.Reference &&
        context.nested.isEmpty() &&
        (additionalProperties as? Model.Object.AdditionalProperties.Allowed)?.value == false &&
        properties.size == 1 &&
        properties["items"]?.isRequired == true &&
        properties["items"]?.model is Model.Collection

fun Model.Object.toCollectionFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toCollectionTypeSpec(config))
        .build()
}

private fun Model.Object.toCollectionTypeSpec(config: RenderConfig): TypeSpec {
    val className = context.toClassName(config)
    val collection = properties.getValue("items").model as Model.Collection
    val typeName = collection.toTypeName(config)
    val defaultLiteral = collection.defaultLiteral(config)

    val parameter = ParameterSpec.builder("items", typeName)
        .apply {
            if (defaultLiteral != null) {
                addAnnotation(Required::class)
                defaultValue(defaultLiteral)
            }
        }
        .build()

    val builder = TypeSpec.classBuilder(className.simpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(parameter)
                .build()
        )
        .addProperty(
            PropertySpec.builder("items", typeName)
                .initializer("items")
                .build()
        )
        .addAnnotation(Serializable::class)

    if (KmpTarget.JVM in config.targets) {
        builder.addAnnotation(JvmInline::class)
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.escapeForKdoc()) }

    collection.inner.nestedOrNull()
        ?.let { model ->
            when (model) {
                is Model.Enum -> builder.addType(model.toTypeSpec(config))
                is Model.Object -> builder.addType(model.toTypeSpec(config))
                is Model.Union -> builder.addType(model.toTypeSpec(config))
                is Model.DiscriminatedObject -> builder.addType(model.toTypeSpec(config))

                is Model.ByteArray,
                is Model.Collection,
                is Model.Date,
                is Model.DateTime,
                is Model.FreeFormJson,
                is Model.Primitive,
                is Model.Reference,
                is Model.Uuid -> {}
            }
        }

    return builder.build()
}

private fun Model.Collection.defaultLiteral(config: RenderConfig): CodeBlock? =
    default.toLiteral { values -> values.toListLiteral(inner, config) }

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

        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.DiscriminatedObject,
        is Model.FreeFormJson,
        is Model.Object,
        is Model.Primitive.Unit,
        is Model.Reference,
        is Model.AnyOf,
        is Model.OneOf,
        is Model.Uuid -> null
    }

private fun String.escapeForKdoc(): String =
    replace("%", "%%")
