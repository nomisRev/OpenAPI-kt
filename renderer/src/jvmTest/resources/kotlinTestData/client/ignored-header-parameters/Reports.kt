package io.github.nomisrev.render.test.client.ignored.`header`.parameters

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Reports internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend fun json(contentType: String? = null, authorization: String? = null): String {
      val response = client.get("/reports") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType?.let { parameter("Content-Type", it) }
        authorization?.let { cookie("Authorization", it) }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }

    public suspend fun textCsv(contentType: String? = null, authorization: String? = null): String {
      val response = client.get("/reports") {
        `header`(HttpHeaders.Accept, ContentType("text", "csv"))
        contentType?.let { parameter("Content-Type", it) }
        authorization?.let { cookie("Authorization", it) }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
