package io.github.nomisrev.render.golden.client.responses.multiple.api

import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Api {
    sealed interface RetrieveModelResult {
        data class OK(val value: String) : RetrieveModelResult

        data class NotFound(val value: String) : RetrieveModelResult
    }

    suspend fun retrieveModel(
        model: String,
    ): RetrieveModelResult
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun retrieveModel(model: String): Api.RetrieveModelResult {
        val response = client.get("/models/$model")
        return when (response.status) {
            HttpStatusCode.OK -> Api.RetrieveModelResult.OK(response.body())
            HttpStatusCode.NotFound -> Api.RetrieveModelResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
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
