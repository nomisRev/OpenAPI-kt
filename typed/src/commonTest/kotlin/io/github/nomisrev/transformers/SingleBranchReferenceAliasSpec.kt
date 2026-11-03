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
 * A named component with a single `$ref` branch, `anyOf`/`oneOf: [{$ref: X}]`, is an alias of X with its own name,
 * like `allOf: [{$ref: X}]` and the nullable `anyOf`/`oneOf: [{$ref: X}, {type: null}]`: X's schema modelled under
 * the component's name. Resolving the `$ref` instead yields X's own model, so X is generated twice and the component,
 * which properties still reference, never is.
 */
val singleBranchReferenceAliasSpec by testSuite {
    val aliasName = NamingContext.reference("Alias", SchemaContext.Null)
    val string = ReferenceOr.value(Schema(type = Type.Basic.String))
    val nullSchema = ReferenceOr.value(Schema(type = Type.Basic.Null))
    val targets: List<Pair<String, Schema>> = listOf(
        "object" to Schema(
            type = Type.Basic.Object,
            properties = mapOf("name" to string),
            required = listOf("name"),
            additionalProperties = AdditionalProperties.Allowed(false),
        ),
        "enum" to Schema(type = Type.Basic.String, enum = listOf("red", "green")),
        "primitive" to Schema(type = Type.Basic.String, description = ReferenceOr.value("my string")),
        "collection" to Schema(type = Type.Basic.Array, items = string),
    )

    fun openAPI(alias: Schema, target: Schema): OpenAPI = api.reference("Alias", alias).reference("X", target)

    suspend fun aliasModel(alias: Schema, target: Schema): Model =
        registry(openAPI(alias, target)) { ReferenceOr.schema("Alias").toModel(aliasName, SchemaContext.Write) }

    suspend fun modelOf(schema: Schema, openAPI: OpenAPI): Model =
        registry(openAPI) { ReferenceOr.value(schema).toModel(NamingContext.path("test"), SchemaContext.Write) }

    val singleBranches: List<Pair<String, Schema>> = listOf(
        "anyOf: [\$ref X]" to Schema(anyOf = listOf(ReferenceOr.schema("X"))),
        "oneOf: [\$ref X]" to Schema(oneOf = listOf(ReferenceOr.schema("X"))),
    )

    targets.forEach { [kind, target] ->
        singleBranches.forEach { [label, alias] ->
            // `allOf: [{$ref: X}]` to a primitive or collection models the bare value instead of an `Alias` wrapper while
            // properties still reference `Alias` (a separate bug), so only the nullable variant is compared for those.
            if (kind == "object" || kind == "enum") test("Component $label to $kind models like allOf: [\$ref X]") {
                assertEq(aliasModel(Schema(allOf = listOf(ReferenceOr.schema("X"))), target), aliasModel(alias, target))
            }

            test("Component $label to $kind models like its nullable variant made non-null") {
                val nullableModel = aliasModel(
                    if (alias.anyOf != null) Schema(anyOf = alias.anyOf!! + nullSchema)
                    else Schema(oneOf = alias.oneOf!! + nullSchema),
                    target
                )
                assertTrue(nullableModel.isNullable, "Sanity: nullable variant should be nullable: $nullableModel")
                assertEq(nullableModel.with(isNullable = false), aliasModel(alias, target))
            }
        }
    }

    singleBranches.forEach { [label, alias] ->
        test("Component $label to an object is an object named Alias, not X") {
            val model = aliasModel(alias, targets.first().second)
            assertTrue(
                model is Model.Object && model.context == aliasName && !model.isNullable,
                "should be the non-null Alias object: $model"
            )
        }

        test("Array items and map values referencing a component $label to an object are the Alias object") {
            val openAPI = openAPI(alias, targets.first().second)
            val items =
                (modelOf(Schema(type = Type.Basic.Array, items = ReferenceOr.schema("Alias")), openAPI) as Model.Collection).inner
            val values = (modelOf(
                Schema(type = Type.Basic.Object, additionalProperties = AdditionalProperties.PSchema(ReferenceOr.schema("Alias"))),
                openAPI
            ) as Model.Dictionary).inner
            listOf(items, values).forEach { inner ->
                assertTrue(
                    inner is Model.Object && !inner.isNullable && inner.context == aliasName,
                    "should be the Alias object, not X: $inner"
                )
            }
        }

        test("Inline $label (not a component) is X itself") {
            val inline = modelOf(alias, openAPI(alias, targets.first().second))
            assertTrue(
                inline is Model.Object && inline.context == NamingContext.reference("X", SchemaContext.Null),
                "inline single-branch \$ref should be X: $inline"
            )
        }
    }
}
