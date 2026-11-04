package io.github.nomisrev.render.test.client.wildcard.media.types

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String

public class TextGreeting internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): String {
      val response = client.get("text-greeting")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
