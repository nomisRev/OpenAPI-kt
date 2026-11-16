package io.github.nomisrev.render.test.client.operations.dropped.on.errors

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.serialization.JsonConvertException
import kotlin.Int
import kotlin.String
import kotlinx.serialization.json.JsonPrimitive

public class Items internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(limit: Int? = null): String {
      val response = __client.get("items") {
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
    }
  }
}
