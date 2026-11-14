package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.optional.nullable.wire.Api
import io.github.nomisrev.render.test.client.optional.nullable.wire.Filter
import io.github.nomisrev.render.test.client.optional.nullable.wire.Items
import io.github.nomisrev.render.test.client.optional.nullable.wire.Optional
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `optional-nullable-wire` golden. In a JSON body (the flattened `invoke` parameters) an
 * optional and nullable property sends nothing when `Absent`, `null` when `Null` and its value when `Value`,
 * whatever the `Json` of the client. A urlencoded form, a query, a header and a cookie have no null: `Absent` and
 * `Null` are both left out.
 */
class OptionalNullableWireRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private fun api(json: Json): Api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json(json) }
        }
    )

    private val configs = listOf(
        "Ktor default" to DefaultJson,
        "Json {}" to Json {},
        "Json { explicitNulls = false }" to Json { explicitNulls = false },
        "Json { encodeDefaults = true; explicitNulls = false }" to Json { encodeDefaults = true; explicitNulls = false },
    )

    private val request: HttpRequestData
        get() = requests.last()

    private fun body(): String = when (val body = request.body) {
        is TextContent -> body.text
        is OutgoingContent.ByteArrayContent -> body.bytes().decodeToString()
        else -> error("Unexpected body $body")
    }

    @Test
    fun anUnsetOptionalNullableBodyPropertySendsNoKey() = runBlocking {
        configs.forEach { [name, json] ->
            api(json).items.id("1").patch()
            assertEquals("{}", body(), name)
        }
    }

    @Test
    fun aNullOptionalNullableBodyPropertySendsNull() = runBlocking {
        configs.forEach { [name, json] ->
            api(json).items.id("1").patch(description = Optional.Null)
            assertEquals("""{"description":null}""", body(), name)
        }
    }

    @Test
    fun aValueOptionalNullableBodyPropertySendsTheValue() = runBlocking {
        configs.forEach { [name, json] ->
            api(json).items.id("1").patch(description = Optional.Value("d"))
            assertEquals("""{"description":"d"}""", body(), name)
        }
    }

    @Test
    fun thePropertiesOfOneBodyAreIndependent() = runBlocking {
        configs.forEach { [name, json] ->
            api(json).items.id("1").patch(name = Optional.Value("n"), description = Optional.Null, count = 3)
            assertEquals("""{"name":"n","description":null,"count":3}""", body(), name)
        }
    }

    @Test
    fun anOptionalNullablePropertyOfANestedInlineObjectFollowsTheSameContract() = runBlocking {
        configs.forEach { [name, json] ->
            api(json).items.id("1").patch(settings = Items.IdPath.Patch.Settings())
            assertEquals("""{"settings":{}}""", body(), name)
            api(json).items.id("1").patch(settings = Items.IdPath.Patch.Settings(color = Optional.Null))
            assertEquals("""{"settings":{"color":null}}""", body(), name)
        }
    }

    @Test
    fun aFormLeavesOutAbsentAndNullAndSendsTheValue() = runBlocking {
        val api = api(DefaultJson)
        api.forms.post()
        assertEquals("", body())
        api.forms.post(nickname = "nick", filter = Filter())
        assertEquals("nickname=nick", body())
        api.forms.post(filter = Filter(q = Optional.Null, limit = 5))
        assertEquals("filter%5Blimit%5D=5", body())
        api.forms.post(filter = Filter(q = Optional.Value("a b")))
        assertEquals("filter%5Bq%5D=a+b", body())
    }

    @Test
    fun anObjectQueryParameterLeavesOutAbsentAndNull() = runBlocking {
        val api = api(DefaultJson)
        api.search.get(filter = Filter())
        assertEquals("", request.url.encodedQuery)
        api.search.get(filter = Filter(q = Optional.Null, limit = 5))
        assertEquals("limit=5", request.url.encodedQuery)
        api.search.get(filter = Filter(q = Optional.Value("a b")))
        assertEquals("q=a%20b", request.url.encodedQuery)
    }

    @Test
    fun aDeepObjectQueryParameterLeavesOutAbsentAndNull() = runBlocking {
        val api = api(DefaultJson)
        api.search.get(deep = Filter(q = Optional.Null, limit = 5))
        assertEquals("deep%5Blimit%5D=5", request.url.encodedQuery)
        api.search.get(deep = Filter(q = Optional.Value("x")))
        assertEquals("deep%5Bq%5D=x", request.url.encodedQuery)
    }

    @Test
    fun anObjectHeaderLeavesOutAbsentAndNull() = runBlocking {
        val api = api(DefaultJson)
        api.search.get(xFilter = Filter(q = Optional.Value("x"), limit = 5))
        assertEquals("q,x,limit,5", request.headers["X-Filter"])
        api.search.get(xFilter = Filter(q = Optional.Null, limit = 5))
        assertEquals("limit,5", request.headers["X-Filter"])
    }
}
