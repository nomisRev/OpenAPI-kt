package io.github.api

import io.github.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.collections.List
import kotlinx.datetime.LocalDate

public class Versions internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/versions")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        404 -> Response.NotFound(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<LocalDate>,
      ) : Response

      public data class NotFound(
        public val `value`: BasicError,
      ) : Response
    }
  }
}
