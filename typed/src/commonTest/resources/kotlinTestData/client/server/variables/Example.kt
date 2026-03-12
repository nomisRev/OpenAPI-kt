package io.github.nomisrev.render.golden.client.server.variables.api

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

    data class MultiEnvironment(
        val environment: Environment = Environment.Production,
        val version: String = "v2",
    ) : ExampleServer {
        override val url: String
            get() = "https://${environment.value}.api.example.com/${version}"

        enum class Environment(val value: String) {
            Production("production"),
            Staging("staging"),
            Dev("dev"),
        }
    }


    data class Custom(override val url: String) : ExampleServer
}


internal class KtorExample(private val client: HttpClient) : Example {
    override suspend fun listData(): String =
        client.get("/data").body()
}

fun ExampleClient(
    server: ExampleServer = ExampleServer.MultiEnvironment(),
    block: HttpClientConfig<*>.() -> Unit = {},
): Example {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(server.url) }
        block()
    }
    return KtorExample(client)
}
