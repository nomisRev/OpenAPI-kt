@file:Suppress("TooManyFunctions")

package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.effectiveMaximum
import io.github.nomisrev.openapi.effectiveMinimum
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.merge
import io.github.nomisrev.openapi.mergeMaximumBound
import io.github.nomisrev.openapi.mergeMinimumBound
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.pipeline.plugins.implicit.isImplicitCollection
import io.github.nomisrev.openapi.registry.AllOfNullability
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.allOfNullability
import io.github.nomisrev.openapi.registry.combine
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.isNonStructural
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.schemaName
import kotlin.collections.component1
import kotlin.collections.component2

@Suppress("ReturnCount", "UnsafeCallOnNullableType")
context(scope: Registry.Scope)
suspend fun Schema.discriminatedSubtypeOrNull(context: SchemaContext, name: String): NamingContext? {
    if (allOf.orEmpty().isEmpty()) return null

    val references = allOf!!.mapNotNull { refOrSchema ->
        when (refOrSchema) {
            is ReferenceOr.Reference -> refOrSchema
            is ReferenceOr.Value<Schema> -> null
        }
    }

    val singleReference = references.singleOrNull() ?: return null
    val singlePeeked = singleReference.peek()
    return when (singleReference) {
        else if singlePeeked.allOf.orEmpty().size in 1..2 -> singlePeeked.discriminatedSubtypeOrNull(context, name)
        else if singlePeeked.discriminator?.mapping.orEmpty().containsValue("#/components/schemas/$name") ->
            NamingContext(
                NamingContext.Reference(singleReference.ref.schemaName(), context),
                listOf(NamingContext.DiscriminatedObjectCase(name))
            )

        else -> null
    }
}

@Suppress("UnsafeCallOnNullableType")
context(scope: Registry.Scope)
suspend fun ResolvedSchema.toDiscriminatedObject(context: SchemaContext): Model.DiscriminatedObject {
    val discriminator = requireNotNull(schema.discriminator) { "Discriminator required for discriminated object" }
    val mapping = requireNotNull(discriminator.mapping) { "Discriminator mapping required" }
    require(this is ResolvedSchema.Reference) { "Expected reference for discriminated object" }

    val mappings = mapping.filterValues { it != "#/components/schemas/${reference.name}" }

    val abstractProperties = properties(schema.properties!!, context)
        .filter { [name, prop] -> prop.model !is Model.FreeFormJson && name != discriminator.propertyName }

    val overrides = mutableListOf<Model.DiscriminatedObject.PropertyOverride>()
    val subtypes = mappings.map { [mappedName, ref] ->
        val name = name.nest(NamingContext.DiscriminatedObjectCase(mappedName))
        val subtypeSchema = ReferenceOr.Reference(ref).peek()
        val allOf = requireNotNull(subtypeSchema.allOf) { "Subtype schema must be allOf" }

        val parentClass = allOf.single { it.isSubtype(mapping) }
        val [superRequired, superProps, origins] =
            parentClass.collectSuperTypeProperties(mapping, context, overrides)

        val peeked =
            (allOf - parentClass).singleOrNull()?.peek() ?: Schema()
        val peekedProperties =
            peeked.properties.orEmpty().filter { [_, refOrSchema] -> refOrSchema.takeIf(context) != null }

        val properties =
            superProps.merge(peekedProperties) { property, inherited, declared ->
                overrideProperty(
                    PropertyDeclaration(ref.schemaName(), declared),
                    PropertyDeclaration(origins[property] ?: parentClass.schemaName(), inherited),
                    property,
                    overrides,
                )
            }.filter { [name, _] -> name != discriminator.propertyName }

        ResolvedSchema.Value(
            name,
            peeked.copy(
                properties = properties,
                required = superRequired + peeked.required
            )
        )   // TODO: To remove 'resolveReference' make this toObject to work with resolveReference = false
            //  It's the only place where indirect recursion due to subtype relationship is broken and results in OOM
            .let {
                it.toObject(context, properties)
//                scope.registry().engine.transform(scope, scope.registry(), it, context, false) as Model.Object
            }
    }

    return Model.DiscriminatedObject(
        name,
        abstractProperties,
        listOf(simpleCase(mapping, abstractProperties)) + subtypes,
        description(),
        schema.title,
        discriminator.propertyName,
        isNullable,
        // An intermediate subtype's override is found again for each subtype that extends it.
        overrides.distinct(),
    )
}

