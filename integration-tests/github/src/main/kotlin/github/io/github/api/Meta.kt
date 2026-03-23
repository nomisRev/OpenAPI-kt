package io.github.api

import io.github.model.ApiOverview
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Meta internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/meta")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: ApiOverview,
      ) : Response

      public data object NotModified : Response
    }
  }
}
