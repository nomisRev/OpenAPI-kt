package io.github.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Markdown internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val raw: Raw = Raw(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      text: String,
      mode: Mode? = null,
      context: String? = null,
    ): Response {
      val response = client.post("/markdown") {
        contentType(ContentType.Application.Json)
        setBody(Body(text = text, mode = mode, context = context))
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    public enum class Mode(
      public val `value`: String,
    ) {
      @SerialName("markdown")
      Markdown("markdown"),
      @SerialName("gfm")
      Gfm("gfm"),
      ;
    }

    @Serializable
    internal data class Body(
      public val text: String,
      public val mode: Mode? = null,
      public val context: String? = null,
    )

    public sealed interface Response {
      public data class Ok(
        public val `value`: String,
      ) : Response

      public data object NotModified : Response
    }
  }

  public class Raw internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: String? = null): Response {
        val response = client.post("/markdown/raw") {
          body?.let {
            contentType(ContentType.Text.Plain)
            setBody(it)
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: String? = null): Response {
        val response = client.post("/markdown/raw") {
          body?.let {
            contentType(ContentType("text", "x-markdown"))
            setBody(it)
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: String,
        ) : Response

        public data object NotModified : Response
      }
    }
  }
}