fun Schema.isEmpty(): Boolean =
    properties.isNullOrEmpty() &&
            additionalProperties == null &&
            enumLikeValues().isNullOrEmpty() &&
            items == null &&
            anyOf.isNullOrEmpty() &&
            allOf.isNullOrEmpty() &&
            oneOf.isNullOrEmpty()

/*
 * This includes properties from intermediate parents, but intermediate parents are currently not generated.
 * See: BundleElement, LocalizableBundleElement, StateBundleElement in youtrack.json.
 * Where StateBundleElement is actually a subtype of LocalizableBundleElement that is a subtype of the top parent BundleElement.
 * In our current implementation StateBundleElement is just a child of BundleElement,
 * but in this function we ensure it also has the properties of `LocalizableBundleElement`.
 * As a downside, you cannot match on `LocalizableBundleElement` and also cover `StateBundleElement` but need to explicitly cover both.
 * This cannot conveniently be modeled in Kotlin,
 * and the additional boilerplate during 'when' is simpler than the more complex data structure.
 */
@Suppress("UnsafeCallOnNullableType")
context(scope: Registry.Scope)
private tailrec suspend fun ReferenceOr<Schema>.collectSuperTypeProperties(
    mapping: Map<String, String>,
    context: SchemaContext,
    overrides: MutableList<Model.DiscriminatedObject.PropertyOverride>,
    properties: SuperTypeProperties = SuperTypeProperties(emptyList(), emptyMap(), emptyMap())
): SuperTypeProperties {
    val parentSchema = peek()
    val parentName = schemaName()
    val superTypeOrNull = parentSchema.allOf?.singleOrNull { it.isSubtype(mapping) }
    return if (superTypeOrNull != null) {
        val schema = (parentSchema.allOf!! - superTypeOrNull).singleOrNull()?.peek()
        val schemaProps = schema?.properties.orEmpty()
            .filter { [_, refOrSchema] -> refOrSchema.takeIf(context) != null }

        superTypeOrNull.collectSuperTypeProperties(
            mapping,
            context,
            overrides,
            SuperTypeProperties(
                properties.required + schema?.required.orEmpty(),
                properties.properties.merge(schemaProps) { property, declared, inherited ->
                    overrideProperty(
                        PropertyDeclaration(properties.origins.getValue(property), declared),
                        PropertyDeclaration(parentName, inherited),
                        property,
                        overrides,
                    )
                },
                schemaProps.mapValues { parentName } + properties.origins,
            )
        )
    } else {
        SuperTypeProperties(
            properties.required + parentSchema.required,
            parentSchema.properties.orEmpty().merge(properties.properties) { property, inherited, declared ->
                overrideProperty(
                    PropertyDeclaration(properties.origins.getValue(property), declared),
                    PropertyDeclaration(parentName, inherited),
                    property,
                    overrides,
                )
            },
            parentSchema.properties.orEmpty().mapValues { parentName } + properties.origins,
        )
    }
}

/**
 * The properties a discriminated subtype inherits through its parents in the discriminator mapping, the properties
 * they require, and for each property the schema whose declaration it is ([origins]), the most specific one.
 */
private data class SuperTypeProperties(
    val required: List<String>,
    val properties: Map<String, ReferenceOr<Schema>>,
    val origins: Map<String, String>,
)

