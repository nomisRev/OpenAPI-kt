package io.github.nomisrev.render.test.client.raw.error.and.default.response

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.String

public class Reports internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("/reports")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        400 -> Response.BadRequest(response)
        else -> Response.Default(response.status, response)
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: String,
      ) : Response

      public data class BadRequest(
        public val `value`: HttpResponse,
      ) : Response

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: HttpResponse,
      ) : Response
    }
  }
}
