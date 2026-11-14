package io.github.nomisrev.render.test.client.factory.server.names

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Ping internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("ping")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
