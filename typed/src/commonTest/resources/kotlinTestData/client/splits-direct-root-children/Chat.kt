package client.splits.direct.root.children.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

interface Chat {
    val completions: Completions

    interface Completions {
        suspend fun createChatCompletion(): String
    }
}

internal class KtorChat(private val client: HttpClient) : Chat {
    override val completions: Chat.Completions = KtorChatCompletions(client)
}

internal class KtorChatCompletions(private val client: HttpClient) : Chat.Completions {
    override suspend fun createChatCompletion(): String =
        client.post("/chat/completions").body()
}
