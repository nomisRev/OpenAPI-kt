package io.github.nomisrev.render.golden.client.responses.no_content.api

import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete

interface Api {
    sealed interface DeleteModelResult {
        data class OK(val value: String) : DeleteModelResult

        data object NoContent : DeleteModelResult
    }

    suspend fun deleteModel(
        model: String,
    ): DeleteModelResult
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun deleteModel(model: String): Api.DeleteModelResult {
        val response = client.delete("/models/$model")
        return when (response.status) {
            HttpStatusCode.OK -> Api.DeleteModelResult.OK(response.body())
            HttpStatusCode.NoContent -> Api.DeleteModelResult.NoContent
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
