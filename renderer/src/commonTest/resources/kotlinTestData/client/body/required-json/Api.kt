package io.github.nomisrev.render.golden.client.body.required_json.api

import io.github.nomisrev.model.CreateChatCompletionRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Api {
    suspend fun createChatCompletion(
        model: String,
        limit: String,
        openAIOrganization: String,
        body: CreateChatCompletionRequest,
        after: String? = null,
        xTraceId: String? = null,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun createChatCompletion(model: String, limit: String, openAIOrganization: String, body: CreateChatCompletionRequest, after: String?, xTraceId: String?): String =
        client.post("/chat/completions/$model") {
            parameter("limit", limit)
            header("OpenAI-Organization", openAIOrganization)
            after?.let { parameter("after", it) }
            xTraceId?.let { header("X-Trace-Id", it) }
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

fun ApiClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): Api {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorApi(client)
}
