package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.assertEq
import io.github.nomisrev.reference
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertTrue

/**
 * A named component that also allows `null` is a nullable alias however it is spelled:
 * `anyOf`/`oneOf: [X, {type: null}]` (in any order), `type: [X, "null"]`, or `nullable: true`.
 * Its own model is nullable, so a `$ref` to it (a property, an array item, a map value) must be
 * nullable too, or `null` on the wire cannot be decoded.
 */
val nullableAliasReferenceSpec by testSuite {
    val name = NamingContext.path("test")
    val nullSchema = ReferenceOr.value(Schema(type = Type.Basic.Null))
    val string = ReferenceOr.value(Schema(type = Type.Basic.String))
    val owner = Schema(
        type = Type.Basic.Object,
        properties = mapOf("name" to string),
        required = listOf("name"),
        additionalProperties = AdditionalProperties.Allowed(false),
    )

    suspend fun modelOf(schema: Schema, openAPI: OpenAPI): Model =
        registry(openAPI) { ReferenceOr.value(schema).toModel(name, SchemaContext.Write) }

    val holder = Schema(
        type = Type.Basic.Object,
        properties = mapOf("alias" to ReferenceOr.schema("Alias")),
        required = listOf("alias"),
    )

    suspend fun aliasProperty(alias: Schema): Model {
        val model = modelOf(holder, api.reference("Alias", alias).reference("Owner", owner))
        return (model as Model.Object).properties.getValue("alias").model
    }

    val nullableAliases: List<Pair<String, Schema>> = listOf(
        "anyOf: [string, null]" to Schema(anyOf = listOf(string, nullSchema)),
        "anyOf: [null, string]" to Schema(anyOf = listOf(nullSchema, string)),
        "oneOf: [string, null]" to Schema(oneOf = listOf(string, nullSchema)),
        "oneOf: [null, string]" to Schema(oneOf = listOf(nullSchema, string)),
        "anyOf: [\$ref Owner, null]" to Schema(anyOf = listOf(ReferenceOr.schema("Owner"), nullSchema)),
        "oneOf: [\$ref Owner, null]" to Schema(oneOf = listOf(ReferenceOr.schema("Owner"), nullSchema)),
        "type: [string, null]" to Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Null))),
        "type: string, nullable: true" to Schema(type = Type.Basic.String, nullable = true),
        "allOf: [\$ref Owner], nullable: true" to Schema(allOf = listOf(ReferenceOr.schema("Owner")), nullable = true),
    )

    nullableAliases.forEach { [label, alias] ->
        // `allOf` ignores the `nullable` next to it in the component's own model; only its `$ref`s are checked here.
        if (alias.allOf == null) test("Component $label is nullable on its own") {
            val definition = registry(api.reference("Alias", alias).reference("Owner", owner)) {
                ReferenceOr.schema("Alias").toModel(NamingContext.reference("Alias", SchemaContext.Null), SchemaContext.Write)
            }
            assertTrue(definition.isNullable, "Sanity: Alias model should be nullable: $definition")
        }

        test("Property referencing a component $label is a nullable reference") {
            val property = aliasProperty(alias)
            assertTrue(property is Model.Reference && property.isNullable, "alias should be a nullable reference: $property")
        }
    }

    test("Property referencing anyOf: [string, null] models like one referencing type: string, nullable: true") {
        assertEq(
            aliasProperty(Schema(type = Type.Basic.String, nullable = true)),
            aliasProperty(Schema(anyOf = listOf(string, nullSchema)))
        )
    }

    test("Property referencing a non-null anyOf: [string, integer] component stays non-null") {
        val alias = aliasProperty(
            Schema(anyOf = listOf(string, ReferenceOr.value(Schema(type = Type.Basic.Integer))))
        )
        assertTrue(!alias.isNullable, "alias should not be nullable: $alias")
    }

    test("Recursive reference to an anyOf: [object, null] component is nullable") {
        val node = Schema(
            anyOf = listOf(
                ReferenceOr.value(
                    Schema(type = Type.Basic.Object, properties = mapOf("next" to ReferenceOr.schema("Node")))
                ),
                nullSchema
            )
        )
        val model = registry(api.reference("Node", node)) {
            ReferenceOr.schema("Node").toModel(NamingContext.reference("Node", SchemaContext.Null), SchemaContext.Write)
        }
        val next = (model as Model.Object).properties.getValue("next").model
        assertTrue(next is Model.Reference && next.isNullable, "next should be a nullable reference: $next")
    }

    test("Array items referencing anyOf: [string, null] component are nullable") {
        val model = modelOf(
            Schema(type = Type.Basic.Array, items = ReferenceOr.schema("Alias")),
            api.reference("Alias", Schema(anyOf = listOf(string, nullSchema)))
        )
        val inner = (model as Model.Collection).inner
        assertTrue(inner.isNullable, "items should be nullable: $inner")
    }

    test("Map values referencing anyOf: [string, null] component are nullable") {
        val model = modelOf(
            Schema(type = Type.Basic.Object, additionalProperties = AdditionalProperties.PSchema(ReferenceOr.schema("Alias"))),
            api.reference("Alias", Schema(anyOf = listOf(string, nullSchema)))
        )
        val inner = (model as Model.Dictionary).inner
        assertTrue(inner.isNullable, "map values should be nullable: $inner")
    }

    // A named nullable alias of a referenced object is its own nullable type, like `allOf: [{$ref: Owner}]` is its
    // own (non-null) type: a copy of `Owner` named after the alias. Resolving it to `Owner` itself generated `Owner`
    // twice and left `$ref`s to the alias pointing at a type that was never generated.
    val refAliases: List<Pair<String, Schema>> = listOf(
        "anyOf: [\$ref Owner, null]" to Schema(anyOf = listOf(ReferenceOr.schema("Owner"), nullSchema)),
        "anyOf: [null, \$ref Owner]" to Schema(anyOf = listOf(nullSchema, ReferenceOr.schema("Owner"))),
        "oneOf: [\$ref Owner, null]" to Schema(oneOf = listOf(ReferenceOr.schema("Owner"), nullSchema)),
        "oneOf: [null, \$ref Owner]" to Schema(oneOf = listOf(nullSchema, ReferenceOr.schema("Owner"))),
        "allOf: [\$ref Owner, null]" to Schema(allOf = listOf(ReferenceOr.schema("Owner"), nullSchema)),
    )

    suspend fun aliasModel(alias: Schema): Model =
        registry(api.reference("Alias", alias).reference("Owner", owner)) {
            ReferenceOr.schema("Alias").toModel(NamingContext.reference("Alias", SchemaContext.Null), SchemaContext.Write)
        }

    refAliases.forEach { [label, alias] ->
        test("Component $label models as a nullable copy of Owner named Alias, like allOf: [\$ref Owner]") {
            val nonNullAlias = aliasModel(Schema(allOf = listOf(ReferenceOr.schema("Owner"))))
            assertTrue(
                nonNullAlias is Model.Object && nonNullAlias.context == NamingContext.reference("Alias", SchemaContext.Null),
                "Sanity: allOf: [\$ref Owner] should model as an object named Alias: $nonNullAlias"
            )
            assertEq(nonNullAlias.with(isNullable = true), aliasModel(alias))
        }

        test("Array items and map values referencing a component $label are the nullable Alias object") {
            val openAPI = api.reference("Alias", alias).reference("Owner", owner)
            val items = (modelOf(Schema(type = Type.Basic.Array, items = ReferenceOr.schema("Alias")), openAPI) as Model.Collection).inner
            val values = (modelOf(
                Schema(type = Type.Basic.Object, additionalProperties = AdditionalProperties.PSchema(ReferenceOr.schema("Alias"))),
                openAPI
            ) as Model.Dictionary).inner
            listOf(items, values).forEach { inner ->
                assertTrue(
                    inner is Model.Object && inner.isNullable &&
                        inner.context == NamingContext.reference("Alias", SchemaContext.Null),
                    "should be the nullable Alias object, not Owner: $inner"
                )
            }
        }
    }
}
