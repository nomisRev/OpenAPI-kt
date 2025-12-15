package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.schemaName

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
    val cases = subtypes.mapIndexed { index, subtype ->
        subtype.resolve(name.nest(unionCase(index, subtype)), context) {
            val discriminatorValue = schema.discriminator?.mapping?.let { discriminator ->
                when (it) {
                    is ResolvedSchema.Recursive if it.name is NamingContext.Reference -> discriminator[it.name.name]
                    is ResolvedSchema.Reference -> discriminator[it.name.name]
                    is ResolvedSchema.Recursive,
                    is ResolvedSchema.Value -> null
                }
            }
            Model.Union.Case(it.toModel(context), discriminatorValue)
        }
    }

    return Model.Union(
        name,
        cases,
        default(),
        description(),
        schema.title,
        cases.mapNotNullTo(mutableSetOf()) { it.model.nestedOrNull() },
        schema.discriminator?.propertyName,
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun unionCase(index: Int, subtype: ReferenceOr<Schema>): NamingContext.UnionCase {
    val schema = subtype.peek()
    fun discriminatorValue() = schema.discriminator?.mapping?.let { discriminator ->
        when (subtype) {
            is ReferenceOr.Reference -> discriminator[subtype.ref.schemaName()]
            is ReferenceOr.Value<Schema> -> null
        }
    }

    suspend fun specialName() = schema.properties
        ?.entries
        ?.firstOrNull { (key, _) -> key in setOf("type", "event", $$"$type") }
        ?.let { (_, refOrSchema) -> refOrSchema.peek().enum?.singleOrNull() }

    fun name() =
        schema.enum?.joinToString(prefix = "", separator = "Or") { it?.replaceFirstChar(Char::uppercaseChar) ?: "" }

    return NamingContext.UnionCase(discriminatorValue() ?: specialName() ?: name() ?: "Case$index")
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