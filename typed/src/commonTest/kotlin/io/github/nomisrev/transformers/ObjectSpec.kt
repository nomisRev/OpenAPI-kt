package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.ExpectedApi
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.context
import io.github.nomisrev.verifyAll
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.forever
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.ResolvedSchema.Value
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.product
import io.github.nomisrev.randomChunked
import io.github.nomisrev.reference
import io.github.nomisrev.zip
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.to

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
        Schema(
            type = Type.Basic.Object,
            additionalProperties = PSchema(ReferenceOr.value(schema)),
        ) expect Model.Collection(
            inner = model,
            Model.Default.Value(emptyList()),
            null,
            null,
            false,
            null
        )
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(true),
            description = description.actual,
            nullable = isNullable
        ) expect Model.FreeFormJson(description.expected, null, isNullable ?: false, null)
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            description = description.actual,
            nullable = isNullable
        ) expect Model.Primitive.Unit(description.expected, isNullable ?: false, null)
    } + description.product(listOf(true, false, null)) { description, isNullable ->
        Schema(
            type = Type.Basic.Object,
            description = description.actual,
            nullable = isNullable
        ) expect Model.FreeFormJson(description.expected, null, isNullable ?: false, null)
    }

    verifyAll("Additional Properties", aProps)

    fun List<Expect<Schema, Model>>.removeType() = map { (schema, model) ->
        schema.copy(type = null) expect model
    }

    verifyAll("Additional Properties without type: Object", aProps.removeType())

    verifyAll(
        "AdditionalProperties referenced schema remains referenced",
        Model.Primitive.all().map { (schema, model) ->
            val api = api.reference("Top", schema)
            val actualSchema = Schema(type = Type.Basic.Object, additionalProperties = PSchema(schema("Top")))
            val expectedModel = Model.Collection(
                Model.Object.value(
                    NamingContext.Reference("Top", SchemaContext.Null),
                    model,
                    isScalarWrapper = true,
                ),
                Model.Default.Value(emptyList()),
                null,
                null,
                false,
                null
            )
            ExpectedApi(actualSchema, expectedModel, api, listOf(NamingContext.Reference("Top", SchemaContext.Null)))
        }
    ) { schema -> Value(NamingContext.path("Test"), schema) }

    val objNames = sequenceOf(NamingContext.path("test")).forever()

    val obj = objNames.zip(
        properties,
        sequenceOf(true, false).forever(),
        additionalProperties
    ) { name, props, isNullable, additionalProperties ->
        val schema = Schema(
            type = Type.Basic.Object,
            properties = props.associate { (propName, schema, _) -> Pair(propName, ReferenceOr.value(schema.actual)) },
            description = null,
            required = props.filter { it.isRequired }.map { it.name },
            additionalProperties = additionalProperties.actual,
            nullable = isNullable
        )
        val obj: Model.Object = Model.Object(
            context = name,
            description = null,
            title = null,
            properties = props.associate { (propName, schema, isRequired) ->
                propName to Model.Object.Property(schema.expected, isRequired)
            },
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable
        )
        schema expect obj
    }

    verifyAll("Object", obj.take(10_000).toList())
    verifyAll("Object without type: Object", obj.take(10_000).toList().removeType())

    val enum = Model.Enum.strings(NamingContext.path("test")).map {
        val schema =
            Schema(type = Type.Basic.Object, properties = mapOf("enum" to ReferenceOr.value(it.actual)))
        val model = Model.Object(
            context = NamingContext.path("test"),
            description = null,
            title = null,
            properties = mapOf(
                "enum" to Model.Object.Property(
                    it.expected.context { ctx -> ctx.nest(NamingContext.ObjectProperty("enum")) },
                    false
                )
            ),
            additionalProperties = false,
            isNullable = false
        )
        schema expect model
    }

    verifyAll("Enum Value", enum)

    val enumNesting = Model.Enum.strings(NamingContext.path("test"))
        .map { (innerSchema, innerModel) ->
            val listSchema = Schema(type = Type.Basic.Array, items = ReferenceOr.value(innerSchema))
            val listModel = Model.Collection(
                innerModel.context { it.nest(NamingContext.ObjectProperty("enum")) },
                null,
                null,
                null,
                false,
                null
            )
            val objSchema =
                Schema(type = Type.Basic.Object, properties = mapOf("enum" to ReferenceOr.value(listSchema)))
            val model = Model.Object(
                context = NamingContext.path("test"),
                description = null,
                title = null,
                properties = mapOf("enum" to Model.Object.Property(listModel, false)),
                additionalProperties = false,
                isNullable = false
            )
            objSchema expect model
        }

    verifyAll("Enum Value nesting", enumNesting)

    val writeOnly = listOf(true, false, null)
    val readOnly = listOf(true, false, null)
    fun Sequence<List<Prop>>.randomRead() =
        map { props ->
            val readWriteProps = props.map {
                Triple(
                    readOnly[RANDOM.nextInt(0, 2)],
                    writeOnly[RANDOM.nextInt(0, 2)],
                    it
                )
            }
            val actual = Pair(readWriteProps.associate { (readOnly, writeOnly, prop) ->
                Pair(prop.name, ReferenceOr.value(prop.schema.actual.copy(readOnly = readOnly, writeOnly = writeOnly)))
            }, readWriteProps.filter { (_, _, prop) -> prop.isRequired }.map { (_, _, prop) -> prop.name })

            val expected = readWriteProps.filter { (readOnly, _, _) -> readOnly != true }.associate { (_, _, prop) ->
                prop.name to Model.Object.Property(
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
            title = null,
            properties = props.expected,
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable,
            hadPropertiesBeforeStripping = props.actual.first.size > props.expected.size,
        )
        schema expect obj
    }

    verifyAll(
        "Object with readOnly properties",
        objWithReadOnly.take(10_000).toList()
    )

    fun Sequence<List<Prop>>.randomWrite() =
        map { props ->
            val readWriteProps = props.map {
                Triple(readOnly[RANDOM.nextInt(0, 2)], writeOnly[RANDOM.nextInt(0, 2)], it)
            }
            val actual = Pair(readWriteProps.associate { (readOnly, writeOnly, prop) ->
                Pair(prop.name, ReferenceOr.value(prop.schema.actual.copy(readOnly = readOnly, writeOnly = writeOnly)))
            }, readWriteProps.filter { (_, _, prop) -> prop.isRequired }.map { (_, _, prop) -> prop.name })

            val expected = readWriteProps.filter { (_, writeOnly, _) -> writeOnly != true }.associate { (_, _, prop) ->
                prop.name to Model.Object.Property(
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
            title = null,
            properties = props.expected,
            additionalProperties = additionalProperties.expected,
            isNullable = isNullable,
            hadPropertiesBeforeStripping = props.actual.first.size > props.expected.size,
        )
        schema expect obj
    }

    verifyAll(
        "Object with writeOnly properties",
        objWithWriteOnly.take(10_000).toList(),
        SchemaContext.Read
    )

    verifyAll(
        "Empty Objects", listOf(
            Schema(
                type = Type.Basic.Object,
                additionalProperties = Allowed(false),
                properties = emptyMap()
            ),
            Schema(
                type = Type.Basic.Object,
                additionalProperties = Allowed(false)
            ),
            Schema(additionalProperties = Allowed(false))
        ).map { schema ->
            ExpectedApi(
                schema,
                Model.Object(
                    context = NamingContext.reference("EmptyObject", SchemaContext.Null),
                    description = null,
                    title = null,
                    properties = emptyMap(),
                    additionalProperties = Model.Object.AdditionalProperties.Allowed(false),
                    isNullable = false
                ),
                api.reference("EmptyObject", schema),
                listOf(NamingContext.Reference("EmptyObject", SchemaContext.Null))
            )
        }) { ResolvedSchema.Reference(NamingContext.Reference("EmptyObject", SchemaContext.Null), it) }

    test("Inline AdditionalProperties scheme inherits outer context if schema is empty") {
        val s = Schema(
            type = Type.Basic.Object,
            additionalProperties = PSchema(
                ReferenceOr.value(
                    Schema(
                        type = Type.Basic.Object,
                        properties = mapOf(
                            "filename" to ReferenceOr.value(Schema.string),
                            "type" to ReferenceOr.value(Schema.string),
                        )
                    )
                )
            )
        )
        val expected = Model.Collection(
            inner = Model.Object(
                context = NamingContext.reference("test", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "filename" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
                    "type" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false)
                ),
                additionalProperties = false,
                isNullable = false
            ),
            description = null,
            title = null,
            default = Model.Default.Value(emptyList()),
            constraint = null,
            isNullable = false
        )
        registry(api) {
            val actual = ReferenceOr.value(s)
                .toModel(NamingContext.reference("test", SchemaContext.Null), SchemaContext.Null)
            assertEquals(expected, actual)
        }
    }

    //             file_search:
    //              type: object
    //              properties:
    //                vector_store_ids:
    //                  type: array
    //                  maxItems: 1
    //                  items:
    //                    type: string
    //                vector_stores:
    //                  type: array
    //                  description: >
    //                    A helper to create a [vector
    //                    store](/docs/api-reference/vector-stores/object) with
    //                    file_ids and attach it to this assistant. There can be a
    //                    maximum of 1 vector store attached to the assistant.
    //                  maxItems: 1
    //                  items:
    //                    type: object
    //                    properties:
    //                      file_ids:
    //                        type: array
    //                        description: >
    //                          A list of [file](/docs/api-reference/files) IDs to add
    //                          to the vector store. There can be a maximum of 10000
    //                          files in a vector store.
    //                        maxItems: 10000
    //                        items:
    //                          type: string
    //              oneOf:
    //                - required:
    //                    - vector_store_ids
    //                - required:
    //                    - vector_stores
    test("Object with oneOf requires") {
        val s = Schema(
            type = Type.Basic.Object,
            properties = mapOf(
                "vector_store_ids" to ReferenceOr.value(
                    Schema(
                        type = Type.Basic.Array,
                        items = ReferenceOr.value(Schema.string)
                    )
                ),
                "vector_store" to ReferenceOr.value(
                    Schema(
                        type = Type.Basic.Array,
                        items = ReferenceOr.value(Schema.string)
                    )
                ),
            ),
            oneOf = listOf(
                ReferenceOr.value(Schema(required = listOf("vector_store_ids"))),
                ReferenceOr.value(Schema(required = listOf("vector_store")))
            )
        )
        val expected = Model.Object(
            context = NamingContext.reference("test", SchemaContext.Null),
            description = null,
            title = null,
            properties = mapOf(
                "vector_store_ids" to Model.Object.Property(
                    Model.Collection(
                        inner = Model.Primitive.String(null, null, null, false, null),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    false
                ),
                "vector_store" to Model.Object.Property(
                    Model.Collection(
                        inner = Model.Primitive.String(null, null, null, false, null),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    false
                )
            ),
            false,
            false
        )
        registry(api) {
            assertEquals(
                expected,
                ReferenceOr.value(s).toModel(NamingContext.reference("test", SchemaContext.Null), SchemaContext.Null)
            )
        }
    }

    // ─── hadPropertiesBeforeStripping tests ───────────────────────────────────────

    test("hadPropertiesBeforeStripping is false when schema has no properties") {
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = emptyMap()
        )
        val apiWithSchema = api.reference("Foo", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("Foo")
                    .toModel(NamingContext.reference("Foo", SchemaContext.Null), SchemaContext.Write)
            }
            val obj = actual as Model.Object
            assertEquals(
                false, obj.hadPropertiesBeforeStripping,
                "Schema with no properties should not have hadPropertiesBeforeStripping=true"
            )
        }
    }

    test("hadPropertiesBeforeStripping is false when no properties are stripped") {
        // All properties are writable (no readOnly) — nothing to strip in Write context
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = mapOf(
                "name" to ReferenceOr.value(Schema.string),
                "age" to ReferenceOr.value(Schema.integer)
            )
        )
        val apiWithSchema = api.reference("Foo", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("Foo")
                    .toModel(NamingContext.reference("Foo", SchemaContext.Null), SchemaContext.Write)
            }
            val obj = actual as Model.Object
            assertEquals(
                false, obj.hadPropertiesBeforeStripping,
                "Schema with no readOnly properties should not have hadPropertiesBeforeStripping=true"
            )
        }
    }

    test("hadPropertiesBeforeStripping is true when all properties are stripped (all readOnly, Write context)") {
        // All properties are readOnly — Write context strips all of them
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = mapOf(
                "id" to ReferenceOr.value(Schema.string.copy(readOnly = true)),
                "createdAt" to ReferenceOr.value(Schema.string.copy(readOnly = true))
            )
        )
        val apiWithSchema = api.reference("AllReadOnly", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("AllReadOnly")
                    .toModel(NamingContext.reference("AllReadOnly", SchemaContext.Write), SchemaContext.Write)
            }
            val obj = actual as Model.Object
            assertEquals(
                true, obj.hadPropertiesBeforeStripping,
                "Schema with all readOnly properties should have hadPropertiesBeforeStripping=true in Write context"
            )
            assertEquals(
                emptyMap(), obj.properties,
                "All properties should be stripped in Write context"
            )
        }
    }

    test("hadPropertiesBeforeStripping is true when some but not all properties are stripped (Write context)") {
        // Some readOnly, some writable — Write context strips readOnly ones
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = mapOf(
                "id" to ReferenceOr.value(Schema.string.copy(readOnly = true)),
                "name" to ReferenceOr.value(Schema.string)
            )
        )
        val apiWithSchema = api.reference("PartialReadOnly", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("PartialReadOnly")
                    .toModel(NamingContext.reference("PartialReadOnly", SchemaContext.Write), SchemaContext.Write)
            }
            val obj = actual as Model.Object
            assertEquals(
                true, obj.hadPropertiesBeforeStripping,
                "Schema with some readOnly properties should have hadPropertiesBeforeStripping=true in Write context"
            )
            assertEquals(
                setOf("name"), obj.properties.keys,
                "Only the writable property should survive"
            )
        }
    }

    test("hadPropertiesBeforeStripping is false when schema has single property by design (value class case)") {
        // Exactly one property, no readOnly — should remain value class candidate
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = mapOf(
                "value" to ReferenceOr.value(Schema.string)
            )
        )
        val apiWithSchema = api.reference("Tag", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("Tag")
                    .toModel(NamingContext.reference("Tag", SchemaContext.Null), SchemaContext.Write)
            }
            val obj = actual as Model.Object
            assertEquals(
                false, obj.hadPropertiesBeforeStripping,
                "Single-property schema with no stripping should have hadPropertiesBeforeStripping=false"
            )
        }
    }

    test("Read context: hadPropertiesBeforeStripping is true when writeOnly properties are stripped") {
        // Some writeOnly, some readable — Read context strips writeOnly ones
        val schema = Schema(
            type = Type.Basic.Object,
            additionalProperties = Allowed(false),
            properties = mapOf(
                "id" to ReferenceOr.value(Schema.string),
                "password" to ReferenceOr.value(Schema.string.copy(writeOnly = true))
            )
        )
        val apiWithSchema = api.reference("WithWriteOnly", schema)
        Registry(apiWithSchema).use { registry ->
            val actual = with(registry) {
                ReferenceOr.schema("WithWriteOnly")
                    .toModel(NamingContext.reference("WithWriteOnly", SchemaContext.Read), SchemaContext.Read)
            }
            val obj = actual as Model.Object
            assertEquals(
                true, obj.hadPropertiesBeforeStripping,
                "Schema with writeOnly properties should have hadPropertiesBeforeStripping=true in Read context"
            )
            assertEquals(
                setOf("id"), obj.properties.keys,
                "Only the readable property should survive"
            )
        }
    }
}
