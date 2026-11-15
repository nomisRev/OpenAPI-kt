package io.github.nomisrev.render.test.client.media.type.without.schema

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlinx.serialization.json.JsonElement

public class Data internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): JsonElement {
      val response = __client.get("data")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
