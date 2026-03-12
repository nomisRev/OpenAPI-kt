package io.github.nomisrev.render.golden.client.body.content_type_preference.api

import io.github.nomisrev.model.CreateThingRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Api {
    suspend fun createThing(
        body: CreateThingRequest,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun createThing(body: CreateThingRequest): String =
        client.post("/things") {
            contentType(ContentType.Application.Json)
            setBody(body)
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
