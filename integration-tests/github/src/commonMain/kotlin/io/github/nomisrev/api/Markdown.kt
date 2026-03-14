package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.http.contentType

interface Markdown {
    val raw: Markdown.Raw

    @Serializable
    data class MarkdownRenderBody(val text: String, val mode: Mode? = null, val context: String? = null) {
        @Serializable
        enum class Mode {
            @SerialName("markdown") Markdown, @SerialName("gfm") Gfm;
        }
    }

    sealed interface MarkdownRenderResult {
        data class OK(val value: String) : MarkdownRenderResult

        data object NotModified : MarkdownRenderResult
    }

    suspend fun markdownRender(
        body: MarkdownRenderBody,
    ): MarkdownRenderResult

    interface Raw {
        sealed interface MarkdownRenderRawResult {
            data class OK(val value: String) : MarkdownRenderRawResult

            data object NotModified : MarkdownRenderRawResult
        }

        suspend fun markdownRenderRaw(
            body: String? = null,
        ): MarkdownRenderRawResult
    }
}

internal class KtorMarkdown(private val client: HttpClient) : Markdown {
    override val raw: Markdown.Raw = KtorMarkdownRaw(client)

    override suspend fun markdownRender(body: Markdown.MarkdownRenderBody): Markdown.MarkdownRenderResult {
        val response = client.post("/markdown") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Markdown.MarkdownRenderResult.OK(response.body())
            HttpStatusCode.NotModified -> Markdown.MarkdownRenderResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarkdownRaw(private val client: HttpClient) : Markdown.Raw {
    override suspend fun markdownRenderRaw(body: String?): Markdown.Raw.MarkdownRenderRawResult {
        val response = client.post("/markdown/raw") {
            contentType(ContentType.Text.Plain)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Markdown.Raw.MarkdownRenderRawResult.OK(response.body())
            HttpStatusCode.NotModified -> Markdown.Raw.MarkdownRenderRawResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
