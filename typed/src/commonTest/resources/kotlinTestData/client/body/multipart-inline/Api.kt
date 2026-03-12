package io.github.nomisrev.render.golden.client.body.multipart_inline.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post

interface Api {
    suspend fun uploadFile(
        file: ByteArray,
        purpose: String,
    ): String
}

internal class KtorApi(private val client: HttpClient) : Api {
    override suspend fun uploadFile(file: ByteArray, purpose: String): String =
        client.post("/files") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", file)
                        append("purpose", purpose.toString())
                    }
                )
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
