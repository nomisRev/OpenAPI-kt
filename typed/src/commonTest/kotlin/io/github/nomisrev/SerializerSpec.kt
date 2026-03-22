package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.Union.CaseBoolean
import io.github.nomisrev.Union.CaseInt
import io.github.nomisrev.Union.CaseLong
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromJsonElement
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: String): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Int): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Boolean): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Double): JsonElement? = put(key, JsonPrimitive(element))

fun JsonObjectBuilder.putAll(jsonObject: JsonObject?) =
    jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }

val serializerSpec by testSuite {
    fun nested(index: Int): JsonObject = buildJsonObject {
        put("config1", "test$index")
        put("config2", index)
    }

    verify(
        "John - no additional schema",
        PersonWithAdditionalPropertiesSchema("John", 20),
        buildJsonObject {
            put("name", "John")
            put("age", 20)
        }
    )

    verify(
        "John - with additional schema",
        PersonWithAdditionalPropertiesSchema(
            "John",
            20,
            mapOf(
                "test1" to PersonWithAdditionalPropertiesSchema.NestedClass("test1", 1),
                "test2" to PersonWithAdditionalPropertiesSchema.NestedClass("test2", 2)
            )
        ),
        buildJsonObject {
            put("name", "John")
            put("age", 20)
            put("test1", nested(1))
            put("test2", nested(2))
        }
    )

    verify(
        "John - with JsonNull age",
        PersonWithAdditionalPropertiesSchema(
            "John",
            null,
            mapOf("test1" to PersonWithAdditionalPropertiesSchema.NestedClass("test1", 1))
        ),
        buildJsonObject {
            put("name", "John")
            put("age", JsonNull)
            put("test1", nested(1))
        }
    )

    test("age: Not a number") {
        val ex = assertFailsWith<SerializationException> {
            Json.decodeFromJsonElement<PersonWithAdditionalPropertiesSchema>(
                buildJsonObject {
                    put("name", "John")
                    put("age", "not-a-number")
                }
            )
        }
        assertEquals(
            """
            |Failed to parse literal '"not-a-number"' as an int value at element: $.primitive
            |JSON input: "not-a-number"
            """.trimMargin(),
            ex.message
        )
    }

    listOf(
        buildJsonObject { put("extra", 1) },
        buildJsonObject { put("extra-str", "one") },
        buildJsonObject { put("extra-bool", false) },
        buildJsonObject { put("extra-double", 0.0) },
        buildJsonObject { put("extra-null", JsonNull) },
        buildJsonObject { put("extra-obj", buildJsonObject { put("nested", 1) }) }
    ).forEach { json ->
        verify(
            "Person additional JsonObject - $json",
            PersonWithAdditionalProperties(
                "John",
                null,
                PersonWithAdditionalProperties.NestedClass("test1", 1),
                json
            ),
            buildJsonObject {
                put("name", "John")
                put("age", JsonNull)
                put("nested", nested(1))
                putAll(json)
            }
        )
    }

    verify(
        "Person without JsonObject",
        PersonWithAdditionalProperties(
            "John",
            null,
            PersonWithAdditionalProperties.NestedClass("test1", 1),
            null
        ),
        buildJsonObject {
            put("name", "John")
            put("age", JsonNull)
            put("nested", nested(1))
        }
    )

    fun asc(): OpenEnum = OpenEnum.AscOrDesc.Asc

    verify(
        "OpenEnum - Enum A",
        asc(),
        JsonPrimitive("asc")
    )

    fun desc(): OpenEnum = OpenEnum.AscOrDesc.Desc

    verify(
        "OpenEnum - AscOrDesc Desc",
        desc(),
        JsonPrimitive("desc")
    )

    fun open(): OpenEnum = OpenEnum.CaseString("open")

    verify(
        "OpenEnum - open",
        open(),
        JsonPrimitive("open")
    )

    fun caseElse(): Union =
        Union.CaseElse(
            name = "John",
            email = "john.doe@company.org"
        )

    verify(
        "Union - CaseElse",
        caseElse(),
        buildJsonObject {
            put("name", "John")
            put("email", "john.doe@company.org")
        }
    )

    fun person(): Union =
        Union.CasePerson(
            PersonWithAdditionalProperties(
                name = "John",
                age = 30,
                nested = PersonWithAdditionalProperties.NestedClass("test1", 1)
            )
        )

    verify(
        "Union - CasePerson",
        person(),
        buildJsonObject {
            put("name", "John")
            put("age", 30)
            put("nested", nested(1))
        }
    )

    fun empty(): Union = Union.Empty

    verify(
        "Union - Empty",
        empty(),
        buildJsonObject {}
    )

    fun a(): Union = Union.AOrB.A

    verify(
        "Union - AOrB A",
        a(),
        JsonPrimitive("a")
    )

    fun b(): Union = Union.AOrB.B

    verify(
        "Union - AOrB B",
        b(),
        JsonPrimitive("b")
    )

    fun int(): Union = CaseInt(1)

    verify(
        "Union - Int",
        int(),
        JsonPrimitive(1)
    )

    fun long(): Union = CaseLong(Int.MAX_VALUE.toLong() + 1)

    verify(
        "Union - Long",
        long(),
        JsonPrimitive(Int.MAX_VALUE.toLong() + 1)
    )

    // TODO: these are failing on JS... Check later, but should behave exactly like KotlinX Serialization
//    fun float(): Union = Union.CaseFloat(1.0f)
//
//    verify(
//        "Union - Float",
//        float(),
//        JsonPrimitive(1.0f)
//    )
//
//    fun double(): Union = Union.CaseDouble(Double.MAX_VALUE)
//
//    verify(
//        "Union - Double",
//        double(),
//        JsonPrimitive(Double.MAX_VALUE)
//    )

    fun boolean(): Union = CaseBoolean(true)

    verify(
        "Union - Boolean",
        boolean(),
        JsonPrimitive(true)
    )

    fun string(): Union = Union.CaseString("random")

    verify(
        "Union - String",
        string(),
        JsonPrimitive("random")
    )

    test("Union runtime ordering - overlapping object properties prefer most specific shape") {
        val payload = buildJsonObject {
            put("a", "x")
            put("b", "y")
            put("c", "z")
        }

        val parsed: Any = attemptDeserialize(
            payload,
            OverlappingAbc::class to { decodeFromJsonElement(OverlappingAbc.serializer(), it) },
            OverlappingAb::class to { decodeFromJsonElement(OverlappingAb.serializer(), it) },
        )

        assertEquals(OverlappingAbc("x", "y", "z"), parsed)
    }

    test("Union runtime ordering - List<Item> is tried before Item") {
        val payload = buildJsonObject { put("id", 1) }
        val listPayload = kotlinx.serialization.json.JsonArray(listOf(payload))

        val parsed: Any = attemptDeserialize(
            listPayload,
            List::class to { decodeFromJsonElement(ListSerializer(RuntimeItem.serializer()), it) },
            RuntimeItem::class to { decodeFromJsonElement(RuntimeItem.serializer(), it) },
        )

        assertEquals(listOf(RuntimeItem(1)), parsed)
    }

    test("Union runtime ordering - additionalProperties object is tried after strict object") {
        val payload = buildJsonObject {
            put("name", "John")
            put("age", 30)
        }

        val parsed: Any = attemptDeserialize(
            payload,
            StrictPerson::class to { decodeFromJsonElement(StrictPerson.serializer(), it) },
            JsonObject::class to { decodeFromJsonElement(JsonObject.serializer(), it) },
        )

        assertEquals(StrictPerson("John", 30), parsed)
    }

    test("Union runtime ordering - FreeFormJson is tried last") {
        val payload = JsonPrimitive("hello")

        val parsed: Any = attemptDeserialize(
            payload,
            String::class to { decodeFromJsonElement(String.serializer(), it) },
            JsonElement::class to { it },
        )

        assertTrue(parsed is String)
        assertEquals("hello", parsed)
    }

    test("Recursive union runtime roundtrip") {
        val expected: RecursiveUnion = RecursiveUnion.Branch(
            listOf(
                RecursiveUnion.Leaf("a"),
                RecursiveUnion.Branch(
                    listOf(
                        RecursiveUnion.Leaf("b")
                    )
                )
            )
        )

        val json = Json.encodeToJsonElement(RecursiveUnion.serializer(), expected)
        val actual = Json.decodeFromJsonElement<RecursiveUnion>(json)

        assertEquals(expected, actual)
    }
}

