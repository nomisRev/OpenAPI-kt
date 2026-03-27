@file:Suppress("TooManyFunctions")

package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.schemaName
import io.github.nomisrev.openapi.toPascalCase

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
    return buildOneOf(context, subtypes)
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.buildOneOf(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model.OneOf {
    val discriminator = resolveUnionDiscriminator(subtypes)
    val dispatch = classifyDispatch(discriminator)
    val cases = buildUnionCases(context, subtypes, discriminator, dispatch)
    return Model.OneOf(
        name,
        cases,
        default(),
        description(),
        schema.title,
        dispatch,
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.buildAnyOf(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model.AnyOf {
    val discriminator = resolveUnionDiscriminator(subtypes)
    val dispatch = classifyDispatch(discriminator)
    val cases = buildUnionCases(context, subtypes, discriminator, dispatch)
    return Model.AnyOf(
        name,
        cases,
        default(),
        description(),
        schema.title,
        dispatch,
        isNullable
    )
}

private data class UnionDiscriminator(
    val propertyName: String,
    val caseValues: List<Set<String>>,
)

private val PreferredInferredDiscriminatorNames = listOf("type", "event", "role", "object", "kind", "\$type")

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.resolveUnionDiscriminator(
    subtypes: List<ReferenceOr<Schema>>,
): UnionDiscriminator? {
    val uniqueSubtypes = subtypes.distinct()
    val explicit = schema.discriminator?.propertyName?.let { propertyName ->
        UnionDiscriminator(
            propertyName = propertyName,
            caseValues = uniqueSubtypes.map { subtype ->
                schema.discriminator.discriminatorValueForSubtype(subtype)
            }
        )
    }
    return explicit ?: uniqueSubtypes.inferTagOnlyDiscriminatorOrNull()
}

private fun classifyDispatch(discriminator: UnionDiscriminator?): UnionDispatch = when {
    discriminator == null -> UnionDispatch.Structural
    discriminator.caseValues.all { it.size == 1 } &&
            discriminator.caseValues.map { it.single() }.distinct().size == discriminator.caseValues.size ->
        UnionDispatch.NativeDiscriminator(discriminator.propertyName)

    else -> UnionDispatch.TaggedCustom(discriminator.propertyName)
}

context(ctx: Registry.Scope)
@Suppress("ReturnCount")
private suspend fun List<ReferenceOr<Schema>>.inferTagOnlyDiscriminatorOrNull(): UnionDiscriminator? {
    if (size < 2) return null
    if (any { it !is ReferenceOr.Reference }) return null

    val caseLiterals = map { subtype ->
        val subtypeSchema = subtype.peek().flattenAllOfForUnionDiscriminator()
        val properties = subtypeSchema.properties ?: return null
        val required = subtypeSchema.required.toSet()
        properties.mapNotNull { (propertyName, propertySchema) ->
            val property = propertySchema.peek()
            val literals = property.enum.orEmpty().filterNotNull().toSet()
            val isStringLike = property.type == null || property.type == Type.Basic.String
            if (propertyName !in required || literals.isEmpty() || !isStringLike) null
            else propertyName to literals
        }.toMap()
    }

    val commonPropertyNames = caseLiterals
        .map { it.keys }
        .reduce(Set<String>::intersect)

    val discriminators = commonPropertyNames.mapNotNull { propertyName ->
        val values = caseLiterals.map { it[propertyName] }
        if (values.any { it == null } || values.filterNotNull().flatten().distinct().size <= 1) null
        else UnionDiscriminator(propertyName, values.filterNotNull())
    }

    return discriminators.firstOrNull { it.propertyName in PreferredInferredDiscriminatorNames }
        ?: discriminators.singleOrNull()
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.buildUnionCases(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
    discriminator: UnionDiscriminator?,
    dispatch: UnionDispatch,
): List<Model.Union.Case> {
    val uniqueSubtypes = subtypes.distinct()
    val caseDiscriminators = discriminator?.caseValues ?: List(uniqueSubtypes.size) { emptySet() }
    val unionContexts = uniqueSubtypes.mapIndexed { index, refOrSchema ->
        caseDiscriminators[index]
            .singleOrNull()
            .takeIf { dispatch !is UnionDispatch.Structural }
            ?.let { name.nest(NamingContext.UnionCase(it)) }
            ?: name.unionCase(index, refOrSchema, uniqueSubtypes, context)
    }

    /**
     * We need to make unionCase NamingContext name generation more powerful:
     *  1. For OpenEnum we want to non-string case to inherit the outer name:
     *      -> "model": { oneOf[{ type: String},{ type: String, enum: [a, b]  }] }
     *      => sealed interface Model should have 2 subtypes CaseString & CaseModel.
     *
     *  2. For similar cases like "open-enum" but with complex types:
     *    -> "event": { oneOf[{ -$ref:EventA }, { -$ref:EventB }, { type: object, properties={} }]
     *    => sealed interface Event with 3 subtypes EventA, EventB, and CaseEvent
     *
     *  So in this case whenever there are n casses, and n-1 cases are references,
     *  then the non-referenced case should inherit the outer name.
     */
    val cases = uniqueSubtypes.mapIndexed { index, subtype ->
        subtype.resolve(unionContexts[index], context) {
            val discriminatorValues = caseDiscriminators[index]
            val model = when (dispatch) {
                is UnionDispatch.NativeDiscriminator -> it.toDiscriminatedUnionCaseModel(
                    context = context,
                    caseContext = unionContexts[index],
                    discriminatorField = dispatch.propertyName,
                    caseDiscriminator = discriminatorValues.singleOrNull(),
                )

                UnionDispatch.Structural,
                is UnionDispatch.TaggedCustom -> it.toModel(context, false)
            }
            Model.Union.Case(model, discriminatorValues)
        }
    }
    return cases
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.toDiscriminatedUnionCaseModel(
    context: SchemaContext,
    caseContext: NamingContext,
    discriminatorField: String,
    caseDiscriminator: String?,
): Model {
    val normalizedSchema = schema.normalizedDiscriminatedUnionCase(
        discriminatorField = discriminatorField,
        caseDiscriminator = caseDiscriminator,
    )
    val normalized = when (this) {
        is ResolvedSchema.Reference,
        is ResolvedSchema.Value -> ResolvedSchema.Value(caseContext, normalizedSchema)

        is ResolvedSchema.Recursive -> ResolvedSchema.Recursive(caseContext, normalizedSchema)
    }
    return normalized.toModel(context, true)
}

context(ctx: Registry.Scope)
private suspend fun Schema.normalizedDiscriminatedUnionCase(
    discriminatorField: String,
    caseDiscriminator: String?,
): Schema =
    flattenAllOfForUnionDiscriminator()
        .stripTagOnlyDiscriminatorProperty(discriminatorField, caseDiscriminator)
        .hoistSingleRemainingObjectProperty()

context(ctx: Registry.Scope)
private suspend fun Schema.flattenAllOfForUnionDiscriminator(): Schema =
    allOf
        ?.takeIf { it.isNotEmpty() }
        ?.let { allOfSchemas ->
            (allOfSchemas.map { it.peek().flattenAllOfForUnionDiscriminator() } + copy(allOf = null))
                .reduce { acc, schema ->
                    acc.merge(schema).copy(required = (acc.required + schema.required).distinct())
                }
        }
        ?: this

context(ctx: Registry.Scope)
private suspend fun Schema.stripTagOnlyDiscriminatorProperty(
    discriminatorField: String,
    caseDiscriminator: String?,
): Schema {
    val currentProperties = properties ?: return this
    val discriminatorProperty = currentProperties[discriminatorField] ?: return this
    if (!discriminatorProperty.isTagOnlyDiscriminator(caseDiscriminator)) return this

    return copy(
        properties = currentProperties - discriminatorField,
        required = required - discriminatorField,
        additionalProperties = additionalProperties ?: AdditionalProperties.Allowed(false),
    )
}

context(ctx: Registry.Scope)
private suspend fun ReferenceOr<Schema>.isTagOnlyDiscriminator(caseDiscriminator: String?): Boolean {
    val propertySchema = peek()
    return when {
        propertySchema.enum?.singleOrNull() != null -> true
        caseDiscriminator != null && propertySchema.type == Type.Basic.String && propertySchema.enum == null -> true
        else -> false
    }
}

context(ctx: Registry.Scope)
private suspend fun Schema.hoistSingleRemainingObjectProperty(): Schema {
    val currentProperties = properties ?: return this
    if (currentProperties.size != 1) return this

    val (propertyName, propertySchema) = currentProperties.entries.single()
    if (required.size != 1 || required.single() != propertyName) return this

    val hoistedSchema = propertySchema.peek()
    if (!hoistedSchema.isHoistableSinglePropertyObject()) return this

    return copy(
        properties = hoistedSchema.properties,
        required = hoistedSchema.required,
        additionalProperties = hoistedSchema.additionalProperties,
    )
}

private fun Schema.isHoistableSinglePropertyObject(): Boolean =
    type == Type.Basic.Object &&
            properties?.isNotEmpty() == true &&
            items == null &&
            oneOf.isNullOrEmpty() &&
            anyOf.isNullOrEmpty() &&
            allOf.isNullOrEmpty() &&
            enum.isNullOrEmpty() &&
            when (val value = additionalProperties) {
                null -> true
                is AdditionalProperties.Allowed -> !value.value
                is AdditionalProperties.PSchema -> false
            }

@Suppress("CyclomaticComplexMethod", "UnsafeCallOnNullableType")
context(ctx: Registry.Scope)
suspend fun NamingContext.unionCase(
    index: Int,
    subtype: ReferenceOr<Schema>,
    allSubtypes: List<ReferenceOr<Schema>>,
    context: SchemaContext
): NamingContext {
    val schema = subtype.peek()

    if (schema.type == Type.Basic.Array && schema.items != null) {
        return when (val refOrSchema = schema.items!!) {
            is ReferenceOr.Reference -> NamingContext.reference(refOrSchema.ref.schemaName(), context)
            is ReferenceOr.Value<Schema> ->
                if (refOrSchema.value.isComplexCompositeUnionCase()) {
                    nest(NamingContext.UnionCase(refOrSchema.value.unionCaseName(index)))
                } else {
                    unionCase(index, refOrSchema, allSubtypes, context)
                }
        }
    }

    val discriminatorValue: String? = schema.discriminator?.mapping?.let { discriminator ->
        when (subtype) {
            is ReferenceOr.Reference -> discriminator[subtype.ref.schemaName()]
            is ReferenceOr.Value<Schema> -> null
        }
    }

    val specialName = schema.properties
        ?.entries
        ?.firstOrNull { (key, _) -> key in setOf("type", "event", $$"$type") }
        ?.let { (_, refOrSchema) -> refOrSchema.peek().enum?.singleOrNull() }

    @Suppress("MagicNumber")
    val enumName =
        schema.enum?.joinToString(prefix = "", separator = "Or") {
            it?.replaceFirstChar(Char::uppercaseChar).orEmpty()
        }.takeIf { (it?.length ?: 0) < 90 }

    /**
     * Checks if this is an OpenEnum pattern where n-1 cases are plain string types
     * and 1 case is a string enum. Returns true if current subtype is the enum case.
     */
    suspend fun isOpenEnumCase(): Boolean {
        if (allSubtypes.size < 2) return false
        val schemas = allSubtypes.map { it.peek() }
        val stringTypes = schemas.filter { it.type == Type.Basic.String && it.enum == null }
        val enumTypes = schemas.filter { it.type == Type.Basic.String && it.enum != null }
        return stringTypes.size == allSubtypes.size - 1 &&
                enumTypes.size == 1 &&
                schema.type == Type.Basic.String &&
                schema.enum != null
    }

    /**
     * Checks if this is a reference pattern where n-1 cases are references
     * and 1 case is a non-reference (value). Returns true if current subtype is the non-reference case.
     */
    fun isNonReferenceCaseInRefPattern(): Boolean {
        val referenceCount = allSubtypes.count { it is ReferenceOr.Reference }
        val valueCount = allSubtypes.count { it is ReferenceOr.Value }
        return referenceCount == allSubtypes.size - 1 &&
                valueCount == 1 &&
                subtype is ReferenceOr.Value &&
                schema.type == Type.Basic.Object
    }

    /**
     * Checks if this is a reference pattern where n-1 cases are references
     * and 1 case is a non-reference (value). Returns true if current subtype is the non-reference case.
     */
    suspend fun isObjectInPrimitivePattern(): Boolean {
        val referenceCount = allSubtypes.count { it.peek().type in primitives }
        val valueCount = allSubtypes.count { it is ReferenceOr.Value }
        return referenceCount == allSubtypes.size - 1 &&
                valueCount == 1 &&
                subtype is ReferenceOr.Value
    }

    val name = when {
        allSubtypes.size == 1 -> "Case"
        discriminatorValue != null -> discriminatorValue
        specialName != null -> specialName
        isOpenEnumCase() -> enumName ?: "CaseEnum"
        isNonReferenceCaseInRefPattern() -> "CaseElse"
        else -> schema.unionCaseName(index)
    }

    return nest(NamingContext.UnionCase(name))
}

private val primitives = setOf(Type.Basic.String, Type.Basic.Number, Type.Basic.Boolean, Type.Basic.Integer)

private const val MAX_CODE_LENGTH = 90

/**
 * Generates a union case name based on the schema type and structure.
 * For objects: joins property names with "And" (e.g., `AgeAndName`)
 * For primitives: uses type-based names (e.g., `CaseInt`, `CaseString`)
 * For enums: joins enum values with "Or" (e.g., `AscOrDesc`)
 */
@Suppress(
    "CyclomaticComplexMethod",
    "CastNullableToNonNullableType",
    "ReturnCount"
)
private fun Schema.unionCaseName(index: Int): String {
// TODO: this seems not correct here
    title?.toPascalCase()?.takeIf { it.isNotBlank() }?.let { return it }
    compositeUnionCaseName(index)?.let { return it }
    return when (type) {
        Type.Basic.Object -> specialUnionCaseName() ?: objectUnionCaseName(index)

        Type.Basic.Number -> if (format == "float") "CaseFloat" else "CaseDouble"
        Type.Basic.Boolean -> "CaseBoolean"
        Type.Basic.Integer -> if (format == "int32") "CaseInt" else "CaseLong"

        Type.Basic.String -> when {
            enum != null -> enum?.joinToString(separator = "Or") {
                it?.toPascalCase().orEmpty()
            }?.takeIf { it.length < MAX_CODE_LENGTH } ?: caseIndex.getOrElse(index) { "Case$index" }

            format == "binary" -> "CaseBinary"
            format == "uuid" -> "CaseUuid"
            format == "date" -> "CaseDate"
            format == "date-time" -> "CaseDateTime"
            else -> format?.takeIf { it.isNotBlank() }?.let { "Case${it.toPascalCase()}" } ?: "CaseString"
        }

        Type.Basic.Array -> "CaseArray"
        Type.Basic.Null -> "CaseNull"

        is Type.Array -> (type as Type.Array).types.joinToString(
            prefix = "Case",
            separator = "Or"
        ) { it.name.toPascalCase() }.takeIf { it.length < MAX_CODE_LENGTH }
            ?: caseIndex.getOrElse(index) { "Case$index" }

        null -> caseIndex.getOrElse(index) { "Case$index" }
    }
}

private fun Schema.compositeUnionCaseName(index: Int): String? {
    val branches = oneOf.orEmpty().ifEmpty { anyOf.orEmpty() }.ifEmpty { allOf.orEmpty() }
    if (branches.size < 2) return null

    val names = branches.mapIndexedNotNull { branchIndex, refOrSchema ->
        val schema = refOrSchema.valueOrNull() ?: return@mapIndexedNotNull when (refOrSchema) {
            is ReferenceOr.Reference -> refOrSchema.ref.schemaName().toPascalCase().takeIf { it.isNotBlank() }
            is ReferenceOr.Value -> null
        }

        schema.unionCaseName(branchIndex).removePrefix("Case").takeIf { it.isNotBlank() }
    }

    return names.distinct()
        .takeIf { it.size >= 2 }
        ?.joinToString(separator = "Or")
        ?.takeIf { it.length < MAX_CODE_LENGTH }
        ?: caseIndex.getOrElse(index) { "Case$index" }
}

private fun Schema.specialUnionCaseName(): String? =
    properties
        ?.entries
        ?.firstOrNull { (key, _) -> key in setOf("type", "event", "\$type") }
        ?.let { (_, refOrSchema) -> refOrSchema.valueOrNull()?.enum?.singleOrNull()?.toPascalCase() }
        ?.takeIf { it.isNotBlank() }

private fun Schema.objectUnionCaseName(index: Int): String {
    val props = properties?.entries.orEmpty()
    if (props.size == 1) {
        val (name, refOrSchema) = props.single()
        val suffix = refOrSchema.valueOrNull()?.objectUnionPropertySuffix()
        if (!suffix.isNullOrBlank()) {
            return "${name.toPascalCase()}$suffix"
        }
    }
    return props.joinToString(prefix = "", separator = "And") { (name, _) -> name.toPascalCase() }
        .takeIf { it.isNotBlank() && it.length < MAX_CODE_LENGTH }
        ?: caseIndex.getOrElse(index) { "Case$index" }
}

private fun Schema.objectUnionPropertySuffix(): String? = when (type) {
    Type.Basic.Array -> items?.valueOrNull()?.collectionEntrySuffix()
    Type.Basic.Object,
    Type.Basic.Number,
    Type.Basic.Boolean,
    Type.Basic.Integer,
    Type.Basic.Null,
    Type.Basic.String,
    is Type.Array,
    null -> null
}

private fun Schema.collectionEntrySuffix(): String? = when (type) {
    Type.Basic.String -> "Strings"
    Type.Basic.Integer -> if (format == "int32") "Ints" else "Longs"
    Type.Basic.Number -> if (format == "float") "Floats" else "Doubles"
    Type.Basic.Boolean -> "Booleans"
    Type.Basic.Object ->
        properties?.keys
            ?.joinToString(separator = "And") { it.toPascalCase() }
            ?.takeIf(String::isNotBlank)
            ?.pluralizeCaseName()

    Type.Basic.Array -> items?.valueOrNull()?.collectionEntrySuffix()?.removeSuffix("s")?.plus("List")
    Type.Basic.Null,
    is Type.Array,
    null -> null
}

private fun String.pluralizeCaseName(): String =
    when {
        endsWith("s") -> this
        endsWith("y") && length > 1 && this[length - 2].lowercaseChar() !in setOf('a', 'e', 'i', 'o', 'u') ->
            dropLast(1) + "ies"

        else -> this + "s"
    }

private fun Schema.isComplexCompositeUnionCase(): Boolean =
    type == null &&
            enum == null &&
            (
                    oneOf.orEmpty().isNotEmpty() ||
                            anyOf.orEmpty().isNotEmpty() ||
                            allOf.orEmpty().isNotEmpty()
                    )

val caseIndex = listOf(
    "One",
    "Two",
    "Three",
    "Four",
    "Five",
    "Six",
    "Seven",
    "Eight",
    "Nine",
    "Ten",
    "Eleven",
    "Twelve",
    "Thirteen",
    "Fourteen",
    "Fifteen",
    "Sixteen",
)

private const val SCHEMA_REF_PREFIX = "#/components/schemas/"

private fun String.schemaRefNameOrSelf(): String =
    if (startsWith(SCHEMA_REF_PREFIX)) schemaName() else this

context(ctx: Registry.Scope)
@Suppress("ReturnCount")
private suspend fun Schema.Discriminator?.discriminatorValueForSubtype(
    subtype: ReferenceOr<Schema>
): Set<String> {
    if (this == null) return emptySet()

    val mapped = when (subtype) {
        is ReferenceOr.Reference -> {
            val subtypeRefName = subtype.ref.schemaRefNameOrSelf()
            mapping
                ?.entries
                ?.filter { (_, ref) ->
                    ref == subtype.ref || ref.schemaRefNameOrSelf() == subtypeRefName
                }
                ?.mapTo(linkedSetOf()) { (key, _) -> key }
        }

        is ReferenceOr.Value<Schema> -> null
    }
    if (!mapped.isNullOrEmpty()) return mapped

    val discriminatorProperty = propertyName.let { propertyName ->
        subtype.peek()
            .flattenAllOfForUnionDiscriminator()
            .properties
            ?.get(propertyName)
            ?.peek()
    }
    val discriminatorLiteral = discriminatorProperty?.enum?.singleOrNull()
    if (discriminatorLiteral != null) return setOf(discriminatorLiteral)

    val discriminatorValues = discriminatorProperty
        ?.enum
        .orEmpty()
        .filterNotNull()
        .toSet()
    if (discriminatorValues.isNotEmpty()) return discriminatorValues

    return when (subtype) {
        is ReferenceOr.Reference -> subtype.ref.schemaRefNameOrSelf().takeUnless { it == "#" }?.let(::setOf).orEmpty()
        is ReferenceOr.Value<Schema> -> emptySet()
    }
}
