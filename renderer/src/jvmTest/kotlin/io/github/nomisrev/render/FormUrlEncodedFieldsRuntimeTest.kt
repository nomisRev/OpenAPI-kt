package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.form.urlencoded.fields.Api
import io.github.nomisrev.render.test.client.form.urlencoded.fields.Code
import io.github.nomisrev.render.test.client.form.urlencoded.fields.Filters
import io.github.nomisrev.render.test.client.form.urlencoded.fields.Profiles
import io.github.nomisrev.render.test.client.form.urlencoded.fields.Status
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.parseUrlEncodedParameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Runtime check for the `form-urlencoded-fields` golden: every form field is serialized like a query parameter of its
 * Encoding Object `style` and `explode` (OpenAPI 3.1.1, 4.8.15, Appendix C), then form-urlencoded. A `null` field and
 * an empty array are left out; a scalar is its string form and an enum its wire value; an array is repeated keys
 * (`form`, `explode: true`, the default), or its items joined by `,` (`explode: false`), a space (`spaceDelimited`)
 * or `|` (`pipeDelimited`), the delimiter percent-encoded with the value; an object is one key per property (`form`,
 * `explode: true`) or one `name[key]` per property (`deepObject`).
 */
@OptIn(ExperimentalUuidApi::class)
class FormUrlEncodedFieldsRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private fun body(): String =
        requireNotNull((requests.last().body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())

    private fun decoded(): Map<String, List<String>> =
        body().parseUrlEncodedParameters(plusIsSpace = true).entries().associate { it.key to it.value }

    @Test
    fun everyFieldKindIsSerializedByItsStyle() = runBlocking {
        api.profiles.post(
            name = "Ada Lovelace",
            nickname = "ada",
            count = 3,
            age = 36,
            ratio = 1.5,
            active = true,
            role = Profiles.Post.Body.Role.ReadOnly,
            status = Status.CLOSED,
            born = LocalDate(1815, 12, 10),
            seen = Instant.parse("2024-01-31T10:00:00Z"),
            id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
            code = Code("X-1"),
            tags = listOf("a", "b"),
            roles = listOf(Profiles.Post.Body.Roles.Admin, Profiles.Post.Body.Roles.ReadOnly),
            ids = listOf(1, 2),
            words = listOf("hello", "world"),
            path = listOf("a", "b"),
            flags = listOf(true, false),
        )

        assertEquals("application/x-www-form-urlencoded", requests.last().body.contentType?.toString())
        assertEquals(
            "name=Ada+Lovelace&nickname=ada&count=3&age=36&ratio=1.5&active=true&role=read-only&status=CLOSED" +
                "&born=1815-12-10&seen=2024-01-31T10%3A00%3A00Z&id=123e4567-e89b-12d3-a456-426614174000&code=X-1" +
                "&tags=a&tags=b&roles=admin&roles=read-only&ids=1%2C2&words=hello+world&path=a%7Cb" +
                "&flags=true&flags=false",
            body(),
        )
        assertEquals(listOf("1,2"), decoded()["ids"])
        assertEquals(listOf("hello world"), decoded()["words"])
        assertEquals(listOf("a|b"), decoded()["path"])
    }

    @Test
    fun absentOptionalFieldsAreLeftOut() = runBlocking {
        api.profiles.post(
            name = "Ada",
            count = 0,
            ratio = 2.0,
            role = Profiles.Post.Body.Role.Admin,
            roles = listOf(Profiles.Post.Body.Roles.Admin),
        )

        assertEquals("name=Ada&count=0&ratio=2.0&role=admin&roles=admin", body())
    }

    @Test
    fun emptyArraysAreLeftOut() = runBlocking {
        api.profiles.post(
            name = "Ada",
            count = 0,
            ratio = 2.0,
            role = Profiles.Post.Body.Role.Admin,
            tags = emptyList(),
            roles = emptyList(),
            ids = emptyList(),
            words = emptyList(),
            path = emptyList(),
            flags = emptyList(),
        )

        assertEquals("name=Ada&count=0&ratio=2.0&role=admin", body())
    }

    @Test
    fun objectsAreTheirPropertiesAsKeysOrDeepObjectKeys() = runBlocking {
        api.filters.post(
            filter = Filters.Post.Body.Filter(status = Status.OPEN, limit = 10),
            range = Filters.Post.Body.Range(min = 1, max = 5),
        )

        assertEquals("status=OPEN&limit=10&range%5Bmin%5D=1&range%5Bmax%5D=5", body())
        assertEquals(listOf("1"), decoded()["range[min]"])
    }

    @Test
    fun absentObjectsAndPropertiesAreLeftOut() = runBlocking {
        api.filters.post(filter = Filters.Post.Body.Filter(status = Status.OPEN))
        assertEquals("status=OPEN", body())

        api.filters.post(filter = Filters.Post.Body.Filter(status = Status.OPEN), range = Filters.Post.Body.Range(min = 1))
        assertEquals("status=OPEN&range%5Bmin%5D=1", body())
    }
}