@TestRegistering
inline fun <reified A> TestSuite.verify(
    name: String,
    expected: A,
    json: JsonElement
) = test(name) { assertEquals(expected, Json.decodeFromJsonElement<A>(json)) }

@Serializable
data class OverlappingAb(val a: String, val b: String)

@Serializable
data class OverlappingAbc(val a: String, val b: String, val c: String)

@Serializable
data class RuntimeItem(val id: Int)

@Serializable
data class StrictPerson(val name: String, val age: Int)

@Serializable(with = RecursiveUnion.Serializer::class)
sealed interface RecursiveUnion {
    @Serializable
    data class Leaf(val leaf: String) : RecursiveUnion

    @Serializable
    data class Branch(val children: List<RecursiveUnion>) : RecursiveUnion

    companion object Serializer : KSerializer<RecursiveUnion> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.RecursiveUnion", PolymorphicKind.SEALED) {
                element("Leaf", Leaf.serializer().descriptor)
                element("Branch", Branch.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): RecursiveUnion {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            return attemptDeserialize(
                value,
                Leaf::class to { decodeFromJsonElement(Leaf.serializer(), it) },
                Branch::class to { decodeFromJsonElement(Branch.serializer(), it) },
            )
        }

        override fun serialize(encoder: Encoder, value: RecursiveUnion) = when (value) {
            is Leaf -> encoder.encodeSerializableValue(Leaf.serializer(), value)
            is Branch -> encoder.encodeSerializableValue(Branch.serializer(), value)
        }
    }
}

