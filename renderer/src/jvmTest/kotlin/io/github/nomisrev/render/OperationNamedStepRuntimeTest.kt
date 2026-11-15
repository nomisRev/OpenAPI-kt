package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.operation.named.step.Api
import io.github.nomisrev.render.test.client.operation.named.step.Standards
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `operation-named-step` golden: a path step with the name of an operation of its parent and
 * that operation are one member, called for the operation and navigated for the step.
 */
class OperationNamedStepRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()
    private var reply: Pair<HttpStatusCode, String> = HttpStatusCode.NoContent to ""

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond(reply.second, reply.first, headersOf(HttpHeaders.ContentType, "application/json"))
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private fun assertRequest(method: HttpMethod, path: String) {
        assertEquals(method, requests.last().method)
        assertEquals(path, requests.last().url.encodedPath)
    }

    @Test
    fun theMemberIsCalledForTheOperationAndNavigatedForTheStep() = runBlocking {
        reply = HttpStatusCode.OK to """{"count":2}"""
        val listed: Standards.Get.Response = api.standards.get(limit = 5)
        assertRequest(HttpMethod.Get, "/standards")
        assertEquals("limit=5", requests.last().url.encodedQuery)
        assertEquals(Standards.Get.Response(count = 2), listed)

        reply = HttpStatusCode.OK to """{"names":["a"]}"""
        val fetched: Standards.Get.Post.Response = api.standards.get.post(ids = listOf("1"))
        assertRequest(HttpMethod.Post, "/standards/get")
        assertEquals("""{"ids":["1"]}""", (requests.last().body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
        assertEquals(Standards.Get.Post.Response(names = listOf("a")), fetched)
    }

    @Test
    fun otherOperationsOfTheParentAndOtherStepsAreAsAlways() = runBlocking {
        api.members.get()
        assertRequest(HttpMethod.Get, "/members")
        api.members.post()
        assertRequest(HttpMethod.Post, "/members")
        api.members.get.post()
        assertRequest(HttpMethod.Post, "/members/get")
        // `/members` has no DELETE, so `delete` is only the step.
        api.members.delete.post()
        assertRequest(HttpMethod.Post, "/members/delete")
    }

    @Test
    fun aParameterBelowTheStep() = runBlocking {
        api.tokens.get()
        assertRequest(HttpMethod.Get, "/tokens")
        api.tokens.get.uuid("u1").get()
        assertRequest(HttpMethod.Get, "/tokens/get/u1")
    }

    @Test
    fun theStepHasAnOperationOfTheSameNameItself() = runBlocking {
        val member = api.detector.detectorId("d1").member
        member.get()
        assertRequest(HttpMethod.Get, "/detector/d1/member")
        member.get.get()
        assertRequest(HttpMethod.Get, "/detector/d1/member/get")
        member.get.post()
        assertRequest(HttpMethod.Post, "/detector/d1/member/get")
    }

    @Test
    fun anOperationWithAMethodPerMediaTypeKeepsThoseNextToTheStep() = runBlocking {
        reply = HttpStatusCode.OK to "\"text\""
        assertEquals("text", api.docs.get.json())
        assertRequest(HttpMethod.Get, "/docs")

        reply = HttpStatusCode.NoContent to ""
        api.docs.get.delete()
        assertRequest(HttpMethod.Delete, "/docs/get")
    }
}
