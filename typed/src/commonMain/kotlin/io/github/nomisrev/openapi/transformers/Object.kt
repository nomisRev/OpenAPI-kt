package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.readOnly
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel
import io.github.nomisrev.openapi.writeOnly

context(ctx: Registry)
suspend fun ResolvedSchema.toObject(
    context: SchemaContext,
    properties: Map<String, ReferenceOr<Schema>>
): Model {
    val properties = properties.mapNotNull { (name, refOrSchema) ->
        when (context) {
            SchemaContext.Input if refOrSchema.readOnly() == true -> null
            SchemaContext.Output if refOrSchema.writeOnly() == true -> null
            else -> {
                val propSchema = refOrSchema.resolve(NamingContext.ObjectProperty(name), context)
                val model = propSchema.toModel(context)
                Model.Object.Property(
                    name,
                    model,
                    schema.required.contains(name),
                    propSchema.description()
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
        properties = properties,
        inline = nested,
        additionalProperties = additionalProperties,
        isNullable = schema.nullable ?: false
    )
}

context(ctx: Registry)
private suspend fun ResolvedSchema.additionalProperties(context: SchemaContext) =
    when (val ap = schema.additionalProperties) {
        is AdditionalProperties.Allowed -> Model.Object.AdditionalProperties.Allowed(ap.value)
        null -> Model.Object.AdditionalProperties.Allowed(false)
        is AdditionalProperties.PSchema -> Model.Object.AdditionalProperties.Schema(
            ap.value.resolve(name.nest(NamingContext.GenerateUnionName("Additional")), context)
                .toModel(SchemaContext.Output)
        )
    }