/** Property [declaration] of schema [schema]. */
private class PropertyDeclaration(val schema: String, val declaration: ReferenceOr<Schema>)

private fun ReferenceOr<Schema>.schemaName(): String = when (this) {
    is ReferenceOr.Reference -> ref.schemaName()
    is ReferenceOr.Value<Schema> -> error("Expected a \$ref to a schema in the discriminator mapping, but got $value")
}

/**
 * Merges the declaration of [property] a discriminated subtype makes ([own]) with the one it inherits from a parent in
 * the discriminator mapping ([inherited]). A compatible declaration is merged as [combineProperty] merges any two.
 * One that contradicts the inherited declaration, so that no value satisfies both, overrides it like a subclass does:
 * the subtype's declaration is the property, and the override is recorded in [overrides] to be reported. YouTrack
 * declares `ActivityItem.added: {type: object}` as a placeholder that each subtype redeclares (`{type: array}`,
 * `{type: integer}`, a `$ref`). Outside a discriminated hierarchy such a contradiction is still rejected.
 */
context(ctx: Registry.Scope)
private suspend fun overrideProperty(
    own: PropertyDeclaration,
    inherited: PropertyDeclaration,
    property: String,
    overrides: MutableList<Model.DiscriminatedObject.PropertyOverride>,
): ReferenceOr<Schema> =
    try {
        inherited.declaration.combine(own.declaration)
    } catch (e: ContradictoryDeclarations) {
        overrides += Model.DiscriminatedObject.PropertyOverride(
            schema = own.schema,
            parent = inherited.schema,
            property = property,
            inherited = inherited.declaration.toJson(),
            declared = own.declaration.toJson(),
            reason = e.message.orEmpty(),
        )
        own.declaration
    } catch (e: IllegalArgumentException) {
        throw e.wrap("allOf members declare property '$property' incompatibly: ${e.message}")
    }

private fun ReferenceOr<Schema>.toJson(): String = when (this) {
    is ReferenceOr.Reference -> $$"""{"$ref":"$$ref"}"""
    is ReferenceOr.Value<Schema> -> value.toString()
}

/**
 * Two declarations no value satisfies both of, such as `string` and `integer`, two `$ref`s that do not extend one
 * another, disjoint enums or different formats, as opposed to a merge that fails for another reason.
 */
private class ContradictoryDeclarations(message: String, cause: Throwable? = null) :
    IllegalArgumentException(message, cause)

/** Prefixes this error with [message], keeping it a [ContradictoryDeclarations] when it is one. */
private fun IllegalArgumentException.wrap(message: String): IllegalArgumentException =
    if (this is ContradictoryDeclarations) ContradictoryDeclarations(message, this)
    else IllegalArgumentException(message, this)

/**
 * Merges the declarations of property [name] by two schemas an `allOf` combines. The property's value must validate
 * against both declarations, so the result is their intersection, independent of the order the members list them in:
 *  - two inline schemas are merged ([merge]): nullable only when both permit `null`, constraints intersect, and
 *    `format`, `enum`, `readOnly` and `writeOnly` of either apply.
 *  - a `$ref` stays that `$ref` when the inline schema only annotates, constrains or restates its type, and nullability
 *    agrees. Constraints of the inline schema cannot change the referenced type, so they are not represented.
 *    Otherwise the inline schema adds structure, or excludes `null` the referenced type permits, and the result is
 *    the inline merge of the referenced schema with it.
 *  - of two `$ref`s the one that extends the other (`allOf`, transitively) is kept, it validates against both.
 *    Two `$ref`s that do not extend one another are an intersection of two named types no generated type expresses,
 *    and are rejected rather than resolved by picking one.
 * Declarations no value satisfies, such as `string` and `integer`, are rejected.
 */
