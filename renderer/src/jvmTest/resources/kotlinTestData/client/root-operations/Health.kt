package io.github.nomisrev.render.test.client.root.operations

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Health {
  public suspend fun `get`()
}

internal class KtorHealth(
  private val client: HttpClient,
) : Health {
  override suspend fun `get`() {
    client.get("/health")
  }
}
