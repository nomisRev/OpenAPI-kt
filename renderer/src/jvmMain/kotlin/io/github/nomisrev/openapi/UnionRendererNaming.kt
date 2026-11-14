package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.isTopLevel

@Suppress("CyclomaticComplexMethod")
internal fun Model.Union.Case.caseSimpleName(
    config: RenderConfig,
    collidingSingleDiscriminatorTags: Set<String> = emptySet(),
): String =
    discriminatorValues.singleOrNull()
        ?.takeUnless { it in collidingSingleDiscriminatorTags }
        ?.toPascalCase()
        ?.takeIf { it.isNotBlank() }
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
            is Model.ByteArray, is Model.RawResponse -> "CaseBinary"
            is Model.Date -> "CaseDate"
            is Model.DateTime -> "CaseDateTime"
            is Model.Uuid -> "CaseUuid"
            is Model.FreeFormJson -> "CaseJsonElement"
            is Model.Collection -> caseModel.collectionCaseSimpleName(config)
            is Model.Dictionary -> "Case${caseModel.inner.collectionEntryName(config)}Map"
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

/**
 * The class names of the cases of one union, in order, all different: a name a case shares with an earlier one gets the
 * first free number from 2. [caseSimpleName] is a constant per Kotlin type for a case without a context of its own, so
 * a collision the typed layer let through would otherwise be two declarations of one class. The typed layer separates
 * cases, so this is a safety net; [uniqueCaseNameRenames] reports where it was needed.
 */
internal fun List<Model.Union.Case>.caseSimpleNames(
    config: RenderConfig,
    collidingSingleDiscriminatorTags: Set<String> = collidingSingleDiscriminatorTags(),
): List<String> {
    val names = map { it.caseSimpleName(config, collidingSingleDiscriminatorTags) }
    val taken = names.toMutableSet()
    val seen = mutableSetOf<String>()
    return names.map { name ->
        if (seen.add(name)) name
        else generateSequence(2) { it + 1 }.map { "$name$it" }.first { it !in taken }.also {
            taken += it
            seen += it
        }
    }
}

/** The case names of [this] union that [caseSimpleNames] had to number, as `first -> numbered`. */
internal fun Model.Union.uniqueCaseNameRenames(config: RenderConfig): List<Pair<String, String>> {
    val colliding = cases.collidingSingleDiscriminatorTags()
    return cases.map { it.caseSimpleName(config, colliding) }.zip(cases.caseSimpleNames(config, colliding))
        .filter { [base, unique] -> base != unique }
}

internal fun List<Model.Union.Case>.collidingSingleDiscriminatorTags(): Set<String> =
    mapNotNull { it.discriminatorValues.singleOrNull() }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys

internal fun Model.Union.Case.serialNameOrNull(@Suppress("UNUSED_PARAMETER") discriminatorField: String): String? =
    discriminatorValues.singleOrNull()

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
        is Model.ByteArray, is Model.RawResponse -> "Binaries"
        is Model.Date -> "Dates"
        is Model.DateTime -> "DateTimes"
        is Model.Uuid -> "Uuids"
        is Model.FreeFormJson -> "JsonElements"
        is Model.Collection -> "${inner.collectionEntryName(config)}List"
        is Model.Dictionary -> "${inner.collectionEntryName(config)}Map"
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
