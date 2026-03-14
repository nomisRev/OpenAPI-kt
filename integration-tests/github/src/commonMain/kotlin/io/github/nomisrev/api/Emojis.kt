package io.github.nomisrev.api

import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

interface Emojis {
    sealed interface EmojisGetResult {
        data class OK(val value: List<String>) : EmojisGetResult

        data object NotModified : EmojisGetResult
    }

    suspend fun emojisGet(): EmojisGetResult
}

internal class KtorEmojis(private val client: HttpClient) : Emojis {
    override suspend fun emojisGet(): Emojis.EmojisGetResult {
        val response = client.get("/emojis")
        return when (response.status) {
            HttpStatusCode.OK -> Emojis.EmojisGetResult.OK(response.body())
            HttpStatusCode.NotModified -> Emojis.EmojisGetResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