context(ctx: Registry.Scope)
private suspend fun combineProperty(
    name: String,
    first: ReferenceOr<Schema>,
    second: ReferenceOr<Schema>,
): ReferenceOr<Schema> =
    try {
        first.combine(second)
    } catch (e: IllegalArgumentException) {
        throw e.wrap("allOf members declare property '$name' incompatibly: ${e.message}")
    }

context(ctx: Registry.Scope)
private suspend fun ReferenceOr<Schema>.combine(
    other: ReferenceOr<Schema>,
): ReferenceOr<Schema> = when (this) {
    is ReferenceOr.Reference -> when (other) {
        is ReferenceOr.Value<Schema> -> combine(other.value)
        is ReferenceOr.Reference -> combine(other)
    }

    is ReferenceOr.Value<Schema> -> when (other) {
        is ReferenceOr.Reference -> other.combine(value)
        is ReferenceOr.Value<Schema> -> ReferenceOr.value(value.merge(other.value))
    }
}

context(ctx: Registry.Scope)
private suspend fun ReferenceOr.Reference.combine(value: Schema): ReferenceOr<Schema> {
    val target = peek()
    val keepsNullability = listOf(this).allOfNullability().permitsNull() ==
            listOf(this, ReferenceOr.value(value)).allOfNullability().permitsNull()
    return if (keepsNullability && value.restates(target) && !value.requiresOptionalPropertyOf(target)) {
        withAccess(value.readOnly, value.writeOnly)
    } else ReferenceOr.value(mergeTarget(target, value))
}

/**
 * Whether this schema requires a property that [target] declares but leaves optional, so the generated type of a
 * `$ref` to [target] cannot represent it. A `required` naming a property [target] does not declare adds no property,
 * as for a standalone object.
 */
context(ctx: Registry.Scope)
private suspend fun Schema.requiresOptionalPropertyOf(target: Schema): Boolean {
    if (required.isEmpty()) return false
    val expanded = target.expandAllOf()
    return required.any { it in expanded.properties.orEmpty() && it !in expanded.required }
}

context(ctx: Registry.Scope)
private suspend fun ReferenceOr.Reference.combine(other: ReferenceOr.Reference): ReferenceOr<Schema> {
    if (ref == other.ref) return withAccess(other.readOnly, other.writeOnly)
    val target = peek()
    val otherTarget = other.peek()
    val [specific, general] = when {
        target == otherTarget || extends(other) -> this to other
        other.extends(this) -> other to this
        else -> throw ContradictoryDeclarations(
            "${ref.schemaName()} and ${other.ref.schemaName()} do not extend one another, so a value of both is an " +
                    "intersection of two named types that no generated type represents."
        )
    }
    val keepsNullability = listOf(specific).allOfNullability().permitsNull() ==
            listOf(specific, general).allOfNullability().permitsNull()
    return if (keepsNullability) specific.withAccess(general.readOnly, general.writeOnly)
    else ReferenceOr.value(
        specific.mergeTarget(specific.peek(), general.peek()).let {
            it.copy(readOnly = it.readOnly.or(general.readOnly), writeOnly = it.writeOnly.or(general.writeOnly))
        }
    )
}

context(ctx: Registry.Scope)
private suspend fun ReferenceOr.Reference.mergeTarget(target: Schema, value: Schema): Schema =
    try {
        target.merge(value).let { it.copy(readOnly = it.readOnly.or(readOnly), writeOnly = it.writeOnly.or(writeOnly)) }
    } catch (e: IllegalArgumentException) {
        throw e.wrap("${ref.schemaName()} cannot be intersected with $value: ${e.message}")
    }

/** Whether this `$ref` extends [other]: lists it as an `allOf` member, directly or through members it extends. */
context(ctx: Registry.Scope)
private suspend fun ReferenceOr.Reference.extends(
    other: ReferenceOr.Reference,
    visited: Set<String> = setOf(ref),
): Boolean = peek().allOf.orEmpty().filterIsInstance<ReferenceOr.Reference>().any { member ->
    member.ref == other.ref || (member.ref !in visited && member.extends(other, visited + member.ref))
}

