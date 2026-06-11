package io.github.nomisrev.render.test.client.root.operations

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public class Health internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      client.get("/health")
    }
  }
}
