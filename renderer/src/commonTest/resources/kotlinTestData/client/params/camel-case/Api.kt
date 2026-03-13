package io.github.nomisrev.render.golden.client.params.camel_case.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Api {
    suspend fun listEvents(
        fineTuningJobId: String,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun listEvents(fineTuningJobId: String): String =
        client.get("/fine_tuning/jobs/$fineTuningJobId/events").body()
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
