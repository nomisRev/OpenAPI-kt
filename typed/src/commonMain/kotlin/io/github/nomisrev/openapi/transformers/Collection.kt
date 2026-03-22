package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Default
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.toModel

@Suppress("CyclomaticComplexMethod")
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.collection(context: SchemaContext): Model =
    when (this) {
        is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable, schema.title)
        is ResolvedSchema.Value -> toCollection(context)
        is ResolvedSchema.Reference -> {
            fun wrapIfNeeded(inner: Model?): Model = when (inner) {
                is Model.ContextHolder if inner.context.isTopLevel() && inner.context != name -> inner
                is Model.Collection -> inner.copy(inner = wrapIfNeeded(inner.inner))
                is Model.ContextHolder -> when (inner) {
                    is Model.DiscriminatedObject -> error("Discriminated objects are not supported inline")
                    is Model.Reference -> error("References are not supported inline")
                    is Model.Enum -> inner.copy(context = inner.context.nest(NamingContext.ObjectProperty("item")))
                    is Model.Object -> inner.nestContext(NamingContext.ObjectProperty("item"))
                    is Model.OneOf -> inner.copy(context = inner.context.nest(NamingContext.ObjectProperty("item")))
                    is Model.AnyOf -> inner.copy(context = inner.context.nest(NamingContext.ObjectProperty("item")))
                }

                is Model.ByteArray,
                is Model.Date,
                is Model.DateTime,
                is Model.FreeFormJson,
                is Model.Uuid,
                is Model.Primitive -> inner

                null -> Model.FreeFormJson(description = null, constraint = null, isNullable = false, title = null)
            }

            val inner = wrapIfNeeded(schema.items?.toModel(name, context))

            Model.Object(
                name,
                description(),
                schema.title,
                mapOf(
                    "items" to Model.Object.Property(
                        Model.Collection(
                            inner,
                            collectionDefault(),
                            null,
                            Constraints.Collection(schema),
                            isNullable,
                            null
                        ),
                        true
                    )
                ),
                additionalProperties = false,
                isNullable
            )
        }
    }

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.toCollection(context: SchemaContext): Model.Collection {
    val inner = schema.items?.toModel(name, context)
        ?: Model.FreeFormJson(description = null, constraint = null, isNullable = false, title = null)
    return Model.Collection(
        inner,
        collectionDefault(),
        description(),
        Constraints.Collection(schema),
        isNullable,
        schema.title
    )
}

/**
 * Inserts [prefix] as the first element of the nested list in the context of this object and all
 * nested inline models within its properties, so that the full naming path is consistent after the
 * item wrapping in [collection].
 */
private fun Model.Object.nestContext(prefix: NamingContext.Nested): Model.Object {
    val nestedProperties = properties.mapValues { (_, prop) ->
        prop.copy(model = prop.model.prependContextIfInline(prefix))
    }
    return copy(
        context = context.prepend(prefix),
        properties = nestedProperties
    )
}

private fun NamingContext.prepend(prefix: NamingContext.Nested) = copy(nested = listOf(prefix) + nested)

private fun Model.prependContextIfInline(prefix: NamingContext.Nested): Model = when (this) {
    is Model.Enum if !context.isTopLevel() -> copy(context = context.prepend(prefix))
    is Model.Object if !context.isTopLevel() -> nestContext(prefix)
    is Model.OneOf if !context.isTopLevel() -> copy(context = context.prepend(prefix))
    is Model.AnyOf if !context.isTopLevel() -> copy(context = context.prepend(prefix))
    is Model.Collection -> copy(inner = inner.prependContextIfInline(prefix))

    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive,
    is Model.Reference,
    is Model.AnyOf,
    is Model.OneOf,
    is Model.Uuid -> this
}

private fun ResolvedSchema.collectionDefault() = when (val example = schema.default) {
    null -> null
    is ExampleValue.Multiple -> Default.Value(example.values)
    is ExampleValue.Single -> {
        val value = example.value
        when {
            value == "[]" -> Default.Value(emptyList())
            value.equals("null", ignoreCase = true) -> {
                require(schema.nullable == true) { "Null default for non-nullable collection." }
                Default.Null
            }

            else -> Default.Value(listOf(value))
        }
    }
}
