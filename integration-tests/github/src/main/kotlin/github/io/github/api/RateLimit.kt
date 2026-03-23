package io.github.api

import io.github.model.BasicError
import io.github.model.RateLimitOverview
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class RateLimit internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/rate_limit")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        404 -> Response.NotFound(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: RateLimitOverview,
      ) : Response

      public data object NotModified : Response

      public data class NotFound(
        public val `value`: BasicError,
      ) : Response
    }
  }
}
