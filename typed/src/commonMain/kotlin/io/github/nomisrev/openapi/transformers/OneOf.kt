package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.get
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel
import kotlinx.serialization.EncodeDefault

context(ctx: Registry)
suspend fun ResolvedSchema.oneOfNullable(context: SchemaContext, oneOf: List<ReferenceOr<Schema>>) : Model {
    val schema = oneOf.map { it.resolve(name, context) }.single { it.schema.type != Schema.Type.Basic.Null }
    return schema.toModel(context).with(isNullable = true)
}
