package io.github.api

import io.github.model.Feed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Feeds internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Feed {
      val response = __client.get("feeds")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
