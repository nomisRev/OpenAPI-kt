package io.github.api

import io.github.model.BasicError
import io.github.model.Integration
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String

public class Apps internal constructor(
  private val client: HttpClient,
) {
  public fun appSlug(appSlug: String): AppSlugPath = AppSlugPath(client, appSlug)

  public class AppSlugPath internal constructor(
    private val client: HttpClient,
    private val appSlug: String,
  ) {
    public val `get`: Get = Get(client, appSlug)

    public class Get internal constructor(
      private val client: HttpClient,
      private val appSlug: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/apps/$appSlug")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: Integration?,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }
}