private fun ReferenceOr.Reference.withAccess(readOnly: Boolean?, writeOnly: Boolean?): ReferenceOr.Reference =
    copy(readOnly = this.readOnly.or(readOnly), writeOnly = this.writeOnly.or(writeOnly))

private fun Boolean?.or(other: Boolean?): Boolean? = if (this == true || other == true) true else this ?: other

private fun AllOfNullability.permitsNull(): Boolean = this == AllOfNullability.Permits

/**
 * Whether this schema, next to [target], only annotates, constrains or restates [target]'s type: it declares no
 * structure of its own (properties, items, an enum, composites) and its `type` and `format`, if any, are [target]'s.
 */
context(ctx: Registry.Scope)
private suspend fun Schema.restates(target: Schema): Boolean =
    properties == null &&
            additionalProperties == null &&
            items == null &&
            enumLikeValues() == null &&
            allOf.isNullOrEmpty() &&
            anyOf.isNullOrEmpty() &&
            oneOf.isNullOrEmpty() &&
            not == null &&
            discriminator == null &&
            (format == null || format == target.format) &&
            (nonNullType() == null || nonNullType() == target.nonNullType() ||
                    (nonNullType() == Type.Basic.Object && target.nonNullType() == null &&
                            target.isObjectLike(emptySet())))

/** Whether this schema describes objects, itself or through an `allOf` member, like `allOf: [{$ref: Pet}, {...}]`. */
context(ctx: Registry.Scope)
private suspend fun Schema.isObjectLike(visited: Set<String>): Boolean =
    isObjectLike() || allOf.orEmpty().any { member ->
        when (member) {
            is ReferenceOr.Reference -> member.ref !in visited && member.peek().isObjectLike(visited + member.ref)
            is ReferenceOr.Value<Schema> -> member.value.isObjectLike(visited)
        }
    }

private fun Schema.nonNullType(): Type? = when (val type = type) {
    is Type.Array -> type.types.filter { it != Type.Basic.Null }.let { it.singleOrNull() ?: Type.Array(it) }
    else -> type
}

private fun ReferenceOr<Schema>.isSubtype(mapping: Map<String, String>): Boolean =
    if (this is ReferenceOr.Reference) this.ref in mapping.values else false

private fun ResolvedSchema.Reference.simpleCase(
    mapping: Map<String, String>,
    abstractProperties: Map<String, Model.Object.Property>
): Model.Object {
    val mappingName = mapping.entries
        .singleOrNull { [_, ref] -> ref == "#/components/schemas/${reference.name}" }
        ?.key ?: error("Expected exactly one self mapping for discriminated object")

    val selfName =
        if (mappingName == reference.name) name.nest(NamingContext.DiscriminatedObjectCase("Default"))
        else name.nest(NamingContext.DiscriminatedObjectCase(mappingName))

    return Model.Object(
        selfName,
        null,
        null,
        abstractProperties,
        Model.Object.AdditionalProperties.Allowed(false),
        isNullable
    )
}

/**
 * Merges two schemas an `allOf` combines. An instance validates against `allOf` only when it validates against every
 * member, so the merge permits `null` only when neither schema excludes it and one permits it ([allOfNullability]):
 * a `nullable` member next to a non-null one is non-null. Both are merged without their `null` (`nullable`, a `"null"`
 * type, a `null` branch), and the result is `nullable` exactly when the merge permits `null`.
 * Likewise a property is required when either schema requires it, also when the other one declares it: the merged
 * `required` is the union of both, whichever schema lists it first.
 */
context(ctx: Registry.Scope)
suspend fun Schema.merge(other: Schema): Schema {
    if (this == other) return this
    val permitsNull = listOf(allOfNullability(), other.allOfNullability()).combine() == AllOfNullability.Permits
    val left = withoutNull()
    val right = other.withoutNull()
    // `withoutNull` can unwrap `{allOf: [X], nullable: true}` to X, so the required of both forms apply.
    val required = (required + left.required + other.required + right.required).distinct()
    return left.mergeNonNull(right).copy(nullable = if (permitsNull) true else null, required = required)
}

