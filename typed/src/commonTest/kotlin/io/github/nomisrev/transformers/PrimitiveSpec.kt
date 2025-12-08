package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.checkAll
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.registry
import io.github.nomisrev.openapi.requireUnique
import io.github.nomisrev.openapi.toModel
import io.github.nomisrev.reference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalAtomicApi::class)
val PrimitiveSpec by testSuite {
    val name = NamingContext.RouteParam("value", "getBy")

    checkAll("Primitive types", Model.Primitive.String.all()) { schema ->
        Value(name, schema).primitive()
    }

    checkAll("Referenced primitives", Model.Primitive.all()) { schema, inner ->
        val context = NamingContext.Reference(schema.toString(), null)
        val actual = ResolvedSchema.Reference(context, schema).toModel(SchemaContext.Input)
        val expected = Model.Object.value(context, inner)
        Eq(expected, actual)
    }

    test("Schema.Type.Basic.Number multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) { Schema.number.copy(default = ExampleValue.Multiple(listOf("1.3", "2.5"))).primitive() }
        }
        assertEquals("Multiple default values not supported for Number.", e.message)
    }

    test("Schema.Type.Basic.Number multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) { Schema.number.copy(default = ExampleValue.Single("no-number")).primitive() }
        }
        assertEquals("Default value no-number is not a Number.", e.message)
    }

    test("Schema.Type.Basic.Integer multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(name, Schema.integer.copy(default = ExampleValue.Multiple(listOf("1", "2")))).primitive()
            }
        }
        assertEquals("Multiple default values not supported for Integer.", e.message)
    }

    test("Schema.Type.Basic.Integer single default incorrect value throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) { Value(name, Schema.integer.copy(default = ExampleValue.Single("no-int"))).primitive() }
        }
        assertEquals("Default value no-int is not a Integer.", e.message)
    }

    test("Schema.Type.Basic.Boolean multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(name, Schema.boolean.copy(default = ExampleValue.Multiple(listOf("true", "false")))).primitive()
            }
        }
        assertEquals("Multiple default values not supported for Boolean.", e.message)
    }

    test("Schema.Type.Basic.Boolean single default incorrect value throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(name, Schema.boolean.copy(default = ExampleValue.Single("no-bool"))).primitive()
            }
        }
        assertEquals("Default value no-bool is not a Boolean.", e.message)
    }
}
