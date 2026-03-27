package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.isTopLevel

@Suppress("CyclomaticComplexMethod")
internal fun Model.Union.Case.caseSimpleName(config: RenderConfig): String =
    discriminatorValues.singleOrNull()?.toPascalCase()?.takeIf { it.isNotBlank() }
        ?: model.unionCaseValueOrNull()?.toPascalCase()?.takeIf { it.isNotBlank() }
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

internal fun Model.Union.Case.serialNameOrNull(discriminatorField: String): String? =
    discriminatorValues.singleOrNull() ?: discriminatorValueFromInlineObject(discriminatorField) ?: model.unionCaseValueOrNull()

private fun Model.Union.Case.discriminatorValueFromInlineObject(discriminatorField: String): String? {
    val discriminatorModel = (model as? Model.Object)
        ?.properties
        ?.get(discriminatorField)
        ?.model as? Model.Enum
    return discriminatorModel?.values?.singleOrNull()
}

internal fun Model.unionCaseValueOrNull(): String? =
    (((this as? Model.ContextHolder)?.context?.nested?.lastOrNull()) as? NamingContext.UnionCase)?.value

internal fun Model.Collection.collectionCaseSimpleName(config: RenderConfig): String =
    "Case${inner.collectionEntryName(config)}"

internal fun Model.collectionEntryName(config: RenderConfig): String =
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

internal fun TypeSpec.Builder.addSerialNameAnnotation(value: String): TypeSpec.Builder =
    addAnnotation(
        AnnotationSpec.builder(kotlinx.serialization.SerialName::class)
            .addMember("%S", value)
            .build()
    )

internal fun String.unionEscapeForKdoc(): String =
    replace("%", "%%")

internal fun String.ktStringLiteral(): String =
    replace("\\", "\\\\").replace("\"", "\\\"")
