package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Docs internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(): String {
      val response = __client.get("docs") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }

    public suspend fun pdf(): HttpResponse {
      val response = __client.get("docs") {
        `header`(HttpHeaders.Accept, ContentType.Application.Pdf)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response
    }
  }
}
