package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.union.nullable.member.AnyOfRefMember
import io.github.nomisrev.render.test.client.union.nullable.member.Api
import io.github.nomisrev.render.test.client.union.nullable.member.InferredPet
import io.github.nomisrev.render.test.client.union.nullable.member.InlineNullableMember
import io.github.nomisrev.render.test.client.union.nullable.member.NullableCat
import io.github.nomisrev.render.test.client.union.nullable.member.NullableString
import io.github.nomisrev.render.test.client.union.nullable.member.ObjectMember
import io.github.nomisrev.render.test.client.union.nullable.member.RefMember
import io.github.nomisrev.render.test.client.union.nullable.member.TwoNullableMembers
import io.github.nomisrev.render.test.client.union.nullable.member.TypeArrayMember
import io.github.nomisrev.render.test.client.union.nullable.member.ValueHolder
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A non-discriminated `oneOf`/`anyOf` with a member that permits `null` decodes JSON `null` as Kotlin `null` (never as
 * a case holding `null`), and `"x"`, `1` or a tagged object as the member's non-null case, wherever it is used.
 * Uses the generated goldens under `kotlinTestData/client/union-nullable-member` directly.
 */
class UnionNullableMemberRoundTripTest {
    private val json = Json

    private val values = """
        {
          "ref": "x",
          "refs": ["x", 1],
          "refMap": { "a": 1 },
          "anyOf": 1,
          "anyOfs": ["x"],
          "anyOfMap": { "a": "x" },
          "inline": "x",
          "inlines": [1],
          "inlineMap": { "a": "x" },
          "typeArray": 1,
          "typeArrays": ["x"],
          "typeArrayMap": { "a": 1 },
          "twoNullable": "x",
          "twoNullables": [1],
          "object": { "type": "cat", "meow": "m" },
          "objects": ["x"],
          "objectMap": { "a": { "type": "cat", "meow": "n" } },
          "pet": { "type": "cat", "meow": "m" },
          "pets": [{ "type": "dog", "bark": "b" }],
          "petMap": { "a": { "type": "cat", "meow": "n" } }
        }
    """.trimIndent()

    private val nulls = """
        {
          "ref": null,
          "refs": [null],
          "refMap": { "a": null },
          "anyOf": null,
          "anyOfs": [null],
          "anyOfMap": { "a": null },
          "inline": null,
          "inlines": [null],
          "inlineMap": { "a": null },
          "typeArray": null,
          "typeArrays": [null],
          "typeArrayMap": { "a": null },
          "twoNullable": null,
          "twoNullables": [null],
          "object": null,
          "objects": [null],
          "objectMap": { "a": null },
          "pet": null,
          "pets": [null],
          "petMap": { "a": null }
        }
    """.trimIndent()

    @Test
    fun everyPositionDecodesAndEncodesAValue() {
        val decoded = json.decodeFromString<ValueHolder>(values)
        assertEquals(
            ValueHolder(
                ref = RefMember.CaseNullableString(NullableString("x")),
                refs = listOf(RefMember.CaseNullableString(NullableString("x")), RefMember.CaseLong(1)),
                refMap = mapOf("a" to RefMember.CaseLong(1)),
                anyOf = AnyOfRefMember.CaseLong(1),
                anyOfs = listOf(AnyOfRefMember.CaseNullableString(NullableString("x"))),
                anyOfMap = mapOf("a" to AnyOfRefMember.CaseNullableString(NullableString("x"))),
                inline = InlineNullableMember.CaseString("x"),
                inlines = listOf(InlineNullableMember.CaseLong(1)),
                inlineMap = mapOf("a" to InlineNullableMember.CaseString("x")),
                typeArray = TypeArrayMember.CaseLong(1),
                typeArrays = listOf(TypeArrayMember.CaseString("x")),
                typeArrayMap = mapOf("a" to TypeArrayMember.CaseLong(1)),
                twoNullable = TwoNullableMembers.CaseNullableString(NullableString("x")),
                twoNullables = listOf(TwoNullableMembers.CaseLong(1)),
                `object` = ObjectMember.CaseNullableCat(NullableCat(NullableCat.Type.Cat, "m")),
                objects = listOf(ObjectMember.CaseString("x")),
                objectMap = mapOf("a" to ObjectMember.CaseNullableCat(NullableCat(NullableCat.Type.Cat, "n"))),
                pet = InferredPet.Cat("m"),
                pets = listOf(InferredPet.Dog("b")),
                petMap = mapOf("a" to InferredPet.Cat("n")),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(values), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun everyPositionDecodesAndEncodesNull() {
        val decoded = json.decodeFromString<ValueHolder>(nulls)
        assertEquals(
            ValueHolder(
                ref = null,
                refs = listOf(null),
                refMap = mapOf("a" to null),
                anyOf = null,
                anyOfs = listOf(null),
                anyOfMap = mapOf("a" to null),
                inline = null,
                inlines = listOf(null),
                inlineMap = mapOf("a" to null),
                typeArray = null,
                typeArrays = listOf(null),
                typeArrayMap = mapOf("a" to null),
                twoNullable = null,
                twoNullables = listOf(null),
                `object` = null,
                objects = listOf(null),
                objectMap = mapOf("a" to null),
                pet = null,
                pets = listOf(null),
                petMap = mapOf("a" to null),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(nulls), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun topLevelNullDecodesAsNull() {
        assertEquals(null, json.decodeFromString<RefMember?>("null"))
        assertEquals(null, json.decodeFromString<InlineNullableMember?>("null"))
        assertEquals(null, json.decodeFromString<InferredPet?>("null"))
    }

    @Test
    fun anInferredDiscriminatorEncodesItsTagOnce() {
        assertEquals("""{"type":"cat","meow":"m"}""", json.encodeToString<InferredPet>(InferredPet.Cat("m")))
    }

    private class Fixture(responseBody: String) {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            respond(responseBody, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
    }

    @Test
    fun bodiesSendAndReceiveAValue() = runBlocking {
        val value = Fixture("\"received\"")
        assertEquals(
            AnyOfRefMember.CaseNullableString(NullableString("received")),
            value.api.value.put(RefMember.CaseNullableString(NullableString("x")))
        )
        assertEquals("\"x\"", value.sent.single())

        // Ktor's default `json()` is lenient, yet `1` is the integer case, not the string case.
        assertEquals(AnyOfRefMember.CaseLong(1), Fixture("1").api.value.put(RefMember.CaseLong(1)))

        val pet = Fixture("""{"type":"cat","meow":"received"}""")
        assertEquals(InferredPet.Cat("received"), pet.api.pet.put(InferredPet.Dog("sent")))
        assertEquals("""{"type":"dog","bark":"sent"}""", pet.sent.single())
    }

    @Test
    fun bodiesSendAndReceiveNull() = runBlocking {
        val value = Fixture("null")
        assertEquals(null, value.api.value.put(null))
        assertEquals("null", value.sent.single())

        val pet = Fixture("null")
        assertEquals(null, pet.api.pet.put(null))
        assertEquals("null", pet.sent.single())
    }
}
