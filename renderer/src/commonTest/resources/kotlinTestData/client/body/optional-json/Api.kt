package io.github.nomisrev.render.golden.client.body.optional_json.api

import io.github.nomisrev.model.UpdateSettingsRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.patch
import io.ktor.http.contentType

interface Api {
    suspend fun updateSettings(
        body: UpdateSettingsRequest? = null,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun updateSettings(body: UpdateSettingsRequest?): String =
        client.patch("/settings") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
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