@Suppress("UnsafeCallOnNullableType")
context(ctx: Registry.Scope)
private suspend fun Schema.mergeNonNull(other: Schema): Schema = when {
    this == other -> this
    !allOf.isNullOrEmpty() || !other.allOf.isNullOrEmpty() -> mergeNestedAllOf(other)
    compositeUnionOrNull() != null || other.compositeUnionOrNull() != null -> mergeCompositeUnion(other)
    enumLikeValues()?.isNotEmpty() == true || other.enumLikeValues()?.isNotEmpty() == true -> mergeEnum(other)
    type != null && type == other.type -> when (type!!) {
        Type.Basic.Array -> mergeCommon(other)
            .mergeCollectionConstraints(other)
            .copy(items = let(items, other.items) { a, b -> a.combine(b) })

        Type.Basic.Object -> mergeObject(other)
        Type.Basic.Number -> mergeCommon(other).numberConstraints(other)
        Type.Basic.Boolean -> mergeCommon(other)
        Type.Basic.Integer -> mergeCommon(other).numberConstraints(other)
        Type.Basic.Null -> mergeCommon(other)
        Type.Basic.String -> mergeCommon(other).mergeTextConstraints(other)
        is Type.Array -> mergeCommon(other)
    }

    // Every integer is a number, so only integers satisfy both; the number's format (float, double) does not apply.
    type == Type.Basic.Integer && other.type == Type.Basic.Number ->
        mergeNonNull(other.copy(type = type, format = null))

    type == Type.Basic.Number && other.type == Type.Basic.Integer ->
        copy(type = other.type, format = null).mergeNonNull(other)

    isObjectLike() && other.isObjectLike() -> mergeObject(other)
    isNonStructural() -> other.mergeCommon(this).mergeConstraints(this)
    other.isNonStructural() -> mergeCommon(other).mergeConstraints(other)
    // A schema without `type` that shapes values (`items`, `properties`) is merged as the type it is modelled as.
    type == null || other.type == null ->
        copy(type = impliedType()).mergeNonNull(other.copy(type = other.impliedType()))

    else -> throw ContradictoryDeclarations(
        "No value is both of type ${type.describe()} and of type ${other.type.describe()}."
    )
}

/**
 * The type a schema without `type` is modelled as, like the implicit plugins do: `items` is an array
 * ([isImplicitCollection]), `properties` or `additionalProperties` an object. Only called for a schema that shapes
 * values and is no `enum` or composite, which [mergeNonNull] merges before, so it always implies one of them.
 */
private fun Schema.impliedType(): Type = type ?: when {
    isImplicitCollection() -> Type.Basic.Array
    isObjectLike() -> Type.Basic.Object
    else -> throw IllegalArgumentException("Cannot infer the type of $this.")
}

private fun Type?.describe(): String = when (this) {
    is Type.Basic -> value
    is Type.Array -> types.joinToString(prefix = "[", postfix = "]") { it.value }
    null -> "any"
}

context(ctx: Registry.Scope)
private suspend fun Schema.mergeNestedAllOf(other: Schema): Schema =
    expandAllOf().merge(other.expandAllOf())

context(ctx: Registry.Scope)
private suspend fun Schema.expandAllOf(): Schema =
    if (allOf.isNullOrEmpty()) this
    else (allOf!!.map { it.peek() } + copy(allOf = null)).reduce { acc, schema -> acc.merge(schema) }

private enum class CompositeKind { OneOf, AnyOf }

private data class CompositeUnion(
    val kind: CompositeKind,
    val branches: List<ReferenceOr<Schema>>
)

