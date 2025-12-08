package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.assertEq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.checkAll
import io.github.nomisrev.default
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.get
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry
import io.github.nomisrev.openapi.toModel
import io.github.nomisrev.product
import io.github.nomisrev.reference
import io.github.nomisrev.zip
import kotlin.math.exp
import kotlin.random.Random
import kotlin.test.assertEquals

private val propNames = sequence {
    var i = 0
    while (true) yield("name${i++}")
}

private val SEED = Random.nextLong().also { println("#### SEED: $it") }
private val RANDOM = Random(SEED)

data class Prop(val name: String, val schema: Expect<Schema, Model>, val isRequired: Boolean)

fun <A> Sequence<A>.randomChunked(minSize: Int, maxSize: Int): Sequence<List<A>> = sequence {
    val buffer = mutableListOf<A>()
    var maxLength = Random.nextInt(minSize, maxSize)
    forEach {
        buffer.add(it)
        if (buffer.size == maxLength) {
            yield(buffer.toList())
            buffer.clear()
            maxLength = Random.nextInt(minSize, maxSize)
        }
    }
}

fun <A> Sequence<A>.forever(): Sequence<A> = sequence {
    while (true) yieldAll(this@forever)
}

val objectSpec by testSuite {
    val aProps = Model.Primitive.all().map { (schema, model) ->
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

    checkAll("Additional Properties", aProps) { schema ->
        Value(NamingContext.RouteParam("Nested", "getBy"), schema).toModel(SchemaContext.Input)
    }

    fun List<Expect<Schema, Model>>.removeType() = map { (schema, model) ->
        schema.copy(type = null) expect model
    }

    checkAll("Additional Properties without type: Object", aProps.removeType()) {
        Value(NamingContext.RouteParam("Nested", "getBy"), it).toModel(SchemaContext.Input)
    }

    val primitive: Sequence<Expect<Schema, Model>> = sequence {
        val values = Model.Primitive.all() + Model.FreeFormJson.all()
        while (true) {
            val index = RANDOM.nextInt(values.size)
            yield(values[index])
        }
    }

    val properties = propNames.zip(
        primitive,
        sequenceOf(true, false).forever(),
        ::Prop
    ).randomChunked(minSize = 2, maxSize = 10)

    val additionalProperties = sequenceOf(
        Allowed(true) expect true,
        Allowed(false) expect false,
        null expect false
    )

    val objNames = sequenceOf(NamingContext.RouteParam("value", "getBy")).forever()
    val obj = objNames.zip(
        properties,
        sequenceOf(true, false).forever(),
        additionalProperties.forever()
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
                Model.Object.Property(name, schema.expected, isRequired, schema.expected.description)
            },
            inline = emptySet(),
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable
        )
        schema expect obj
    }

    checkAll("Object", obj.take(5000).toList()) { schema ->
        Value(NamingContext.RouteParam("value", "getBy"), schema)
            .toModel(SchemaContext.Input)
    }

    checkAll("Object without type: Object", obj.take(5000).toList().removeType()) { schema ->
        Value(NamingContext.RouteParam("value", "getBy"), schema)
            .toModel(SchemaContext.Input)
    }

    test("Referenced Nested Object") {
        val topInt = NamingContext.Reference("TopInt", null)
        val api = api.reference("TopInt", Schema.integer)
        val intM = Model.Primitive.Long(null, null, null, false)
        val intObj = Model.Object.value(topInt, intM)
        val expected = Model.Object(
            NamingContext.RouteParam("get", "getBy"),
            null,
            listOf(
                Model.Object.Property("prop", intObj, false, null)
            ),
            emptySet(),
            false,
            false
        )
        val registry = Registry(api)
        val actual = with(registry) {
            Value(
                NamingContext.RouteParam("get", "getBy"),
                Schema(
                    type = Type.Basic.Object,
                    properties = mapOf("prop" to ReferenceOr.schema("TopInt"))
                )
            ).toModel(SchemaContext.Input)
        }
        assertEq(expected, actual)
        assertEquals(listOf(topInt), registry.names())
    }

    val enum = Model.Enum.strings(NamingContext.ObjectProperty("enum"))
        .flatMap {
            listOf(
                it.actual expect it.expected,
                Schema(type = Type.Basic.Array, items = ReferenceOr.value(it.actual)) expect Model.Collection.List(
                    it.expected,
                    null,
                    null,
                    null,
                    false
                ),
            )
        }.map {
            val schema =
                Schema(type = Type.Basic.Object, properties = mapOf("enum" to ReferenceOr.value(it.actual)))
            val model = Model.Object(
                context = NamingContext.RouteParam("Nested", "getBy"),
                description = null,
                properties = listOf(Model.Object.Property("enum", it.expected, false, it.expected.description)),
                inline = setOf(it.expected),
                additionalProperties = false,
                isNullable = false
            )
            schema expect model
        }

    checkAll("Enum Value (and nesting)", enum) { schema ->
        Value(NamingContext.RouteParam("Nested", "getBy"), schema).toModel(SchemaContext.Input)
    }
}