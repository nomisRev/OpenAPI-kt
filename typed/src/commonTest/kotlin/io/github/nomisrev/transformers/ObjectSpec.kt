package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.ExpectedApi
import io.github.nomisrev.assertEq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verifyAll
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.forever
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.product
import io.github.nomisrev.randomChunked
import io.github.nomisrev.reference
import io.github.nomisrev.zip
import kotlin.random.Random
import kotlin.test.assertEquals

private val propNames = sequence {
    var i = 0
    while (true) yield("name${i++}")
}


private val SEED = Random.nextLong().also { println("#### SEED: $it") }
private val RANDOM = Random(SEED)

val primitives: Sequence<Expect<Schema, Model>> = sequence {
    val values = Model.Primitive.all() + Model.FreeFormJson.all()
    while (true) {
        val index = RANDOM.nextInt(values.size)
        yield(values[index])
    }
}

data class Prop(val name: String, val schema: Expect<Schema, Model>, val isRequired: Boolean)

private val properties = propNames.zip(
    primitives,
    sequenceOf(true, false).forever(),
    ::Prop
).randomChunked(minSize = 2, maxSize = 10)

private val additionalProperties = primitives.flatMap { (actual, expected) ->
    listOf(
        Allowed(true) expect Model.Object.AdditionalProperties.Allowed(true),
        Allowed(false) expect Model.Object.AdditionalProperties.Allowed(false),
        null expect Model.Object.AdditionalProperties.Allowed(false),
        PSchema(ReferenceOr.value(actual)) expect Model.Object.AdditionalProperties.Schema(expected)
    )
}

