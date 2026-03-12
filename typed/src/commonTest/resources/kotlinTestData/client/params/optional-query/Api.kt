package io.github.nomisrev.render.golden.client.params.optional_query.api

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
        limit: Int? = null,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun listItems(limit: Int?): String =
        client.get("/items") {
            limit?.let { parameter("limit", it) }
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
