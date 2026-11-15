package io.github.nomisrev.render.test.client.ignored.`header`.parameters

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Reports internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend fun json(contentType: String? = null, authorization: String? = null): String {
      val response = __client.get("reports") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        if (contentType != null) {
          this.url.encodedParameters.append("Content-Type", contentType.encodeURLParameter())
        }
        if (authorization != null) {
          cookie("Authorization", authorization)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }

    public suspend fun textCsv(contentType: String? = null, authorization: String? = null): String {
      val response = __client.get("reports") {
        `header`(HttpHeaders.Accept, ContentType("text", "csv"))
        if (contentType != null) {
          this.url.encodedParameters.append("Content-Type", contentType.encodeURLParameter())
        }
        if (authorization != null) {
          cookie("Authorization", authorization)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
