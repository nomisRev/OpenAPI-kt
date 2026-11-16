package io.youtrack

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.youtrack.api.YouTrackRESTAPI
import io.youtrack.model.BaseArticleRead
import io.youtrack.model.BaseArticleWrite
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * An operation typed as a case of a discriminated base (`GET /articles/{id}` returns `BaseArticleRead.Article`)
 * serializes the body with the case's own serializer, so that serializer must read and write YouTrack's `$type`. With
 * the client's default `Json`, a tag it does not know is an unknown key.
 */
class DiscriminatedCaseBodyTest {
    private fun api(response: String, sent: MutableList<String> = mutableListOf()) = YouTrackRESTAPI(
        HttpClient(MockEngine { request ->
            (request.body as? TextContent)?.let { sent += it.text }
            respond(response, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            defaultRequest { url("http://localhost/api/") }
            install(ContentNegotiation) { json() }
        }
    )

    @Test
    fun `an operation returning a case decodes YouTrack's tag`() = runBlocking {
        val article = api("""{"${'$'}type":"Article","id":"1-1","summary":"Hello"}""").articles.id("1-1").get()
        assertEquals(BaseArticleRead.Article(id = "1-1", summary = "Hello"), article)
    }

    @Test
    fun `a request body of a case is sent with its tag`() = runBlocking {
        val sent = mutableListOf<String>()
        api("""{"${'$'}type":"Article","id":"1-2"}""", sent).articles.post(body = BaseArticleWrite.Article(summary = "New"))
        assertEquals(listOf("""{"${'$'}type":"Article","summary":"New"}"""), sent)
    }
}
