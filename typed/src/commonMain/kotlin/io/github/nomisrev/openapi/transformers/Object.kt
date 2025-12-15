package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Object.*
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.*
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.readOnly
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.registry.writeOnly

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toObject(
    context: SchemaContext,
    properties: Map<String, ReferenceOr<Schema>>
): Model.Object {
    val properties = properties.mapNotNull { (name, refOrSchema) ->
        when (context) {
            SchemaContext.Write if refOrSchema.readOnly() == true -> null
            SchemaContext.Read if refOrSchema.writeOnly() == true -> null
            SchemaContext.Write,
            SchemaContext.Read,
            SchemaContext.Null -> refOrSchema.resolve(this@toObject.name.nest(ObjectProperty(name)), context) { propSchema ->
                val model = propSchema.toModel(context)
                Property(
                    name,
                    model,
                    schema.required.contains(name)
                )
            }
        }
    }

    val additionalProperties = additionalProperties(context)
    val nested = properties.mapNotNullTo(mutableSetOf()) { prop -> prop.model.nestedOrNull() } +
            listOfNotNull((additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.nestedOrNull())
    return Model.Object(
        context = name,
        description = description(),
        title = schema.title,
        properties = properties,
        inline = nested,
        additionalProperties = additionalProperties,
        isNullable = isNullable
    )
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.additionalProperties(context: SchemaContext) =
    when (val ap = schema.additionalProperties) {
        is AdditionalProperties.Allowed -> Model.Object.AdditionalProperties.Allowed(ap.value)
        null -> Model.Object.AdditionalProperties.Allowed(false)
        is AdditionalProperties.PSchema -> Model.Object.AdditionalProperties.Schema(
            ap.value.toModel(name.nest(NamingContext.AdditionalProperties), context)
        )
    }
