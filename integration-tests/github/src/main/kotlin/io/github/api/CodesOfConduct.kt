package io.github.api

import io.github.model.BasicError
import io.github.model.CodeOfConduct
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class CodesOfConduct internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun key(key: String): KeyPath = KeyPath(__client, key)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = __client.get("codes_of_conduct")
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
    private val __client: HttpClient,
    private val _key: String,
  ) {
    public val `get`: Get = Get(__client, _key)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _key: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("codes_of_conduct/${this._key.encodeURLParameter()}")
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
