package io.github.nomisrev.render.golden.client.responses.default.api

import io.ktor.http.HttpStatusCode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get

interface Api {
    sealed interface GetModelResult {
        data class OK(val value: String) : GetModelResult

        data class Default(val status: HttpStatusCode, val value: String) : GetModelResult
    }

    suspend fun getModel(
        model: String,
    ): GetModelResult
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun getModel(model: String): Api.GetModelResult {
        val response = client.get("/models/$model")
        return when (response.status) {
            HttpStatusCode.OK -> Api.GetModelResult.OK(response.body())
            else -> Api.GetModelResult.Default(response.status, response.body())
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
