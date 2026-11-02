package io.github.nomisrev.render.test.client.response.separate.methods.flattened.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(name: String, tag: String? = null): JsonResponse {
      val response = client.post("/pets") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, tag = tag))
      }
      return when (response.status.value) {
        201 -> JsonResponse.Created(response.body())
        400 -> BadRequest(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public suspend fun textPlain(name: String, tag: String? = null): TextPlainResponse {
      val response = client.post("/pets") {
        `header`(HttpHeaders.Accept, ContentType.Text.Plain)
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, tag = tag))
      }
      return when (response.status.value) {
        201 -> TextPlainResponse.Created(response.body())
        400 -> BadRequest(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    internal data class Body(
      public val name: String,
      public val tag: String? = null,
    )

    public sealed interface JsonResponse {
      public data class Created(
        public val `value`: Long,
      ) : JsonResponse
    }

    public sealed interface TextPlainResponse {
      public data class Created(
        public val `value`: String,
      ) : TextPlainResponse
    }

    public data class BadRequest(
      public val `value`: Int,
    ) : JsonResponse,
        TextPlainResponse
  }
}
