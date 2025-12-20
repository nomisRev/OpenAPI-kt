package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
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


context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
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
     *  So in this case whenever there are n casses, and n-1 cases are references than the non-referenced case should inherit the outer name.
     */
    val cases = subtypes.mapIndexed { index, subtype ->
        subtype.resolve(name.unionCase(index, subtype, context), context) {
            val discriminatorValue = schema.discriminator?.mapping?.let { discriminator ->
                when (it) {
                    is ResolvedSchema.Recursive if it.name.head is NamingContext.Reference -> discriminator[it.name.head.name]
                    is ResolvedSchema.Reference -> discriminator[it.reference.name]
                    is ResolvedSchema.Recursive,
                    is ResolvedSchema.Value -> null
                }
            }
            Model.Union.Case(it.toModel(context, false), discriminatorValue)
        }
    }

    return Model.Union(
        name,
        cases,
        default(),
        description(),
        schema.title,
        schema.discriminator?.propertyName,
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun NamingContext.unionCase(index: Int, subtype: ReferenceOr<Schema>, context: SchemaContext): NamingContext {
    val schema = subtype.peek()

    if (schema.type == Type.Basic.Array && schema.items != null) {
        return when (val refOrSchema = schema.items!!) {
            is ReferenceOr.Reference -> NamingContext.reference(refOrSchema.ref.schemaName(), context)
            is ReferenceOr.Value<Schema> -> unionCase(index, refOrSchema, context)
        }
    }

    fun discriminatorValue() = schema.discriminator?.mapping?.let { discriminator ->
        when (subtype) {
            is ReferenceOr.Reference -> discriminator[subtype.ref.schemaName()]
            is ReferenceOr.Value<Schema> -> null
        }
    }

    suspend fun specialName() = schema.properties
        ?.entries
        ?.firstOrNull { (key, _) -> key in setOf("type", "event", $$"$type") }
        ?.let { (_, refOrSchema) -> refOrSchema.peek().enum?.singleOrNull() }

    fun enumName() =
        schema.enum?.joinToString(prefix = "", separator = "Or") {
            it?.replaceFirstChar(Char::uppercaseChar) ?: ""
        }.takeIf { (it?.length ?: 0) < 90 }

    return nest(NamingContext.UnionCase(discriminatorValue() ?: specialName() ?: enumName() ?: "Case$index"))
}
