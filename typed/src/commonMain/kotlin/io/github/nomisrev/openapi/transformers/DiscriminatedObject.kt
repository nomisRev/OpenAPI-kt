package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.schemaName

context(scope: Registry.Scope)
suspend fun ResolvedSchema.toDiscriminatedObject(context: SchemaContext): Model.DiscriminatedObject {
    val discriminator = requireNotNull(schema.discriminator) { "Discriminator required for discriminated object" }
    val mapping = requireNotNull(discriminator.mapping) { "Discriminator mapping required" }
    require(this is ResolvedSchema.Reference) { "Expected reference for discriminated object" }

    val mappings = mapping.filterValues { it != "#/components/schemas/${reference.name}" }

    val abstractProperties = properties(schema.properties!!, context)

    val subtypes = mappings.map { (mappedName, ref) ->
        val name = name.nest(NamingContext.DiscriminatedObjectCase(mappedName))
        val subtypeSchema = ReferenceOr.schema(ref).peek()




        TODO()
//        require(subtype is Model.Object) { "DiscriminatedObject subtype expected to be Model.Object got $subtype" }
//        subtype
    }

    return Model.DiscriminatedObject(
        name,
        abstractProperties,
        listOfNotNull(selfObjectOrNull(mapping, context, abstractProperties)) + subtypes,
        description(),
        schema.title,
        discriminator.propertyName,
        isNullable
    )
}

private fun ResolvedSchema.Reference.selfObjectOrNull(
    mapping: Map<String, String>,
    context: SchemaContext,
    abstractProperties: List<Model.Object.Property>
): Model.Object? {
    val refName = mapping.entries
        .singleOrNull { (_, ref) -> ref == "#/components/schemas/${context.name}" }
        ?.value ?: return null

    val selfName =
        if (refName == reference.name) name.nest(NamingContext.DiscriminatedObjectCase("Default"))
        else name.nest(NamingContext.DiscriminatedObjectCase(refName))

    return Model.Object(
        selfName,
        null,
        null,
        abstractProperties,
        emptySet(),
        Model.Object.AdditionalProperties.Allowed(false),
        isNullable
    )
}
