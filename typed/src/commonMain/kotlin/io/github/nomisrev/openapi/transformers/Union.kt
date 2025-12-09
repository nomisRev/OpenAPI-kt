package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.toModel

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
    val cases = subtypes.map { subtype -> subtype.toModel(NamingContext.UnionCase, context) }
    return Model.Union(
        name,
        cases,
        null,
        description(),
        cases.mapNotNullTo(mutableSetOf()) { it.nestedOrNull() },
        schema.discriminator?.let { Model.Discriminator(it.propertyName, it.mapping) },
        isNullable
    )
}

// TODO: This only needs to be done in the Serializer... No need to adjust the actual schema or order during type gen
//private val unionSchemaComparator: Comparator<Model> = Comparator { m1, m2 ->
//    val m1Complexity =
//        when (m1) {
//            is Model.Object -> m1.properties.size
//            is Model.Enum -> m1.values.size
//            is Primitive.String -> Int.MIN_VALUE
//            else -> 0
//        }
//    val m2Complexity =
//        when (m2) {
//            is Model.Object -> m2.properties.size
//            is Model.Enum -> m2.values.size
//            is Primitive.String -> Int.MIN_VALUE
//            else -> 0
//        }
//    m2Complexity - m1Complexity
//}