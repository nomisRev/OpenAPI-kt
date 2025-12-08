package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel

/**
 * oneOf: [{ type: null }, { type: string }]
 * anyOf: [{ type: null }, { type: string }]
 * become
 * {
 *   type: string
 *   nullable: true
 * }
 */
context(ctx: Registry)
suspend fun ResolvedSchema.flattenNull(context: SchemaContext, schemas: List<ReferenceOr<Schema>>) : Model {
    val schema = schemas.map { it.resolve(name, context) }.single { it.schema.type != Schema.Type.Basic.Null }
    return schema.toModel(context).with(isNullable = true)
}
