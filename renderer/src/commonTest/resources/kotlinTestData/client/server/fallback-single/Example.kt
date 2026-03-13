package io.github.nomisrev.render.golden.client.server.fallback_single.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Example {
    suspend fun listData(): String
}

sealed interface ExampleServer {
    val url: String

    data object Default : ExampleServer {
        override val url = "https://api.example.com/v1"
    }


    data class Custom(override val url: String) : ExampleServer
}


internal class KtorExample(private val client: HttpClient) : Example {
    override suspend fun listData(): String =
        client.get("/data").body()
}

fun ExampleClient(
    server: ExampleServer = ExampleServer.Default,
    block: HttpClientConfig<*>.() -> Unit = {},
): Example {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(server.url) }
        block()
    }
    return KtorExample(client)
}
