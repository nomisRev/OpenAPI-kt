package io.github.api

import io.github.model.BasicError
import io.github.model.ValidationErrorSimple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

public class Credentials internal constructor(
  private val client: HttpClient,
) {
  public val revoke: Revoke = Revoke(client)

  public class Revoke internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(credentials: List<String>): Response {
        val response = client.post("/credentials/revoke") {
          contentType(ContentType.Application.Json)
          setBody(Body(credentials = credentials))
        }
        return when (response.status.value) {
          202 -> Response.Accepted(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          500 -> Response.InternalServerError(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @JvmInline
      @Serializable
      internal value class Body(
        public val credentials: List<String>,
      )

      public sealed interface Response {
        public data class Accepted(
          public val `value`: JsonElement,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationErrorSimple,
        ) : Response

        public data class InternalServerError(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }
}
