package io.github.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String

public class Zen internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): String = client.get("/zen").body()
  }
}
