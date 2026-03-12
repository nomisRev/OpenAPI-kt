package io.github.nomisrev.render.golden.client.operation.deprecated.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Api {
    @Deprecated("Deprecated by the API provider")
    suspend fun legacyEndpoint(): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    @Deprecated("Deprecated by the API provider")
    override suspend fun legacyEndpoint(): String =
        client.get("/legacy").body()
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
