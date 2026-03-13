package io.github.nomisrev.render.golden.client.params.cookie.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.cookie
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Api {
    suspend fun getData(
        sessionId: String,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun getData(sessionId: String): String =
        client.get("/data") {
            cookie("session_id", sessionId)
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
