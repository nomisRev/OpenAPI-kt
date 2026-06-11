package io.github.api

import io.github.model.BasicError
import io.github.model.CodeOfConduct
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public class CodesOfConduct internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun key(key: String): KeyPath = KeyPath(client, key)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/codes_of_conduct")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<CodeOfConduct>,
      ) : Response

      public data object NotModified : Response
    }
  }

  public class KeyPath internal constructor(
    private val client: HttpClient,
    private val key: String,
  ) {
    public val `get`: Get = Get(client, key)

    public class Get internal constructor(
      private val client: HttpClient,
      private val key: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/codes_of_conduct/$key")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: CodeOfConduct,
        ) : Response

        public data object NotModified : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }
}
