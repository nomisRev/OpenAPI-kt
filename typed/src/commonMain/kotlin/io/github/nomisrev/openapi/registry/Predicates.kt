package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.ResolvedSchema.Reference
import io.github.nomisrev.openapi.registry.ResolvedSchema.Value

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = with(ctx) { peek().readOnly }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = with(ctx) { peek().writeOnly }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOpenEnumeration(): Boolean = with(ctx) {
    val anyOf = schema.anyOf ?: return false
    if (anyOf.size != 2) return false
    val enum = anyOf.singleOrNull { it.peek().enum != null } ?: return false
    val other = (anyOf - enum).singleOrNull() ?: return false
    // TODO: what about other open enums? Should we detect type of enum at parse time?
    other.peek().type == Schema.Type.Basic.String
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean = with(ctx) {
    val anyOf = schema.anyOf ?: return false
    return anyOf.size == 2 && anyOf.singleOrNull { it.peek().type == Schema.Type.Basic.Null } != null
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOneOfNullableType(): Boolean = with(ctx) {
    val oneOf = schema.oneOf ?: return false
    oneOf.size == 2 && oneOf.singleOrNull { it.peek().type == Schema.Type.Basic.Null } != null
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.Reference.isObjectWithDiscriminator(): Boolean = with(ctx) {
    schema.properties != null &&
            schema.discriminator?.mapping?.isNotEmpty() == true &&
            schema.discriminator?.mapping?.all { (_, ref) ->
                val mappingName = ref.schemaName()
                if (name == NamingContext.Reference(mappingName, null)) {
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
        is ReferenceOr.Reference -> peek().description.get()
    }

    when (this@description) {
        is Reference -> this@description.schema.description.get()
        is Value -> this@description.schema.description.get()
        is ResolvedSchema.Recursive -> this@description.schema.description.get()
    }
}
