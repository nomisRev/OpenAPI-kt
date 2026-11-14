package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

public class Report internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("report") {
        `header`(HttpHeaders.Accept, ContentType.Application.Pdf)
      }
      return when (response.status.value) {
        200 -> Response.Ok(response)
        404 -> Response.NotFound(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val response: HttpResponse,
      ) : Response

      public data class NotFound(
        public val `value`: Problem,
      ) : Response
    }
  }
}
