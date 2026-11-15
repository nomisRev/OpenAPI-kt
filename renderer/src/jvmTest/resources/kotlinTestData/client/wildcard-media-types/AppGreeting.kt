package io.github.nomisrev.render.test.client.wildcard.media.types

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class AppGreeting internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): String {
      val response = __client.get("app-greeting")
      if (response.status.value != 200) throw ResponseException(response, "")
      return if (response.contentType()?.let { it.match(ContentType.Application.Json) || it.contentSubtype.endsWith("+json", ignoreCase = true) } == true) response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") } else response.body()
    }
  }
}
