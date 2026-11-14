package io.github.nomisrev.transformers

import kotlinx.serialization.json.JsonNull
import io.github.nomisrev.enumOf

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
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.assertTrue

/**
 * OpenAPI 3.1 `type: [X, "null"]` with exactly one non-null type is the same schema as OpenAPI 3.0
 * `type: X, nullable: true`, so it must produce the same (nullable) model, keeping `format`, `enum`,
 * `properties` and `items`, instead of a one-case union.
 */
val typeArrayNullableSpec by testSuite {
    val name = NamingContext.path("test")

    suspend fun modelOf(schema: Schema, openAPI: OpenAPI = api): Model =
        registry(openAPI) { ReferenceOr.value(schema).toModel(name, SchemaContext.Write) }

    test("[string, null] is a nullable String") {
        assertEq(
            Model.Primitive.String(null, null, null, true, null),
            modelOf(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Null))))
        )
    }

    test("[null, string] is a nullable String regardless of order") {
        assertEq(
            Model.Primitive.String(null, null, null, true, null),
            modelOf(Schema(type = Type.Array(listOf(Type.Basic.Null, Type.Basic.String))))
        )
    }

    test("[string] without null is a plain String") {
        assertEq(
            Model.Primitive.String(null, null, null, false, null),
            modelOf(Schema(type = Type.Array(listOf(Type.Basic.String))))
        )
    }

    // Every single-type variant must model exactly like its OpenAPI 3.0 `nullable: true` spelling.
    val singleTypeSchemas: List<Pair<String, Schema>> = listOf(
        "integer int32" to Schema(type = Type.Basic.Integer, format = "int32"),
        "integer int64" to Schema(type = Type.Basic.Integer, format = "int64"),
        "integer" to Schema(type = Type.Basic.Integer),
        "number" to Schema(type = Type.Basic.Number),
        "number float" to Schema(type = Type.Basic.Number, format = "float"),
        "boolean" to Schema(type = Type.Basic.Boolean),
        "string date-time" to Schema(type = Type.Basic.String, format = "date-time"),
        "string uuid" to Schema(type = Type.Basic.String, format = "uuid"),
        "string with description" to Schema(
            type = Type.Basic.String,
            description = ReferenceOr.value("A nullable string")
        ),
        "string enum with null" to Schema(
            type = Type.Basic.String,
            enum = enumOf("active", "inactive", null)
        ),
        "string const" to Schema(type = Type.Basic.String, `const` = JsonPrimitive("fixed")),
        "object with properties" to Schema(
            type = Type.Basic.Object,
            properties = mapOf("name" to ReferenceOr.value(Schema(type = Type.Basic.String))),
            required = listOf("name"),
            additionalProperties = AdditionalProperties.Allowed(false)
        ),
        "array with items" to Schema(
            type = Type.Basic.Array,
            items = ReferenceOr.value(Schema(type = Type.Basic.String))
        ),
    )

    singleTypeSchemas.forEach { [label, base] ->
        val basic = base.type as Type.Basic
        test("[$label, null] models like $label with nullable: true") {
            val expected = modelOf(base.copy(nullable = true))
            assertTrue(expected.isNullable, "Sanity: $expected should be nullable")
            assertEq(expected, modelOf(base.copy(type = Type.Array(listOf(basic, Type.Basic.Null)))))
            assertEq(expected, modelOf(base.copy(type = Type.Array(listOf(Type.Basic.Null, basic)))))
        }

        test("[$label] models like $label") {
            assertEq(modelOf(base), modelOf(base.copy(type = Type.Array(listOf(basic)))))
        }
    }

    test("Property referencing a named [string, null] schema is nullable") {
        val holder = Schema(
            type = Type.Basic.Object,
            properties = mapOf("alias" to ReferenceOr.schema("Alias")),
            required = listOf("alias"),
        )
        val typeArray = modelOf(
            holder,
            api.reference("Alias", Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Null))))
        )
        val nullable = modelOf(
            holder,
            api.reference("Alias", Schema(type = Type.Basic.String, nullable = true))
        )
        assertEq(nullable, typeArray)
        val alias = (typeArray as Model.Object).properties.getValue("alias").model
        assertTrue(alias.isNullable, "alias should be nullable: $alias")
    }

    test("[string, integer, null] stays a nullable union") {
        val model = modelOf(
            Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Integer, Type.Basic.Null)))
        )
        assertTrue(model is Model.OneOf && model.isNullable && model.cases.size == 2, "Expected nullable 2-case union: $model")
    }

    test("[string, null] with default null keeps the null default") {
        assertEq(
            modelOf(Schema(type = Type.Basic.String, nullable = true, default = JsonNull)),
            modelOf(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Null)), default = JsonNull))
        )
    }
}
