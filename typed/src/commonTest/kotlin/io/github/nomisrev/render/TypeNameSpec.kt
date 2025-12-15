package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.Expect
import io.github.nomisrev.all
import io.github.nomisrev.description
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.render.toTypeName
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import kotlin.jvm.JvmName
import kotlin.test.assertEquals

fun Model.Primitive.Unit.Companion.all() =
    description.product(listOf(true, false)) { desc, isNullable ->
        Model.Primitive.Unit(desc.expected, isNullable)
    }

val typeNameSpec by testSuite {
    verify(Model.Primitive.Long.all(), TypeName.Long)
    verify(Model.Primitive.Int.all(), TypeName.Int)
    verify(Model.Primitive.String.all(), TypeName.String)
    verify(Model.Primitive.Double.all(), TypeName.Double)
    verify(Model.Primitive.Float.all(), TypeName.Float)
    verify(Model.Primitive.Boolean.all(), TypeName.Boolean)
    verify(Model.Uuid.all(), TypeName.Uuid)
    verify(Model.Date.all(), TypeName.Date)
    verify(Model.DateTime.all(), TypeName.DateTime)
    verify(Model.ByteArray.all(), TypeName.ByteArray)
    verify(Model.FreeFormJson.all(), TypeName.JsonElement)
    verify(Model.Primitive.Unit.all(), TypeName.Unit)

    test("TypeName.JsonObject") {
        val actual = renderer {
            Model.Object(
                context = NamingContext.Reference("Foo", SchemaContext.Null),
                description = null,
                title = null,
                properties = emptyList(),
                additionalProperties = true,
                isNullable = false,
                inline = emptySet()
            ).toTypeName()
        }.first
        assertEquals(TypeName.JsonObject, actual)
    }

    test("TypeName.JsonArray") {
        val actual = renderer {
            Model.Collection(
                Model.FreeFormJson(null, null, false),
                null, null, null, false
            ).toTypeName()
        }.first
        assertEquals(TypeName.JsonArray, actual)
    }
}

@TestRegistering
private fun TestSuite.verify(all: List<Model>, expected: TypeName) =
    test("$expected") {
        all.forEach { model ->
            assertEquals(expected, renderer { model.toTypeName() }.first)
        }
    }

@JvmName("verifyAll")
@TestRegistering
private fun TestSuite.verify(all: List<Expect<Schema, Model>>, expected: TypeName) =
    verify(all.map { it.expected }, expected)
