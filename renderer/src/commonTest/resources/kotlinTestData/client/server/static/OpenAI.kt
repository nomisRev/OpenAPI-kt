package io.github.nomisrev.render.golden.client.server.static.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface OpenAI {
    suspend fun listModels(): String
}

sealed interface OpenAIServer {
    val url: String

    data object Production : OpenAIServer {
        override val url = "https://api.openai.com/v1"
    }


    data object Staging : OpenAIServer {
        override val url = "https://staging.api.openai.com/v1"
    }


    data class Custom(override val url: String) : OpenAIServer
}


internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
    override suspend fun listModels(): String =
        client.get("/models").body()
}

fun OpenAIClient(
    server: OpenAIServer = OpenAIServer.Production,
    block: HttpClientConfig<*>.() -> Unit = {},
): OpenAI {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(server.url) }
        block()
    }
    return KtorOpenAI(client)
}
