package io.github.nomisrev.render.golden.client.params.inline_enum.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Api {
    @Serializable
    enum class Direction {
        @SerialName("asc") Asc, @SerialName("desc") Desc;
    }

    suspend fun listAdvisories(
        direction: Direction = Direction.Desc,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun listAdvisories(direction: Api.Direction): String =
        client.get("/advisories") {
            parameter("direction", direction)
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
