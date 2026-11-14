package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

context(registry: Registry)
private fun warningsOfCurrentRegistry(): List<String> = registry.warnings()

/**
 * An explicit `default: null` on a schema that does not permit `null` is no default (with a warning); where the schema
 * permits `null` it stays [Model.Default.Null].
 */
val nullDefaultSpec by testSuite {
    val name = NamingContext.path("test")

    suspend fun modelWithWarnings(schema: Schema): Pair<Model, List<String>> =
        registry(api) {
            val model = ReferenceOr.value(schema).toModel(name, SchemaContext.Write)
            model to warningsOfCurrentRegistry()
        }

    fun assertIgnored(warnings: List<String>) =
        assertTrue(warnings.any { "Default `null`" in it && "test" in it }, "no warning in $warnings")

    val str = Schema(type = Type.Basic.String, default = JsonNull)
    val one = ReferenceOr.value(Schema(type = Type.Basic.String))
    val two = ReferenceOr.value(Schema(type = Type.Basic.Integer))

    test("a non-nullable string takes no default") {
        val [model, warnings] = modelWithWarnings(str)
        assertNull(assertIs<Model.Primitive.String>(model).default)
        assertIgnored(warnings)
    }
    test("a nullable string keeps Default.Null") {
        val [model, warnings] = modelWithWarnings(str.copy(nullable = true))
        assertEquals(Model.Default.Null, assertIs<Model.Primitive.String>(model).default)
        assertTrue(warnings.isEmpty())
    }
    test("a 3.1 type array with null keeps Default.Null") {
        val [model, _] = modelWithWarnings(str.copy(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Null))))
        assertEquals(Model.Default.Null, assertIs<Model.Primitive.String>(model).default)
    }
    test("non-nullable integer, number and boolean take no default") {
        for (type in listOf(Type.Basic.Integer, Type.Basic.Number, Type.Basic.Boolean)) {
            val [model, warnings] = modelWithWarnings(Schema(type = type, default = JsonNull))
            val default = when (model) {
                is Model.Primitive.Long -> model.default
                is Model.Primitive.Double -> model.default
                is Model.Primitive.Boolean -> model.default
                else -> error("unexpected $model")
            }
            assertNull(default)
            assertIgnored(warnings)
        }
    }
    test("a non-nullable enum takes no default") {
        val [model, warnings] = modelWithWarnings(
            Schema(
                type = Type.Basic.String,
                enum = listOf(JsonPrimitive("S"), JsonPrimitive("O")),
                default = JsonNull,
            )
        )
        assertNull(assertIs<Model.Enum>(model).default)
        assertIgnored(warnings)
    }
    test("an enum containing null, or a nullable enum, keeps Default.Null") {
        val members = listOf(JsonPrimitive("S"), JsonNull)
        val withNull = assertIs<Model.Enum>(
            modelWithWarnings(Schema(type = Type.Basic.String, enum = members, default = JsonNull)).first
        )
        assertEquals(Model.Default.Null, withNull.default)
        val nullable = assertIs<Model.Enum>(
            modelWithWarnings(
                Schema(type = Type.Basic.String, enum = listOf(JsonPrimitive("S")), nullable = true, default = JsonNull)
            ).first
        )
        assertEquals(Model.Default.Null, nullable.default)
    }
    test("a non-nullable array takes no default, a nullable one keeps Default.Null") {
        val array = Schema(type = Type.Basic.Array, items = one, default = JsonNull)
        val [model, warnings] = modelWithWarnings(array)
        assertNull(assertIs<Model.Collection>(model).default)
        assertIgnored(warnings)
        assertEquals(
            Model.Default.Null,
            assertIs<Model.Collection>(modelWithWarnings(array.copy(nullable = true)).first).default,
        )
    }
    test("a oneOf takes no default unless it is nullable") {
        val plain = Schema(oneOf = listOf(one, two), default = JsonNull)
        val [model, warnings] = modelWithWarnings(plain)
        assertNull(assertIs<Model.OneOf>(model).default)
        assertIgnored(warnings)
        val nullable = modelWithWarnings(plain.copy(nullable = true)).first
        assertEquals(Model.Default.Null, assertIs<Model.OneOf>(nullable).default)
    }
    test("an anyOf takes no default unless it is nullable") {
        val plain = Schema(anyOf = listOf(one, two), default = JsonNull)
        val [model, warnings] = modelWithWarnings(plain)
        assertNull(assertIs<Model.AnyOf>(model).default)
        assertIgnored(warnings)
        val nullable = modelWithWarnings(plain.copy(nullable = true)).first
        assertEquals(Model.Default.Null, assertIs<Model.AnyOf>(nullable).default)
    }
}
