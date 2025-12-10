package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.schemaName
import io.github.nomisrev.openapi.toModel

context(scope: Registry.Scope)
suspend fun ResolvedSchema.toDiscriminatedObject(context: SchemaContext): Model.DiscriminatedObject {
    val discriminator = requireNotNull(schema.discriminator) { "Discriminator required for discriminated object" }
    val mapping = requireNotNull(discriminator.mapping) { "Discriminator mapping required" }
    require(this is ResolvedSchema.Reference) { "Expected reference for discriminated object" }

    val hasSelfReference = mapping.entries.any { (_, ref) -> ref == "#/components/schemas/${context.name}" }
    val mappings = mapping.filterValues { it != "#/components/schemas/${name.name}" }

    val baseObject = toObject(context, schema.properties!!)
    val subtypes = mappings.map { (_, ref) ->
        // Skip 'resolve' since we're inlining schemas even top-level ones
        val subtype = ResolvedSchema.Value(
            NamingContext.DiscriminatedObjectCase(ref.schemaName()),
            ReferenceOr.schema(ref).peek()
        ).toModel(context)
        require(subtype is Model.Object) { "DiscriminatedObject subtype expected to be Model.Object got $subtype" }
        subtype
    }

    return Model.DiscriminatedObject(
        name,
        baseObject,
        subtypes,
        description(),
        discriminator.propertyName,
        hasSelfReference,
        isNullable
    )
}