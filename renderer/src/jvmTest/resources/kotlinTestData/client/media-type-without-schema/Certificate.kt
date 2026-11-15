package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

public class Certificate internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): HttpResponse {
      val response = __client.get("certificate") {
        `header`(HttpHeaders.Accept, ContentType.Application.Pdf)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response
    }
  }
}
