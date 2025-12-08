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

    val nested = properties.filter { prop -> prop.model.isNested() }
    return Model.Object(
        context = name,
        description = description(),
        properties = properties,
        inline = nested.map { it.model }.toSet(),
        additionalProperties = additionalProperties(),
        isNullable = schema.nullable ?: false
    )
}

private tailrec fun Model.isNested(): Boolean = when (this) {
    is Model.Collection.List -> inner.isNested()
    is Model.Collection.Map -> inner.isNested()
    is Model.ByteArray,
    is Model.Date,
    is Model.FreeFormJson,
    is Model.DateTime,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> false

    is Model.DiscriminatedObject if context is NamingContext.Reference -> false
    is Model.Enum.Closed if context is NamingContext.Reference -> false
    is Model.Enum.Open if context is NamingContext.Reference -> false
    is Model.Object if context is NamingContext.Reference -> false
    is Model.Union if context is NamingContext.Reference -> false
    is Model.DiscriminatedObject,
    is Model.Enum.Closed,
    is Model.Enum.Open,
    is Model.Object,
    is Model.Union -> true
}

private fun ResolvedSchema.additionalProperties() = when (val ap = schema.additionalProperties) {
    is AdditionalProperties.Allowed -> ap.value
    null -> false
    is AdditionalProperties.PSchema -> TODO()
//        when (val refOrSchema = ap.value) {
//            is ReferenceOr.Value<Schema> -> refOrSchema.value == Schema()
//            is ReferenceOr.Reference -> {
//                val name = refOrSchema.ref.schemaName()
//                val schema =
//                    requireNotNull(openAPI.components.schemas[name]?.valueOrNull()) { "Null or remote schema for $name" }
//                schema == Schema()
//            }
//        }
}
