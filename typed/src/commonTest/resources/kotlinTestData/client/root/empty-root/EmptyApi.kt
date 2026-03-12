package root.empty.root.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

interface EmptyApi

internal class KtorEmptyApi(private val client: HttpClient) : EmptyApi

fun EmptyApiClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): EmptyApi {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorEmptyApi(client)
}
