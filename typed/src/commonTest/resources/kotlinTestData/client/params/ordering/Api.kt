package io.github.nomisrev.render.golden.client.params.ordering.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Api {
    suspend fun getResource(
        id: String,
        requiredQ: String,
        xRequired: String,
        optionalQ: String? = null,
        xOptional: String? = null,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun getResource(id: String, requiredQ: String, xRequired: String, optionalQ: String?, xOptional: String?): String =
        client.get("/resources/$id") {
            parameter("requiredQ", requiredQ)
            header("X-Required", xRequired)
            optionalQ?.let { parameter("optionalQ", it) }
            xOptional?.let { header("X-Optional", it) }
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
