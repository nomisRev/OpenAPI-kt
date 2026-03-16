package io.github.nomisrev.render.test.client.operations.`inline`.markdown.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String

public interface Markdown {
  public suspend fun post(body: PostBody): String

  public data class PostBody(
    public val text: String,
    public val mode: Mode? = Mode.Markdown,
    public val context: String? = null,
  )

  public enum class Mode {
    Markdown,
    Gfm,
  }
}

internal class KtorMarkdown(
  private val client: HttpClient,
) : Markdown {
  override suspend fun post(body: Markdown.PostBody): String {
    return client.post("/markdown") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }
}
