package io.github.nomisrev.render.test.client.factory.servers.variable.default

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public class Pets internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      client.get("/pets")
    }
  }
}
