package io.github.nomisrev.openapi.pipeline.plugins.composite

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.isNull
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext

context(ctx: Registry.Scope)
internal suspend fun ResolvedSchema.flattenNull(
    schemas: List<ReferenceOr<Schema>>,
    build: suspend (List<ReferenceOr<Schema>>) -> Model,
): Model {
    val nonNullSchemas = schemas.filterNot { it.peek().isNull() }
    require(nonNullSchemas.isNotEmpty()) {
        "Null should always be resolved to result in nullable types. Please report this bug. $schema"
    }
    val model = build(nonNullSchemas)
    return model.with(
        description = description() ?: model.description,
        isNullable = true,
        title = schema.title ?: model.title
    )
}

context(ctx: Registry.Scope)
internal suspend fun ResolvedSchema.flattenedSingleNullableBranch(
    branch: ReferenceOr<Schema>,
    context: SchemaContext,
): Model = when (branch) {
    // A named component `anyOf`/`oneOf`/`allOf: [{$ref: X}, {type: null}]` is its own (nullable) type named after
    // the component, like the non-null `allOf: [{$ref: X}]` alias: model X's schema under the component's name.
    // Resolving the `$ref` instead yields X's own model, so X is generated twice and the component never is.
    is ReferenceOr.Reference if this is ResolvedSchema.Reference -> ctx.registry().engine.transform(
        ctx,
        ctx.registry(),
        ResolvedSchema.Reference(reference, branch.peek()),
        context,
        true
    )

    is ReferenceOr.Reference -> branch.toModel(name, context)
    is ReferenceOr.Value<Schema> -> when (this) {
        is ResolvedSchema.Reference -> ctx.registry().engine.transform(
            ctx,
            ctx.registry(),
            ResolvedSchema.Reference(reference, branch.value),
            context,
            true
        )

        is ResolvedSchema.Recursive -> ctx.registry().engine.transform(
            ctx,
            ctx.registry(),
            ResolvedSchema.Recursive(name, branch.value),
            context,
            true
        )

        is ResolvedSchema.Value -> branch.toModel(name, context)
    }
}
