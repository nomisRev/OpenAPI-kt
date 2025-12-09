package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
    val cases = subtypes.map { subtype ->
        // TODO either generate name _before_ resolution using (unresolving) predicates,
        //    Or, use a placeholder and use `Model` for name generation.
        val name = TODO("ctx.unionContext(name, subtype)")
        // Where the FK does this name come from now?
        subtype.resolve(name, context).toModel(context)
    }
    return Model.Union(
        name,
        cases,
        null,
        description(),
        cases.mapNotNullTo(mutableSetOf()) { it.nestedOrNull() },
        schema.discriminator?.let { Model.Discriminator(it.propertyName, it.mapping) },
        schema.nullable ?: false
    )
}