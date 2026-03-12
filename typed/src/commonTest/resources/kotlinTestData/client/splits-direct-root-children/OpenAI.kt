package client.splits.direct.root.children.api

import client.splits.direct.root.children.api.Chat
import client.splits.direct.root.children.api.KtorChat
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest

interface OpenAI {
    val chat: Chat

    val models: Models
}

internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
    override val chat: Chat = KtorChat(client)
    override val models: Models = KtorModels(client)
}

fun OpenAIClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): OpenAI {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorOpenAI(client)
}
