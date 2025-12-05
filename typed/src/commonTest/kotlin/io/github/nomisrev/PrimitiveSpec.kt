package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.ctx
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.primitive
import io.github.nomisrev.openapi.requireUnique
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

val PrimitiveSpec by testSuite {

    test("Duplicated tests") {
        requireUnique(Model.Primitive.all())
    }

    Model.Primitive.all().forEach { (schema, expected) ->
        test(schema.toString()) {
            val actual = ctx(api) { schema.primitive() }
            assertEquals(expected, actual)
        }
    }

    test("Schema.Type.Basic.Number multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) { Schema.number.copy(default = ExampleValue.Multiple(listOf("1.3", "2.5"))).primitive() }
        }
        assertEquals("Multiple default values not supported for Number.", e.message)
    }

    test("Schema.Type.Basic.Number multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) { Schema.number.copy(default = ExampleValue.Single("no-number")).primitive() }
        }
        assertEquals("Default value no-number is not a Number.", e.message)
    }

    test("Schema.Type.Basic.Integer multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) { Schema.integer.copy(default = ExampleValue.Multiple(listOf("1", "2"))).primitive() }
        }
        assertEquals("Multiple default values not supported for Integer.", e.message)
    }

    test("Schema.Type.Basic.Integer single default incorrect value throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) { Schema.integer.copy(default = ExampleValue.Single("no-int")).primitive() }
        }
        assertEquals("Default value no-int is not a Integer.", e.message)
    }

    test("Schema.Type.Basic.Boolean multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) {
                Schema.boolean.copy(default = ExampleValue.Multiple(listOf("true", "false"))).primitive()
            }
        }
        assertEquals("Multiple default values not supported for Boolean.", e.message)
    }

    test("Schema.Type.Basic.Boolean single default incorrect value throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            ctx(api) {
                Schema.boolean.copy(default = ExampleValue.Single("no-bool")).primitive()
            }
        }
        assertEquals("Default value no-bool is not a Boolean.", e.message)
    }
}
