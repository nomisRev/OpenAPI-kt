package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.PersonWithAdditionalProperties.Serializer.generatedSerializer
import io.github.nomisrev.Union.CaseBoolean
import io.github.nomisrev.Union.CaseInt
import io.github.nomisrev.Union.CaseLong
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
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
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: String): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Int): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Boolean): JsonElement? = put(key, JsonPrimitive(element))

@IgnorableReturnValue
fun JsonObjectBuilder.put(key: String, element: Double): JsonElement? = put(key, JsonPrimitive(element))

fun JsonObjectBuilder.putAll(jsonObject: JsonObject?) = jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }

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

    verify(
        "OpenEnum - Enum A",
        OpenEnum.AscOrDesc.Asc,
        JsonPrimitive("asc")
    )

    verify(
        "OpenEnum - AscOrDesc Desc",
        OpenEnum.AscOrDesc.Desc,
        JsonPrimitive("desc")
    )

    verify(
        "OpenEnum - open",
        OpenEnum.CaseString("open"),
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
}

@TestRegistering
inline fun <reified A> TestSuite.verify(
    name: String,
    expected: A,
    json: JsonElement
) = test(name) { assertEquals(expected, Json.decodeFromJsonElement<A>(json)) }

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = PersonWithAdditionalPropertiesSchema.Serializer::class)
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
                    value.additional?.forEach { (key, value) ->
                        put(key, json.encodeToJsonElement(NestedClass.serializer(), value))
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

// We use this instead of `intOrNull` otherwise null swallows type conversion exceptions
// Alternatively consider using Int.serializer().nullabble,
// but then we need to jump through Json.decodeFromJsonElement... for JsonPrimitive...
private fun JsonElement.orNull(): JsonElement? = when (this) {
    JsonNull -> null
    else -> this
}

@Serializable
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

// TODO: Should we generate 'serializer<Value>()' instead of relying on the reflection lookup?
inline fun <reified Value, reified Union> Json.parseSerializable(
    noinline createUnion: (Value) -> Union
): Pair<KClass<*>, (JsonElement) -> Union> =
    Pair(Union::class) { json: JsonElement ->
        createUnion(decodeFromJsonElement(serializer<Value>(), json))
    }

inline fun <reified Union> Json.parseUnionCase(): Pair<KClass<*>, (JsonElement) -> Union> =
    Pair(Union::class) { json: JsonElement -> decodeFromJsonElement(serializer<Union>(), json) }

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
                element("CasePerson", PersonWithAdditionalProperties.serializer().descriptor)
                element("CaseElse", CaseElse.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Currently only supporting Json" }.json

            // TODO: this has critical order.
            return attemptDeserialize(
                value,
                json.parseUnionCase<Empty>(),
                json.parseUnionCase<CaseElse>(),
                // Only 1 object in Union can have additional properties, or it swallows all objects 'below'
                // Doesn't really make sense either but we need to order the cases anyway
                json.parseSerializable<PersonWithAdditionalProperties, Union>(::CasePerson),
                json.parseUnionCase<AOrB>(),
                json.parseSerializable(::CaseInt),
                json.parseSerializable(::CaseLong),
                json.parseSerializable(::CaseFloat),
                json.parseSerializable(::CaseDouble),
                json.parseSerializable(::CaseBoolean),
                json.parseSerializable(::CaseString)
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
            is CaseString -> encoder.encodeString(value.value)
        }
    }
}

public class UnionSerializationException(
    public val payload: JsonElement,
    public val errors: Map<KClass<*>, IllegalArgumentException>,
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

public fun <A> attemptDeserialize(
    json: JsonElement,
    vararg block: Pair<KClass<*>, (JsonElement) -> A>
): A {
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

// TODO: Write a function that automatically generates poly-n version of this function.
//   Use this inside of serializers to avoid complex code in serializers, and splitting complexity.
inline fun <A> Decoder.require(
    name: String,
    block: Json.(a: JsonElement, additional: JsonObject?) -> A
): A {
    require(this is JsonDecoder) { "Additional properties are only supported for Json" }
    val element = decodeSerializableValue(JsonObject.serializer())
    require(element.containsKey(name)) { "Missing required property '$name'" }
    return json.block(element[name]!!, JsonObject(element - name))
}

inline fun <A> Decoder.require(
    name: String,
    name2: String,
    block: Json.(a: JsonElement, b: JsonElement, additional: JsonObject?) -> A
): A {
    require(this is JsonDecoder) { "Additional properties are only supported for Json" }
    val element = decodeSerializableValue(JsonObject.serializer())
    val names = setOf(name, name2)
    require(element.keys.containsAll(names)) { "Missing required properties '$name' and '$name2'" }
    return json.block(element[name]!!, element[name2]!!, JsonObject(element - names))
}

inline fun <A> Decoder.require(
    name: String,
    name2: String,
    name3: String,
    block: Json.(a: JsonElement, b: JsonElement, c: JsonElement, additional: JsonObject?) -> A
): A {
    require(this is JsonDecoder) { "Additional properties are only supported for Json" }
    val element = decodeSerializableValue(JsonObject.serializer())
    val names = setOf(name, name2, name3)
    require(element.keys.containsAll(names)) { "Missing required properties '$name', '$name2' and '$name3'" }
    return json.block(
        element[name]!!,
        element[name2]!!,
        element[name3]!!,
        JsonObject(element - names).ifEmpty { null }
    )
}