private fun Schema.compositeUnionOrNull(): CompositeUnion? =
    when {
        !oneOf.isNullOrEmpty() -> CompositeUnion(CompositeKind.OneOf, oneOf!!)
        !anyOf.isNullOrEmpty() -> CompositeUnion(CompositeKind.AnyOf, anyOf!!)
        else -> null
    }

private fun Schema.isObjectLike(): Boolean =
    type == Type.Basic.Object || properties != null || additionalProperties != null

context(ctx: Registry.Scope)
private suspend fun Schema.mergeCompositeUnion(other: Schema): Schema {
    val left = compositeUnionOrNull()
    val right = other.compositeUnionOrNull()
    val mergedBranches = when {
        left != null && right != null ->
            left.branches.flatMap { l ->
                right.branches.mapNotNull { r ->
                    runCatching { l.peek().merge(r.peek()) }.getOrNull()?.let { ReferenceOr.value(it) }
                }
            }

        left != null ->
            left.branches.mapNotNull { branch ->
                runCatching { branch.peek().merge(other) }.getOrNull()?.let { ReferenceOr.value(it) }
            }

        right != null ->
            right.branches.mapNotNull { branch ->
                runCatching { merge(branch.peek()) }.getOrNull()?.let { ReferenceOr.value(it) }
            }

        else -> emptyList()
    }.distinct()

    if (mergedBranches.isEmpty()) return Schema(description = description ?: other.description)
    if (mergedBranches.size == 1) return mergedBranches.single().peek()

    val kind = when {
        left?.kind == CompositeKind.AnyOf || right?.kind == CompositeKind.AnyOf -> CompositeKind.AnyOf
        else -> CompositeKind.OneOf
    }

    return Schema(
        title = title ?: other.title,
        description = description ?: other.description,
        default = default ?: other.default,
        oneOf = mergedBranches.takeIf { kind == CompositeKind.OneOf },
        anyOf = mergedBranches.takeIf { kind == CompositeKind.AnyOf }
    )
}

context(ctx: Registry.Scope)
private suspend fun Schema.mergeEnum(other: Schema): Schema {
    val leftValues = enumLikeValues()
    val rightValues = other.enumLikeValues()
    return when {
        leftValues != null && rightValues != null -> {
            val mergedValues = leftValues.filter { it in rightValues }
            if (mergedValues.isEmpty()) {
                throw ContradictoryDeclarations("Enum intersection is empty for allOf: $this, $other")
            }
            copy(enum = null, `const` = null).merge(other.copy(enum = null, `const` = null)).copy(
                enum = mergedValues,
                `const` = null,
                default = mergedEnumDefault(mergedValues, default, other.default)
            )
        }

        leftValues != null ->
            copy(enum = null, `const` = null).merge(other).copy(
                enum = leftValues,
                `const` = null,
                default = mergedEnumDefault(leftValues, default, other.default)
            )

        rightValues != null -> other.mergeEnum(this)
        else -> error("Expected at least one enum when merging enums")
    }
}

private fun mergedEnumDefault(
    values: List<String?>,
    left: io.github.nomisrev.openapi.parser.DefaultValue?,
    right: io.github.nomisrev.openapi.parser.DefaultValue?
): io.github.nomisrev.openapi.parser.DefaultValue? {
    val allowedValues = values.toSet()
    return sequenceOf(left, right)
        .filterIsInstance<io.github.nomisrev.openapi.parser.ExampleValue.Single>()
        .firstOrNull { it.value in allowedValues }
}

fun Schema.mergeCommon(other: Schema): Schema = copy(
    type = type ?: other.type,
    format = mergeFormat(format, other.format),
    description = description ?: other.description,
    default = default ?: other.default,
    readOnly = readOnly.or(other.readOnly),
    writeOnly = writeOnly.or(other.writeOnly),
)

