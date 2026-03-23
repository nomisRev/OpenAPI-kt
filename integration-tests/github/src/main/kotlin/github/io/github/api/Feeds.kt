package io.github.api

import io.github.model.Feed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`

public class Feeds internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Feed = client.get("/feeds").body()
  }
}
