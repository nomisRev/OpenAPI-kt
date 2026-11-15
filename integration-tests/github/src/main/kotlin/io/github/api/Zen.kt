package io.github.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String

public class Zen internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): String {
      val response = __client.get("zen")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
