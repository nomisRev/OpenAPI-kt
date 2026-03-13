package io.github.nomisrev.render.golden.client.body.form_urlencoded.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import io.ktor.client.request.setBody
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Api {
    suspend fun createToken(
        grantType: String,
        code: String,
        redirectUri: String,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun createToken(grantType: String, code: String, redirectUri: String): String =
        client.post("/oauth/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                Parameters.build {
                    append("grant_type", grantType)
                    append("code", code)
                    append("redirect_uri", redirectUri)
                }.formUrlEncode()
            )
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
