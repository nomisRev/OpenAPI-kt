package io.github.nomisrev.render.test.client.operations.`inline`.markdown.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface Markdown {
  public val post: Post

  public interface Post {
    public suspend operator fun invoke(body: Body): Response

    @Serializable
    public data class Body(
      public val text: String,
      public val mode: Mode? = null,
      public val context: String? = null,
    ) {
      @Serializable
      public enum class Mode {
        @SerialName("markdown")
        Markdown,
        @SerialName("gfm")
        Gfm,
      }
    }

    public data class Response(
      public val `value`: String,
    )
  }
}

internal class KtorMarkdown(
  private val client: HttpClient,
) : Markdown {
  override val post: Markdown.Post = object : Markdown.Post {
    override suspend operator fun invoke(body: Markdown.Post.Body): Markdown.Post.Response {
      val value: String = client.post("/markdown") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
      return Markdown.Post.Response(value)
    }
  }
}
