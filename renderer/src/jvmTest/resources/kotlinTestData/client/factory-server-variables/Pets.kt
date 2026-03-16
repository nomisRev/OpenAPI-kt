package io.github.nomisrev.render.test.client.factory.server.variables

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public interface Pets {
  public suspend fun `get`()
}

internal class KtorPets(
  private val client: HttpClient,
) : Pets {
  override suspend fun `get`() {
    client.get("/pets")
  }
}
