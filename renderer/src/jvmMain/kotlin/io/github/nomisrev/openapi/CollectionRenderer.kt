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

internal fun List<String>.toListLiteral(inner: Model, config: RenderConfig): CodeBlock? =
    when {
        isEmpty() -> CodeBlock.of("emptyList()")
        else -> {
            val entries = map { value -> inner.collectionEntryLiteral(value, config) }
            if (entries.any { it == null }) {
                null
            } else {
                CodeBlock.builder()
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
        }
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
