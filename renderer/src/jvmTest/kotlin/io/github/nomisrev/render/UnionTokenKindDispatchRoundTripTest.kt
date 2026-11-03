package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.union.token.kind.dispatch.Api
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.Cat
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.FreeFormOrCat
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.IntegerOrCat
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.IntegerOrString
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.MapOrList
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.NumberOrInteger
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.PlainString
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.PlainStringOrDate
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.PlainStringOrInteger
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrBoolean
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrDateTime
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrEnumOrInteger
import io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrInteger
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

/**
 * A non-discriminated union decodes a value as the case whose JSON type matches the value's token, and the result is
 * the same under a strict `Json {}`, Ktor's lenient `DefaultJson` (what `install(ContentNegotiation) { json() }` uses)
 * and a lenient, coercing `Json`. Encoding writes the case's own value back. Uses the generated goldens under
 * `kotlinTestData/client/union-token-kind-dispatch` directly.
 */
class UnionTokenKindDispatchRoundTripTest {
    private val configs = mapOf(
        "Json {}" to Json {},
        "DefaultJson" to DefaultJson,
        "Json { isLenient; coerceInputValues }" to Json {
            isLenient = true
            coerceInputValues = true
        },
    )

    /** Collects every mismatch, input by configuration, so a failure reports all of them at once. */
    private class Checks(val configs: Map<String, Json>) {
        val mismatches = mutableListOf<String>()

        private val KSerializer<*>.name get() = descriptor.serialName.substringAfterLast('.')

        fun <A> decodes(serializer: KSerializer<A>, input: String, expected: A) =
            configs.forEach { [name, json] ->
                val decoded = runCatching { json.decodeFromString(serializer, input) }
                if (decoded != Result.success(expected)) mismatches += "${serializer.name} $input under $name: expected $expected, got $decoded"
                val encoded = json.encodeToJsonElement(serializer, expected)
                if (encoded != json.parseToJsonElement(input)) mismatches += "${serializer.name} $expected under $name encoded as $encoded"
            }

        fun <A> rejects(serializer: KSerializer<A>, input: String) =
            configs.forEach { [name, json] ->
                val decoded = runCatching { json.decodeFromString(serializer, input) }
                if (decoded.exceptionOrNull() !is SerializationException) {
                    mismatches += "${serializer.name} $input under $name: expected a SerializationException, got $decoded"
                }
            }
    }

    private fun check(block: Checks.() -> Unit) {
        val mismatches = Checks(configs).apply(block).mismatches
        assertTrue(mismatches.isEmpty(), mismatches.joinToString("\n"))
    }

    @Test
    fun aNumberIsNeverAString() {
        check {
            decodes(StringOrInteger.serializer(), "1", StringOrInteger.CaseLong(1))
            decodes(IntegerOrString.serializer(), "1", IntegerOrString.CaseLong(1))
            decodes(PlainStringOrInteger.serializer(), "1", PlainStringOrInteger.CaseLong(1))
            decodes(StringOrEnumOrInteger.serializer(), "1", StringOrEnumOrInteger.CaseLong(1))
            rejects(StringOrDateTime.serializer(), "1")
            rejects(PlainStringOrDate.serializer(), "1")
        }
    }

    @Test
    fun aStringIsNeverANumber() {
        check {
            decodes(StringOrInteger.serializer(), "\"1\"", StringOrInteger.CaseString("1"))
            decodes(IntegerOrString.serializer(), "\"1\"", IntegerOrString.CaseString("1"))
            decodes(PlainStringOrInteger.serializer(), "\"1\"", PlainStringOrInteger.CasePlainString(PlainString("1")))
            decodes(StringOrEnumOrInteger.serializer(), "\"1\"", StringOrEnumOrInteger.CaseString("1"))
            rejects(NumberOrInteger.serializer(), "\"1\"")
            rejects(IntegerOrCat.serializer(), "\"1\"")
        }
    }

    @Test
    fun aBooleanIsNeverAStringAndAStringIsNeverABoolean() {
        check {
            decodes(StringOrBoolean.serializer(), "true", StringOrBoolean.CaseBoolean(true))
            decodes(StringOrBoolean.serializer(), "\"true\"", StringOrBoolean.CaseString("true"))
            decodes(StringOrBoolean.serializer(), "\"x\"", StringOrBoolean.CaseString("x"))
        }
    }

    @Test
    fun anIntegerIsTriedBeforeANumber() {
        check {
            decodes(NumberOrInteger.serializer(), "1", NumberOrInteger.CaseLong(1))
            decodes(NumberOrInteger.serializer(), "1.5", NumberOrInteger.CaseDouble(1.5))
        }
    }

    @Test
    fun anEnumAndAFormatAreTriedBeforeAPlainString() {
        check {
            decodes(StringOrEnumOrInteger.serializer(), "\"a\"", StringOrEnumOrInteger.AOrB.A)
            decodes(StringOrEnumOrInteger.serializer(), "\"z\"", StringOrEnumOrInteger.CaseString("z"))
            decodes(
                StringOrDateTime.serializer(),
                "\"2024-01-01T00:00:00Z\"",
                StringOrDateTime.CaseDateTime(Instant.parse("2024-01-01T00:00:00Z"))
            )
            decodes(StringOrDateTime.serializer(), "\"x\"", StringOrDateTime.CaseString("x"))
            decodes(PlainStringOrDate.serializer(), "\"2024-01-01\"", PlainStringOrDate.CaseDate(LocalDate(2024, 1, 1)))
            decodes(PlainStringOrDate.serializer(), "\"x\"", PlainStringOrDate.CasePlainString(PlainString("x")))
        }
    }

    @Test
    fun objectsArraysAndFreeFormJsonDecodeByShape() {
        check {
            decodes(FreeFormOrCat.serializer(), """{"meow":"m"}""", FreeFormOrCat.CaseCat(Cat("m")))
            decodes(
                FreeFormOrCat.serializer(),
                """{"bark":"b"}""",
                FreeFormOrCat.CaseJsonElement(buildJsonObject { put("bark", "b") })
            )
            decodes(FreeFormOrCat.serializer(), "1", FreeFormOrCat.CaseJsonElement(JsonPrimitive(1)))
            decodes(FreeFormOrCat.serializer(), "\"x\"", FreeFormOrCat.CaseJsonElement(JsonPrimitive("x")))
            decodes(FreeFormOrCat.serializer(), "[1]", FreeFormOrCat.CaseJsonElement(buildJsonArray { add(JsonPrimitive(1)) }))
            decodes(MapOrList.serializer(), """{"a":"b"}""", MapOrList.CaseStringsMap(mapOf("a" to "b")))
            decodes(MapOrList.serializer(), """["a"]""", MapOrList.CaseStrings(listOf("a")))
            decodes(IntegerOrCat.serializer(), """{"meow":"m"}""", IntegerOrCat.CaseCat(Cat("m")))
            decodes(IntegerOrCat.serializer(), "1", IntegerOrCat.CaseLong(1))
        }
    }

    private fun api(responseBody: String) = Api(HttpClient(MockEngine {
        respond(responseBody, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
    }) {
        install(ContentNegotiation) { json() }
    })

    @Test
    fun aClientWithKtorsDefaultJsonReceivesTheCaseOfTheResponseToken() = runBlocking {
        assertEquals(StringOrInteger.CaseLong(1), api("1").value.get())
        assertEquals(StringOrInteger.CaseString("1"), api("\"1\"").value.get())
    }
}