// TODO: this is needed for referenced union cases, and wrapped top-level collections & primitives
@Serializable(with = Wrapped.Serializer::class)
@JvmInline
value class Wrapped(val value: PersonWithAdditionalPropertiesSchema) {
    object Serializer : KSerializer<Wrapped> by ValueClassSerializer(
        Wrapped::value,
        ::Wrapped,
        PersonWithAdditionalPropertiesSchema.serializer()
    )
}

fun <Wrapped, Value> ValueClassSerializer(
    unwrap: (Wrapped) -> Value,
    wrap: (Value) -> Wrapped,
    valueSerializer: KSerializer<Value>
) = object : KSerializer<Wrapped> {
    override val descriptor: SerialDescriptor = valueSerializer.descriptor
    override fun deserialize(decoder: Decoder): Wrapped = wrap(decoder.decodeSerializableValue(valueSerializer))
    override fun serialize(encoder: Encoder, value: Wrapped) =
        encoder.encodeSerializableValue(valueSerializer, unwrap(value))
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = PersonWithAdditionalPropertiesSchema.Serializer::class)
@Suppress("RUNTIME_ANNOTATION_NOT_SUPPORTED")
@KeepGeneratedSerializer
data class PersonWithAdditionalPropertiesSchema(
    val name: String,
    val age: Int?,
    val additional: Map<String, NestedClass>? = null
) {
    @Serializable
    data class NestedClass(val config1: String, val config2: Int)

    companion object Serializer : KSerializer<PersonWithAdditionalPropertiesSchema> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: PersonWithAdditionalPropertiesSchema) {
            val json = (encoder as JsonEncoder).json // only needed for complex types
            encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("name", JsonPrimitive(value.name)) // required
                    if (value.age != null) put("age", JsonPrimitive(value.age)) // required nullable
                    value.additional?.forEach { (key, additionalValue) ->
                        put(key, json.encodeToJsonElement(NestedClass.serializer(), additionalValue))
                    }
                })
        }

        override fun deserialize(decoder: Decoder): PersonWithAdditionalPropertiesSchema {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("name", "age")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return PersonWithAdditionalPropertiesSchema(
                name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                age = json.decodeFromJsonElement(Int.serializer().nullable, element["age"]!!),
                additional = (element - names)
                    .mapValues { (_, value) -> decodeFromJsonElement(NestedClass.serializer(), value) }
                    .ifEmpty { null }
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Suppress("RUNTIME_ANNOTATION_NOT_SUPPORTED")
@KeepGeneratedSerializer
@Serializable(with = PersonWithAdditionalProperties.Serializer::class)
data class PersonWithAdditionalProperties(
    val name: String,
    val age: Int?,
    val nested: NestedClass,
    val additional: JsonObject? = null
) {

    @Serializable
    data class NestedClass(val config1: String, val config2: Int)

    companion object Serializer : KSerializer<PersonWithAdditionalProperties> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: PersonWithAdditionalProperties) {
            val json = (encoder as JsonEncoder).json
            encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("name", json.encodeToJsonElement(String.serializer(), value.name))
                    put("age", JsonPrimitive(value.age))
                    put("nested", json.encodeToJsonElement(NestedClass.serializer(), value.nested))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): PersonWithAdditionalProperties {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("name", "age", "nested")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return PersonWithAdditionalProperties(
                name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                age = json.decodeFromJsonElement(Int.serializer().nullable, element["age"]!!),
                nested = json.decodeFromJsonElement(NestedClass.serializer(), element["nested"]!!),
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}

@Serializable(with = OpenEnum.Serializer::class)
sealed interface OpenEnum {
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : OpenEnum

    @Serializable
    enum class AscOrDesc : OpenEnum {
        @SerialName("asc")
        Asc,

        @SerialName("desc")
        Desc;
    }

    companion object Serializer : KSerializer<OpenEnum> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(encoder: Encoder, value: OpenEnum) = when (value) {
            AscOrDesc.Asc -> encoder.encodeString("asc")
            AscOrDesc.Desc -> encoder.encodeString("desc")
            is CaseString -> encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): OpenEnum =
            when (val value = decoder.decodeString()) {
                "asc" -> AscOrDesc.Asc
                "desc" -> AscOrDesc.Desc
                else -> CaseString(value)
            }
    }
}

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    data object Empty : Union

    @Serializable
    data class CaseElse(val name: String, val email: String) : Union

    @Serializable
    @JvmInline
    value class CasePerson(val value: PersonWithAdditionalProperties) : Union

    @Serializable
    enum class AOrB : Union {
        @SerialName("a")
        A,

        @SerialName("b")
        B;
    }

    @Serializable
    @JvmInline
    value class CaseInt(val value: Int) : Union

    @Serializable
    @JvmInline
    value class CaseLong(val value: Long) : Union

    @Serializable
    @JvmInline
    value class CaseFloat(val value: Float) : Union

    @Serializable
    @JvmInline
    value class CaseDouble(val value: Double) : Union

    @Serializable
    @JvmInline
    value class CaseBoolean(val value: Boolean) : Union

    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    companion object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.Union2", PolymorphicKind.SEALED) {
                element("Empty", Empty.serializer().descriptor)
                element("CaseElse", CaseElse.serializer().descriptor)
                element("CasePerson", PersonWithAdditionalProperties.serializer().descriptor)
                element("AOrB", AOrB.serializer().descriptor)
                element("CaseInt", Int.serializer().descriptor)
                element("CaseLong", Long.serializer().descriptor)
                element("CaseFloat", Float.serializer().descriptor)
                element("CaseDouble", Double.serializer().descriptor)
                element("CaseBoolean", Boolean.serializer().descriptor)
                element("CaseString", String.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            // TODO: this has critical order.
            return attemptDeserialize(
                value,
                // Objects most properties first to prevent small objects being returned first in anyOf overlapping schemas
                CaseElse::class to { decodeFromJsonElement(CaseElse.serializer(), it) },
                Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                // Objects with additional propertis PSchema go second last
                // Objects with additional properties go last because they swallow everything
                CasePerson::class to {
                    CasePerson(
                        decodeFromJsonElement(
                            PersonWithAdditionalProperties.serializer(),
                            it
                        )
                    )
                },

                // Nested Union is extremely tricky... should be first?
                // DiscriminatedObject is not allowed

                // Enums should go before primitives or might get swallowed
                AOrB::class to { decodeFromJsonElement(AOrB.serializer(), it) },

                // List (JsArray) can be anywhere since it doesn't conflict

                // Primitives should come in this order
                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                CaseFloat::class to { CaseFloat(decodeFromJsonElement(Float.serializer(), it)) },
                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },

                // All other string types come before (uuid, date, date-time, binary) or get swallowed
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },

                // FreeFormJson should be dead last
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when (value) {
            is CasePerson -> encoder.encodeSerializableValue(PersonWithAdditionalProperties.serializer(), value.value)
            is CaseElse -> encoder.encodeSerializableValue(CaseElse.serializer(), value)
            is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
            is AOrB -> encoder.encodeSerializableValue(AOrB.serializer(), value)
            is CaseBoolean -> encoder.encodeBoolean(value.value)
            is CaseDouble -> encoder.encodeDouble(value.value)
            is CaseFloat -> encoder.encodeFloat(value.value)
            is CaseInt -> encoder.encodeInt(value.value)
            is CaseLong -> encoder.encodeLong(value.value)
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
    }
}

class UnionSerializationException(
    val payload: JsonElement,
    val errors: Map<KClass<*>, IllegalArgumentException>,
) : SerializationException(
    """
        Failed to deserialize Json: $payload.
        Errors:
        ${
        errors.entries.joinToString(separator = "\n") { (type, error) ->
            "$type - failed to deserialize: ${error.stackTraceToString()}"
        }
    }""".trimIndent()
)

fun <A> attemptDeserialize(json: JsonElement, vararg block: Pair<KClass<*>, (JsonElement) -> A>): A {
    val errors = linkedMapOf<KClass<*>, IllegalArgumentException>()
    block.forEach { (kclass, parse) ->
        try {
            return parse(json)
        } catch (e: IllegalArgumentException) {
            errors[kclass] = e
        }
    }
    throw UnionSerializationException(json, errors)
}

// Discriminated union with value class wrapping a referenced type
// This tests that @JvmInline value class correctly flattens the inner type for polymorphic serialization
@Serializable
data class Person(val id: Int, val name: String)

@OptIn(ExperimentalSerializationApi::class)
@kotlinx.serialization.json.JsonClassDiscriminator("type")
@Serializable
sealed interface DiscriminatedUnion {
    @SerialName("person")
    @Serializable
    @JvmInline
    value class CasePerson(val value: Person) : DiscriminatedUnion

    @SerialName("employee")
    @Serializable
    data class Employee(val age: Int, val department: String) : DiscriminatedUnion
}

val discriminatedUnionSpec by testSuite {
    fun casePerson(): DiscriminatedUnion =
        DiscriminatedUnion.CasePerson(Person(id = 1, name = "John"))

    // Discriminated union with value class - the Person fields should be flattened
    // JSON: {"type": "person", "id": 1, "name": "John"}
    verify(
        "DiscriminatedUnion - CasePerson",
        casePerson(),
        buildJsonObject {
            put("type", "person")
            put("id", 1)
            put("name", "John")
        }
    )

    fun employee(): DiscriminatedUnion =
        DiscriminatedUnion.Employee(age = 30, department = "Engineering")

    verify(
        "DiscriminatedUnion - Employee",
        employee(),
        buildJsonObject {
            put("type", "employee")
            put("age", 30)
            put("department", "Engineering")
        }
    )
}
