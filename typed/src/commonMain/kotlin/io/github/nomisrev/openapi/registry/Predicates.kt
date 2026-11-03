package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.NamingContext.Companion.reference
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.plugins.composite.compositeTakesPrecedence
import io.github.nomisrev.openapi.registry.ResolvedSchema.Reference
import io.github.nomisrev.openapi.registry.ResolvedSchema.Value
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = when (this) {
    is ReferenceOr.Reference if ref == "#" -> readOnly
    is ReferenceOr.Reference -> readOnly ?: with(ctx) { peek().readOnly }
    is ReferenceOr.Value -> value.readOnly
}

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = when (this) {
    is ReferenceOr.Reference if ref == "#" -> writeOnly
    is ReferenceOr.Reference -> writeOnly ?: with(ctx) { peek().writeOnly }
    is ReferenceOr.Value -> value.writeOnly
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOpenEnumeration(): Boolean = with(ctx) {
    val anyOf = schema.anyOf ?: return false
    if (anyOf.size != 2) return false
    val enum = anyOf.singleOrNull { it.peek().enumLikeValues() != null } ?: return false
    val other = (anyOf - enum).singleOrNull() ?: return false
    // TODO: what about other open enums? Should we detect type of enum at parse time?
    other.peek().type == Schema.Type.Basic.String
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean = with(ctx) {
    val anyOf = schema.anyOf ?: return false
    return anyOf.any { it.peek().isNull() }
}

private val nullableSchema = Schema(nullable = true)

fun Schema.isNull(): Boolean =
    type == Schema.Type.Basic.Null || this == nullableSchema

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOneOfNullableType(): Boolean = with(ctx) {
    val oneOf = schema.oneOf ?: return false
    oneOf.any { it.peek().isNull() }
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isAllOfNullableType(): Boolean = with(ctx) {
    val allOf = schema.allOf ?: return false
    allOf.any { it.peek().isNull() }
}

/**
 * Whether this schema's model is nullable: `nullable: true`, a `type` array listing `"null"`, an `anyOf`/`oneOf`/`allOf` with a `null` branch, which the composite nullable-flatten plugins model
 * as a nullable `X?`, or a single-`$ref` alias `anyOf`/`oneOf`/`allOf: [{$ref: X}]` of a nullable X, which models
 * X's (nullable) schema, or a discriminated `anyOf`/`oneOf` with a member that permits `null`
 * ([hasNullableDiscriminatedMember]). A `$ref` that is not resolved (a property, a recursive reference) must use this so
 * the use site is nullable exactly when the referenced model is.
 */
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isNullableModel(): Boolean = schema.isNullableModel(emptySet())

context(ctx: Registry.Scope)
private suspend fun Schema.isNullableModel(visited: Set<String>): Boolean {
    val type = type
    if (nullable == true || (type is Schema.Type.Array && Schema.Type.Basic.Null in type.types)) return true
    if (!compositeTakesPrecedence()) return false
    val composites = listOfNotNull(allOf, anyOf, oneOf)
    if (composites.flatten().any { it.peek().isNull() }) return true
    if (hasNullableDiscriminatedMember(visited)) return true
    val target = composites.singleOrNull()?.singleOrNull() as? ReferenceOr.Reference ?: return false
    return target.ref !in visited && target.peek().isNullableModel(visited + target.ref)
}

/**
 * Whether this is a discriminated `anyOf`/`oneOf` (it has a `discriminator`) with a member that permits `null`, such
 * as a `$ref` to `oneOf: [{$ref: Cat}, {type: null}]`. `null` carries no discriminator, so it cannot be one of the
 * union's cases: the union itself is nullable, and the member is modelled as its non-null case.
 */
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.hasNullableDiscriminatedMember(): Boolean =
    schema.hasNullableDiscriminatedMember(emptySet())

context(ctx: Registry.Scope)
private suspend fun Schema.hasNullableDiscriminatedMember(visited: Set<String>): Boolean =
    discriminator != null && listOfNotNull(anyOf, oneOf).flatten().any { member ->
        when (member) {
            is ReferenceOr.Reference ->
                member.ref !in visited && member.peek().isNullableModel(visited + member.ref)

            is ReferenceOr.Value<Schema> -> member.value.isNullableModel(visited)
        }
    }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.Reference.isObjectWithDiscriminator(): Boolean =
    schema.isObjectWithDiscriminator(reference.name)

@OptIn(ExperimentalContracts::class)
context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.isObjectWithDiscriminator(): Boolean {
    contract {
        returns(true) implies (this@isObjectWithDiscriminator is ReferenceOr.Reference)
    }
    return when (this) {
        is ReferenceOr.Reference if peek().isObjectWithDiscriminator(ref) -> true
        is ReferenceOr.Reference, is ReferenceOr.Value<*> -> false
    }
}

context(ctx: Registry.Scope)
private suspend fun Schema.isObjectWithDiscriminator(ref: String): Boolean = with(ctx) {
    properties != null &&
            discriminator?.mapping?.isNotEmpty() == true &&
            discriminator?.mapping?.all { [_, mappingName] ->
                if (ref == mappingName.schemaName()) {
                    true
                } else {
                    val s = peek(mappingName)
                    s.allOf != null && s.type == null
                }
            } ?: false
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.description(): String? = with(ctx) {
    tailrec suspend fun ReferenceOr<String>?.get(): String? = when (this) {
        is ReferenceOr.Value -> value
        null -> null
        is ReferenceOr.Reference ->
            copy(
                ref =
                    if (ref.endsWith("/description")) ref.dropLast("/description".length) else ref
            ).peek().description.get()
    }

    when (this@description) {
        is Reference -> this@description.schema.description.get()
        is Value -> this@description.schema.description.get()
        is ResolvedSchema.Recursive -> this@description.schema.description.get()
    }
}

/**
 * The JSON Schema boolean schema (`true` accepts any value, `false` accepts none) this schema is,
 * following a `$ref`, or `null` when it is a regular schema.
 */
context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.booleanSchema(): Boolean? = peek().booleanSchema
