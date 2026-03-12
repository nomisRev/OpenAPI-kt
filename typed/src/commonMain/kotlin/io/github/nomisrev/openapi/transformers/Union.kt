package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.schemaName
import io.github.nomisrev.openapi.render.toPascalCase


context(ctx: Registry.Scope)
suspend fun ResolvedSchema.union(
    context: SchemaContext,
    subtypes: List<ReferenceOr<Schema>>,
): Model {
    val uniqueSubtypes = subtypes.distinct()
    val peekedSubtypes = uniqueSubtypes.associateWith { it.peek() }
    val unionContexts = peekedSubtypes.entries.mapIndexed { index, (refOrSchema, schema) ->
        name.unionCase(index, refOrSchema, schema, peekedSubtypes, context)
    }

    /**
     * We need to make unionCase NamingContext name generation more powerful:
     *  1. For OpenEnum we want to non-string case to inherit the outer name:
     *      -> "model": { oneOf[{ type: String},{ type: String, enum: [a, b]  }] }
     *      => sealed interface Model should have 2 subtypes CaseString & CaseModel.
     *
     *  2. For similar cases like "open-enum" but with complex types:
     *    -> "event": { oneOf[{ -$ref:EventA }, { -$ref:EventB }, { type: object, properties={} }]
     *    => sealed interface Event with 3 subtypes EventA, EventB, and CaseEvent
     *
     *  So in this case whenever there are n casses, and n-1 cases are references than the non-referenced case should inherit the outer name.
     */
    val cases = uniqueSubtypes.mapIndexed { index, subtype ->
        subtype.resolve(unionContexts[index], context) {
            val discriminatorValue = schema.discriminator?.mapping?.let { discriminator ->
                when (it) {
                    is ResolvedSchema.Recursive if it.name.head is NamingContext.Reference -> discriminator[it.name.head.name]
                    is ResolvedSchema.Reference -> discriminator[it.reference.name]
                    is ResolvedSchema.Recursive,
                    is ResolvedSchema.Value -> null
                }
            }
            Model.Union.Case(it.toModel(context, false), discriminatorValue)
        }
    }

    return Model.Union(
        name,
        cases,
        default(),
        description(),
        schema.title,
        schema.discriminator?.propertyName,
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun NamingContext.unionCase(
    index: Int,
    subtype: ReferenceOr<Schema>,
    allSubtypes: List<ReferenceOr<Schema>>,
    context: SchemaContext
): NamingContext {
    val schema = subtype.peek()

    if (schema.type == Type.Basic.Array && schema.items != null) {
        return when (val refOrSchema = schema.items!!) {
            is ReferenceOr.Reference -> NamingContext.reference(refOrSchema.ref.schemaName(), context)
            is ReferenceOr.Value<Schema> -> unionCase(index, refOrSchema, allSubtypes, context)
        }
    }

    val discriminatorValue: String? = schema.discriminator?.mapping?.let { discriminator ->
        when (subtype) {
            is ReferenceOr.Reference -> discriminator[subtype.ref.schemaName()]
            is ReferenceOr.Value<Schema> -> null
        }
    }

    val specialName = schema.properties
        ?.entries
        ?.firstOrNull { (key, _) -> key in setOf("type", "event", $$"$type") }
        ?.let { (_, refOrSchema) -> refOrSchema.peek().enum?.singleOrNull() }

    val enumName =
        schema.enum?.joinToString(prefix = "", separator = "Or") {
            it?.replaceFirstChar(Char::uppercaseChar) ?: ""
        }.takeIf { (it?.length ?: 0) < 90 }

    /**
     * Checks if this is an OpenEnum pattern where n-1 cases are plain string types
     * and 1 case is a string enum. Returns true if current subtype is the enum case.
     */
    suspend fun isOpenEnumCase(): Boolean {
        if (allSubtypes.size < 2) return false
        val schemas = allSubtypes.map { it.peek() }
        val stringTypes = schemas.filter { it.type == Type.Basic.String && it.enum == null }
        val enumTypes = schemas.filter { it.type == Type.Basic.String && it.enum != null }
        return stringTypes.size == allSubtypes.size - 1 &&
                enumTypes.size == 1 &&
                schema.type == Type.Basic.String &&
                schema.enum != null
    }

    /**
     * Checks if this is a reference pattern where n-1 cases are references
     * and 1 case is a non-reference (value). Returns true if current subtype is the non-reference case.
     */
    fun isNonReferenceCaseInRefPattern(): Boolean {
        val referenceCount = allSubtypes.count { it is ReferenceOr.Reference }
        val valueCount = allSubtypes.count { it is ReferenceOr.Value }
        return referenceCount == allSubtypes.size - 1 &&
                valueCount == 1 &&
                subtype is ReferenceOr.Value
    }

    /**
     * Checks if this is a reference pattern where n-1 cases are references
     * and 1 case is a non-reference (value). Returns true if current subtype is the non-reference case.
     */
    suspend fun isObjectInPrimitivePattern(): Boolean {
        val referenceCount = allSubtypes.count { it.peek().type in primitives }
        val valueCount = allSubtypes.count { it is ReferenceOr.Value }
        return referenceCount == allSubtypes.size - 1 &&
                valueCount == 1 &&
                subtype is ReferenceOr.Value
    }

    val name = when {
        allSubtypes.size == 1 -> "Case"
        discriminatorValue != null -> discriminatorValue
        specialName != null -> specialName
        isOpenEnumCase() -> enumName ?: "CaseEnum"
        isNonReferenceCaseInRefPattern() -> "Else"
        else -> schema.unionCaseName(index)
    }

    return nest(NamingContext.UnionCase(name))
}

private val primitives = setOf(Type.Basic.String, Type.Basic.Number, Type.Basic.Boolean, Type.Basic.Integer)

/**
 * Generates a union case name based on the schema type and structure.
 * For objects: joins property names with "And" (e.g., `AgeAndName`)
 * For primitives: uses type-based names (e.g., `CaseInt`, `CaseString`)
 * For enums: joins enum values with "Or" (e.g., `AscOrDesc`)
 */
private fun Schema.unionCaseName(index: Int): String {
    val fmt = format
    return when (type) {
        Type.Basic.Object -> properties?.entries?.joinToString(
            prefix = "",
            separator = "And"
        ) { (name, _) -> name.toPascalCase() }
            .takeIf { (it?.length ?: 0) < 90 }
            ?: caseIndex.getOrElse(index) { "Case$index" }

        Type.Basic.Number -> if (fmt == "float") "CaseFloat" else "CaseDouble"
        Type.Basic.Boolean -> "CaseBoolean"
        Type.Basic.Integer -> if (fmt == "int32") "CaseInt" else "CaseLong"

        Type.Basic.String -> when {
            enum != null -> enum!!.joinToString(separator = "Or") {
                it?.toPascalCase() ?: ""
            }.takeIf { it.length < 90 } ?: caseIndex.getOrElse(index) { "Case$index" }
            fmt == "binary" -> "CaseBinary"
            fmt == "uuid" -> "CaseUuid"
            fmt == "date" -> "CaseDate"
            fmt == "date-time" -> "CaseDateTime"
            !fmt.isNullOrBlank() -> "Case${fmt.toPascalCase()}"
            else -> "CaseString"
        }

        Type.Basic.Array -> "CaseArray"
        Type.Basic.Null -> "CaseNull"

        is Type.Array -> (type as Type.Array).types.joinToString(
            prefix = "Case",
            separator = "Or"
        ) { it.name.toPascalCase() }.takeIf { it.length < 90 }
            ?: caseIndex.getOrElse(index) { "Case$index" }

        null -> caseIndex.getOrElse(index) { "Case$index" }
    }
}

val caseIndex = listOf(
    "One",
    "Two",
    "Three",
    "Four",
    "Five",
    "Six",
    "Seven",
    "Eight",
    "Nine",
    "Ten",
    "Eleven",
    "Twelve",
    "Thirteen",
    "Fourteen",
    "Fifteen",
    "Sixteen",
)

context(ctx: Registry.Scope)
private suspend fun NamingContext.unionCase(
    index: Int,
    refOrSchema: ReferenceOr<Schema>,
    schema: Schema,
    context: Map<ReferenceOr<Schema>, Schema>,
    context2: SchemaContext
): NamingContext = when (schema.type) {
    Type.Basic.Array -> when (val refOrSchema = schema.items!!) {
        is ReferenceOr.Reference -> NamingContext.reference(refOrSchema.ref.schemaName(), context2)
        is ReferenceOr.Value<Schema> -> unionCase(index, refOrSchema, refOrSchema.peek(), context, context2)
    }

    Type.Basic.Object -> {
        if ((context - refOrSchema).all { it.key is ReferenceOr.Reference }) {
            nest(NamingContext.UnionCase("CaseElse"))
        } else nest(
            NamingContext.UnionCase(
                schema.properties?.entries?.joinToString(
                    prefix = "",
                    separator = "And"
                ) { (name, _) ->
                    name.replaceFirstChar(Char::uppercaseChar)
                }.takeIf { (it?.length ?: 0) < 90 } ?: caseIndex[index])
        )
    }

    Type.Basic.Null -> TODO("$schema")
    Type.Basic.Number -> nest(NamingContext.UnionCase("CaseDouble"))
    Type.Basic.Number if schema.format == "float" -> nest(NamingContext.UnionCase("CaseFloat"))
    Type.Basic.Boolean -> nest(NamingContext.UnionCase("CaseBoolean"))
    Type.Basic.Integer if schema.format == "int32" -> nest(NamingContext.UnionCase("CaseInt"))
    Type.Basic.Integer -> nest(NamingContext.UnionCase("CaseLong"))
    Type.Basic.String if schema.enum != null -> nest(
        NamingContext.UnionCase(
            requireNotNull(schema.enum) { "Enum requires at least 1 possible value. {\"enum\":[]}" }
                .joinToString(separator = "Or") { it?.replaceFirstChar(Char::uppercase) ?: "" }
        )
    )

    Type.Basic.String -> when (schema.format) {
        "binary" -> nest(NamingContext.UnionCase("CaseBinary"))
        "uuid" -> nest(NamingContext.UnionCase("CaseUuid"))
        "date" -> nest(NamingContext.UnionCase("CaseDate"))
        "date-time" -> nest(NamingContext.UnionCase("CaseDateTime"))
        else if !schema.format.isNullOrBlank() -> nest(NamingContext.UnionCase("Case${schema.format}"))
        else -> nest(NamingContext.UnionCase("CaseString"))
    }

    is Type.Array -> nest(
        NamingContext.UnionCase(
            (schema.type as Type.Array).types.joinToString(
                prefix = "Case",
                separator = "Or"
            ) { type ->
                TODO()
            })
    )

    else if schema.enum != null -> nest(
        NamingContext.UnionCase(
            requireNotNull(schema.enum) { "Enum requires at least 1 possible value. {\"enum\":[]}" }
                .joinToString(prefix = "Case", separator = "Or") { it!! }
        )
    )

    null -> TODO("Nested complex union case?")
}
