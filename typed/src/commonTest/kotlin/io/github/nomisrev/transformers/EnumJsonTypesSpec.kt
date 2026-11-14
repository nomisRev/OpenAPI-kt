package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

context(registry: Registry)
private fun warningsOfCurrentRegistry(): List<String> = registry.warnings()

/**
 * `enum` members and `default` are decided from their JSON type: `1` is not `"1"`, the JSON `null` is not the string
 * `"null"`, an array or an object is not an enum constant, and a default of another JSON type than the declared one
 * is rejected with the schema and the value.
 */
val enumJsonTypesSpec by testSuite {
    val name = NamingContext.path("test")

    suspend fun modelWithWarnings(schema: Schema): Pair<Model, List<String>> =
        registry(api) {
            val model = ReferenceOr.value(schema).toModel(name, SchemaContext.Write)
            model to warningsOfCurrentRegistry()
        }

    suspend fun modelOf(schema: Schema): Model = modelWithWarnings(schema).first

    fun ints(vararg values: Int): List<JsonElement> = values.map(::JsonPrimitive)

    test("an integer enum is made of the JSON numbers, with an integer default") {
        val model = modelOf(
            Schema(type = Schema.Type.Basic.Integer, enum = ints(300, 600, 1200), default = JsonPrimitive(300))
        )
        val enum = assertIs<Model.Enum>(model)
        assertEquals(Model.Primitive.Long(null, null, null, false, null), enum.inner)
        assertEquals(
            listOf(
                Model.EnumValue.Long(300, "300"),
                Model.EnumValue.Long(600, "600"),
                Model.EnumValue.Long(1200, "1200"),
            ),
            enum.values,
        )
        assertEquals(Model.Default.Value(Model.EnumValue.Long(300, "300")), enum.default)
    }

    test("an int32 enum has Int members, a number enum Double members and a boolean enum Boolean members") {
        val int = assertIs<Model.Enum>(
            modelOf(Schema(type = Schema.Type.Basic.Integer, format = "int32", enum = ints(1, 2)))
        )
        assertEquals(listOf(Model.EnumValue.Int(1, "1"), Model.EnumValue.Int(2, "2")), int.values)
        val number = assertIs<Model.Enum>(
            modelOf(Schema(type = Schema.Type.Basic.Number, enum = listOf(JsonPrimitive(0.5), JsonPrimitive(1.5))))
        )
        assertEquals(listOf(Model.EnumValue.Double(0.5, "0.5"), Model.EnumValue.Double(1.5, "1.5")), number.values)
        val boolean = assertIs<Model.Enum>(
            modelOf(Schema(type = Schema.Type.Basic.Boolean, enum = listOf(JsonPrimitive(true))))
        )
        assertEquals(listOf(Model.EnumValue.Boolean(true, "true")), boolean.values)
    }

    test("the integer 1 and the string 1 are different enum members") {
        val integers = assertIs<Model.Enum>(modelOf(Schema(type = Schema.Type.Basic.Integer, enum = ints(1))))
        val strings = assertIs<Model.Enum>(
            modelOf(Schema(type = Schema.Type.Basic.String, enum = listOf(JsonPrimitive("1"))))
        )
        assertEquals(listOf(Model.EnumValue.Long(1, "1")), integers.values)
        assertEquals(listOf(Model.EnumValue.String("1")), strings.values)
    }

    test("an integer enum with a numeric string member reads it as the number, with a warning") {
        val [model, warnings] = modelWithWarnings(
            Schema(type = Schema.Type.Basic.Integer, enum = listOf(JsonPrimitive(1), JsonPrimitive("2")))
        )
        assertEquals(
            listOf(Model.EnumValue.Long(1, "1"), Model.EnumValue.Long(2, "2")),
            assertIs<Model.Enum>(model).values,
        )
        assertEquals(
            listOf("Enum value \"2\" at GET /test is a string; read as the integer 2 because the schema declares type integer."),
            warnings,
        )
    }

    test("an integer enum with a member that is no integer is invalid and names the schema and the value") {
        val e = assertFailsWith<IllegalArgumentException> {
            modelOf(Schema(type = Schema.Type.Basic.Integer, enum = listOf(JsonPrimitive(1), JsonPrimitive("BASIC"))))
        }
        assertEquals("Enum value \"BASIC\" at GET /test is not a Integer.", e.message)
    }

    test("an enum without a type and with members of one JSON type takes that type") {
        val enum = assertIs<Model.Enum>(modelOf(Schema(enum = ints(1, 2))))
        assertEquals(listOf(Model.EnumValue.Long(1, "1"), Model.EnumValue.Long(2, "2")), enum.values)
    }

    test("an enum without a type that mixes the integer 1 and the string 1 is not a Kotlin enum, with a warning") {
        val [model, warnings] = modelWithWarnings(
            Schema(enum = listOf(JsonPrimitive(1), JsonPrimitive("1"), JsonPrimitive(true)))
        )
        assertTrue(model !is Model.Enum, model.toString())
        assertEquals(1, warnings.size, warnings.toString())
        assertTrue("GET /test" in warnings.single() && "integer, string, boolean" in warnings.single(), warnings.single())
    }

    test("the JSON null is a null member and the string null is a string member") {
        val enum = assertIs<Model.Enum>(
            modelOf(
                Schema(
                    type = Schema.Type.Basic.String,
                    nullable = true,
                    enum = listOf(JsonPrimitive("a"), JsonNull, JsonPrimitive("null")),
                ),
            )
        )
        assertEquals(
            listOf(Model.EnumValue.String("a"), Model.EnumValue.Null(), Model.EnumValue.String("null")),
            enum.values,
        )
    }

    test("a string default null stays the string, the JSON null default is null") {
        assertEquals(
            Model.Primitive.String(Model.Default.Value("null"), null, null, false, null),
            modelOf(Schema(type = Schema.Type.Basic.String, default = JsonPrimitive("null"))),
        )
        assertEquals(
            Model.Primitive.String(Model.Default.Null, null, null, true, null),
            modelOf(Schema(type = Schema.Type.Basic.String, nullable = true, default = JsonNull)),
        )
    }

    test("an enum with an object member is generated as the declared type, with a warning naming the schema") {
        val [model, warnings] = modelWithWarnings(
            Schema(
                type = Schema.Type.Basic.Object,
                enum = listOf(JsonObject(emptyMap())),
                default = JsonObject(emptyMap()),
            )
        )
        assertTrue(model !is Model.Enum, model.toString())
        assertEquals(1, warnings.size, warnings.toString())
        assertTrue(
            "GET /test" in warnings.single() && "type object" in warnings.single() &&
                "the declared type is used" in warnings.single(),
            warnings.single(),
        )
    }

    test("an enum with an array member next to string members is generated as String, with a warning") {
        val [model, warnings] = modelWithWarnings(
            Schema(
                type = Schema.Type.Basic.String,
                enum = listOf(
                    JsonPrimitive("all"),
                    JsonPrimitive("none"),
                    JsonArray(listOf(JsonPrimitive("project1"))),
                ),
            )
        )
        assertEquals(Model.Primitive.String(null, null, null, false, null), model)
        assertEquals(1, warnings.size, warnings.toString())
        assertTrue("[\"project1\"]" in warnings.single(), warnings.single())
    }

    test("an integer enum of arrays is generated as the integer, with a warning") {
        val [model, warnings] = modelWithWarnings(
            Schema(
                type = Schema.Type.Basic.Integer,
                enum = listOf(JsonArray(listOf(JsonPrimitive(1))), JsonArray(listOf(JsonPrimitive(2)))),
            )
        )
        assertEquals(Model.Primitive.Long(null, null, null, false, null), model)
        assertEquals(1, warnings.size, warnings.toString())
    }

    test("a string default that parses as the declared type is read as it, with a warning") {
        val [integer, integerWarnings] = modelWithWarnings(
            Schema(type = Schema.Type.Basic.Integer, default = JsonPrimitive("7"))
        )
        assertEquals(Model.Primitive.Long(Model.Default.Value(7L), null, null, false, null), integer)
        assertEquals(
            listOf("Default value \"7\" at GET /test is a string; read as the integer 7 because the schema declares type integer."),
            integerWarnings,
        )
        val [boolean, booleanWarnings] = modelWithWarnings(
            Schema(type = Schema.Type.Basic.Boolean, default = JsonPrimitive("true"))
        )
        assertEquals(Model.Primitive.Boolean(Model.Default.Value(true), null, false, null), boolean)
        assertEquals(
            listOf("Default value \"true\" at GET /test is a string; read as the boolean true because the schema declares type boolean."),
            booleanWarnings,
        )
        val [number, numberWarnings] = modelWithWarnings(
            Schema(type = Schema.Type.Basic.Number, default = JsonPrimitive("1.5"))
        )
        assertEquals(Model.Primitive.Double(Model.Default.Value(1.5), null, null, false, null), number)
        assertEquals(
            listOf("Default value \"1.5\" at GET /test is a string; read as the number 1.5 because the schema declares type number."),
            numberWarnings,
        )
    }

    test("an integer default is a number and needs no warning") {
        val [model, warnings] = modelWithWarnings(Schema(type = Schema.Type.Basic.Integer, default = JsonPrimitive(7)))
        assertEquals(Model.Primitive.Long(Model.Default.Value(7L), null, null, false, null), model)
        assertEquals(emptyList(), warnings)
    }

    test("a default that does not parse as the declared type is rejected with the value") {
        val string = assertFailsWith<IllegalArgumentException> {
            modelOf(Schema(type = Schema.Type.Basic.Integer, default = JsonPrimitive("current")))
        }
        assertEquals("Default value \"current\" at GET /test is not a Integer.", string.message)
        val boolean = assertFailsWith<IllegalArgumentException> {
            modelOf(Schema(type = Schema.Type.Basic.Boolean, default = JsonPrimitive(1)))
        }
        assertEquals("Default value 1 at GET /test is not a Boolean.", boolean.message)
        val integer = assertFailsWith<IllegalArgumentException> {
            modelOf(Schema(type = Schema.Type.Basic.Integer, default = JsonPrimitive(true)))
        }
        assertEquals("Default value true at GET /test is not a Integer.", integer.message)
    }

    test("an enum default that is no member type is rejected") {
        val e = assertFailsWith<IllegalArgumentException> {
            modelOf(Schema(type = Schema.Type.Basic.Integer, enum = ints(1, 2), default = JsonPrimitive("two")))
        }
        assertEquals("Enum value \"two\" at GET /test is not a Integer.", e.message)
    }

    test("YAML integer enum of quoted numbers reads as numbers, without a warning") {
        val [model, warnings] = modelWithWarnings(Schema.fromYaml("type: integer\nenum: ['1', '2']"))
        assertEquals(
            listOf(Model.EnumValue.Long(1, "1"), Model.EnumValue.Long(2, "2")),
            assertIs<Model.Enum>(model).values,
        )
        assertEquals(emptyList(), warnings)
    }

    test("YAML string enum of quoted numbers keeps them as strings, without a warning") {
        val [model, warnings] = modelWithWarnings(Schema.fromYaml("type: string\nenum: ['1', '2']"))
        assertEquals(
            listOf(Model.EnumValue.String("1"), Model.EnumValue.String("2")),
            assertIs<Model.Enum>(model).values,
        )
        assertEquals(emptyList(), warnings)
    }

    test("YAML untyped enum of quoted numbers is an integer enum, without a warning") {
        val [model, warnings] = modelWithWarnings(Schema.fromYaml("enum: ['1', '2']"))
        assertEquals(
            listOf(Model.EnumValue.Long(1, "1"), Model.EnumValue.Long(2, "2")),
            assertIs<Model.Enum>(model).values,
        )
        assertEquals(emptyList(), warnings)
    }
}