/**
 * The format of a value that has both formats. The format decides the generated type (`date-time` is an `Instant`,
 * `int32` an `Int`), so it is kept whichever schema declares it: an `int32` is also an `int64`, a `float` also a
 * `double`, and other formats that differ, such as `date` and `date-time`, no value has both of.
 */
private fun mergeFormat(format: String?, other: String?): String? = when {
    format == null || format == other -> other
    other == null -> format
    setOf(format, other) == setOf("int32", "int64") -> "int32"
    setOf(format, other) == setOf("float", "double") -> "float"
    else -> throw ContradictoryDeclarations("No value has both format '$format' and format '$other'.")
}

/** Intersects the constraints of [other], a schema that only constrains, with this one's. */
private fun Schema.mergeConstraints(other: Schema): Schema =
    numberConstraints(other)
        .mergeTextConstraints(other)
        .mergeObjectConstraints(other)
        .copy(
            minItems = let(minItems, other.minItems, ::maxOf),
            maxItems = let(maxItems, other.maxItems, ::minOf),
            uniqueItems = uniqueItems.or(other.uniqueItems),
        )

fun Schema.numberConstraints(other: Schema): Schema {
    val minimumBound = mergeMinimumBound(effectiveMinimum(), other.effectiveMinimum())
    val maximumBound = mergeMaximumBound(effectiveMaximum(), other.effectiveMaximum())

    return copy(
        minimum = minimumBound?.takeUnless { it.exclusive }?.value,
        exclusiveMinimum = minimumBound?.takeIf { it.exclusive }?.let {
            Schema.ExclusiveLimit.NumberValue(it.value)
        },
        maximum = maximumBound?.takeUnless { it.exclusive }?.value,
        exclusiveMaximum = maximumBound?.takeIf { it.exclusive }?.let {
            Schema.ExclusiveLimit.NumberValue(it.value)
        },
        multipleOf = multipleOf ?: other.multipleOf // TODO
    )
}

fun Schema.mergeTextConstraints(other: Schema): Schema =
    copy(
        minLength = let(minLength, other.minLength, ::maxOf),
        maxLength = let(maxLength, other.maxLength, ::minOf),
        pattern = pattern ?: other.pattern // TODO: intersection of multiple patterns is non-trivial
    )

fun Schema.mergeObjectConstraints(other: Schema): Schema =
    copy(
        minProperties = let(minProperties, other.minProperties, ::maxOf),
        maxProperties = let(maxProperties, other.maxProperties, ::minOf)
    )

fun Schema.mergeCollectionConstraints(other: Schema): Schema =
    copy(
        minItems = let(minItems, other.minItems, ::maxOf),
        maxItems = let(maxItems, other.maxItems, ::minOf),
        uniqueItems = uniqueItems ?: false || other.uniqueItems ?: false
    )

context(ctx: Registry.Scope)
suspend fun Schema.mergeObject(other: Schema): Schema {
    val properties = properties.orEmpty().merge(other.properties.orEmpty()) { property, a, b ->
        combineProperty(property, a, b)
    }

    return mergeCommon(other)
        .mergeObjectConstraints(other)
        .copy(
            properties = properties,
            additionalProperties = additionalProperties.merge(other.additionalProperties)
        )
}

private inline fun <A> let(a: A?, b: A?, block: (A, A) -> A): A? =
    if (a != null && b != null) block(a, b) else a ?: b

context(ctx: Registry.Scope)
private suspend fun AdditionalProperties?.merge(other: AdditionalProperties?): AdditionalProperties? =
    let(this, other) { a, b ->
        when (a) {
            is AdditionalProperties.PSchema -> when (b) {
                is Allowed -> a
                is AdditionalProperties.PSchema -> AdditionalProperties.PSchema(
                    ReferenceOr.value(
                        a.value.peek().merge(b.value.peek())
                    )
                )
            }

            is Allowed -> when (b) {
                is Allowed -> Allowed(a.value && b.value)
                is AdditionalProperties.PSchema -> b
            }
        }
    }
