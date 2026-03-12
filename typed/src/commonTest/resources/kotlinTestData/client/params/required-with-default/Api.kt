package io.github.nomisrev.render.golden.client.params.required_with_default.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Api {
    suspend fun listItems(
        limit: Int = 20,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun listItems(limit: Int): String =
        client.get("/items") {
            parameter("limit", limit)
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
