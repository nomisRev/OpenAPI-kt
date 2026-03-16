package io.github.nomisrev.render.test.client.simple.tree

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Users {
  public suspend fun `get`()
}

internal class KtorUsers(
  private val client: HttpClient,
) : Users {
  override suspend fun `get`() {
    client.get("/users")
  }
}