val objectSpec by testSuite {
    val aProps = Model.Primitive.all().map { (schema, model) ->
        // TODO is this correct behavior? multiple values of PSchema allowed?
        Schema(
            type = Type.Basic.Object,
            additionalProperties = PSchema(ReferenceOr.value(schema)),
        ) expect model
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(true),
            description = description.actual,
            nullable = isNullable
        ) expect Model.FreeFormJson(description.expected, null, isNullable ?: false)
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            description = description.actual,
            nullable = isNullable
        ) expect Model.Primitive.Unit(description.expected, isNullable ?: false)
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            description = description.actual,
            nullable = isNullable
        ) expect Model.FreeFormJson(description.expected, null, isNullable ?: false)
    }

    verifyAll("Additional Properties", aProps)

    fun List<Expect<Schema, Model>>.removeType() = map { (schema, model) ->
        schema.copy(type = null) expect model
    }

    verifyAll("Additional Properties without type: Object", aProps.removeType())

    verifyAll(
        "AdditionalProperties.Schema (null properties) is flattened",
        Model.Primitive.all().map { (schema, model) ->
            val topName = NamingContext.Reference("Top", null)
            val api = api.reference("Top", schema)
            val actualSchema = Schema(type = Type.Basic.Object, additionalProperties = PSchema(schema("Top")))
            val expectedModel = Model.Object.value(NamingContext.Reference("Top", null), model)
            actualSchema expect expectedModel
            ExpectedApi(actualSchema, expectedModel, api, listOf(topName))
        }
    ) { schema -> Value(NamingContext.Reference("Top", null), schema) }

    val objNames = sequenceOf(NamingContext.ObjectProperty("test")).forever()

    val obj = objNames.zip(
        properties,
        sequenceOf(true, false).forever(),
        additionalProperties
    ) { name, props, isNullable, additionalProperties ->
        val schema = Schema(
            type = Type.Basic.Object,
            properties = props.associate { (name, schema, isRequired) -> Pair(name, ReferenceOr.value(schema.actual)) },
            description = null,
            required = props.filter { it.isRequired }.map { it.name },
            additionalProperties = additionalProperties.actual,
            nullable = isNullable
        )
        val obj = Model.Object(
            context = name,
            description = null,
            properties = props.map { (name, schema, isRequired) ->
                Model.Object.Property(name, schema.expected, isRequired)
            },
            inline = emptySet(),
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable
        )
        schema expect obj
    }

    verifyAll("Object", obj.take(10_000).toList())
    verifyAll("Object without type: Object", obj.take(10_000).toList().removeType())

    test("Referenced Nested Object") {
        val topInt = NamingContext.Reference("TopInt", null)
        val api = api.reference("TopInt", Schema.integer)
        val intM = Model.Primitive.Long(null, null, null, false)
        val intObj = Model.Object.value(topInt, intM)
        val expected = Model.Object(
            NamingContext.RouteParam("get", "getBy"),
            null,
            listOf(
                Model.Object.Property("prop", intObj, false)
            ),
            emptySet(),
            false,
            false
        )
        val registry = Registry(api)
        val actual = with(registry) {
            ReferenceOr.value(
                Schema(
                    type = Type.Basic.Object,
                    properties = mapOf("prop" to schema("TopInt"))
                )
            ).toModel(NamingContext.RouteParam("get", "getBy"), SchemaContext.Input)
        }
        assertEq(expected, actual)
        assertEquals(setOf(topInt), registry.names())
    }

    val enum = Model.Enum.strings(NamingContext.ObjectProperty("enum")).map {
        val schema =
            Schema(type = Type.Basic.Object, properties = mapOf("enum" to ReferenceOr.value(it.actual)))
        val model = Model.Object(
            context = NamingContext.ObjectProperty("test"),
            description = null,
            properties = listOf(Model.Object.Property("enum", it.expected, false)),
            inline = setOf(it.expected),
            additionalProperties = false,
            isNullable = false
        )
        schema expect model
    }

    verifyAll("Enum Value", enum)

    val enumNesting = Model.Enum.strings(NamingContext.ObjectProperty("enum"))
        .map { (innerSchema, innerModel) ->
            val listSchema = Schema(type = Type.Basic.Array, items = ReferenceOr.value(innerSchema))
            val listModel = Model.Collection.List(innerModel, null, null, null, false)
            val objSchema =
                Schema(type = Type.Basic.Object, properties = mapOf("enum" to ReferenceOr.value(listSchema)))
            val model = Model.Object(
                context = NamingContext.ObjectProperty("test"),
                description = null,
                properties = listOf(Model.Object.Property("enum", listModel, false)),
                inline = setOf(innerModel),
                additionalProperties = false,
                isNullable = false
            )
            objSchema expect model
        }

    verifyAll("Enum Value nesting", enumNesting)

    val writeOnly = listOf(true, false, null)
    val readOnly = listOf(true, false, null)
    fun Sequence<List<Prop>>.randomRead(): Sequence<Expect<Pair<Map<String, ReferenceOr<Schema>>, List<String>>, List<Model.Object.Property>>> =
        map { props ->
            val props = props.map {
                Triple(
                    readOnly[RANDOM.nextInt(0, 2)],
                    writeOnly[RANDOM.nextInt(0, 2)],
                    it
                )
            }
            val actual = Pair(props.associate { (readOnly, writeOnly, prop) ->
                Pair(prop.name, ReferenceOr.value(prop.schema.actual.copy(readOnly = readOnly, writeOnly = writeOnly)))
            }, props.filter { (_, _, prop) -> prop.isRequired }.map { (_, _, prop) -> prop.name })

            val expected = props.filter { (readOnly, _, _) -> readOnly != true }.map { (_, _, prop) ->
                Model.Object.Property(
                    prop.name,
                    prop.schema.expected,
                    prop.isRequired
                )
            }
            actual expect expected
        }

    val objWithReadOnly = objNames.zip(
        properties.randomRead(),
        sequenceOf(true, false).forever(),
        additionalProperties
    ) { name, props, isNullable, additionalProperties ->

        val schema = Schema(
            type = Type.Basic.Object,
            properties = props.actual.first,
            description = null,
            required = props.actual.second,
            additionalProperties = additionalProperties.actual,
            nullable = isNullable
        )
        val obj = Model.Object(
            context = name,
            description = null,
            properties = props.expected,
            inline = emptySet(),
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable
        )
        schema expect obj
    }

    verifyAll(
        "Object with readOnly properties",
        objWithReadOnly.take(10_000).toList()
    )

    fun Sequence<List<Prop>>.randomWrite(): Sequence<Expect<Pair<Map<String, ReferenceOr<Schema>>, List<String>>, List<Model.Object.Property>>> =
        map { props ->
            val props = props.map {
                Triple(readOnly[RANDOM.nextInt(0, 2)], writeOnly[RANDOM.nextInt(0, 2)], it)
            }
            val actual = Pair(props.associate { (readOnly, writeOnly, prop) ->
                Pair(prop.name, ReferenceOr.value(prop.schema.actual.copy(readOnly = readOnly, writeOnly = writeOnly)))
            }, props.filter { (_, _, prop) -> prop.isRequired }.map { (_, _, prop) -> prop.name })

            val expected = props.filter { (_, writeOnly, _) -> writeOnly != true }.map { (_, _, prop) ->
                Model.Object.Property(
                    prop.name,
                    prop.schema.expected,
                    prop.isRequired
                )
            }
            actual expect expected
        }

    val objWithWriteOnly = objNames.zip(
        properties.randomWrite(),
        sequenceOf(true, false).forever(),
        additionalProperties
    ) { name, props, isNullable, additionalProperties ->

        val schema = Schema(
            type = Type.Basic.Object,
            properties = props.actual.first,
            description = null,
            required = props.actual.second,
            additionalProperties = additionalProperties.actual,
            nullable = isNullable
        )
        val obj = Model.Object(
            context = name,
            description = null,
            properties = props.expected,
            inline = emptySet(),
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable
        )
        schema expect obj
    }

    verifyAll(
        "Object with writeOnly properties",
        objWithWriteOnly.take(10_000).toList(),
        SchemaContext.Output
    )
}