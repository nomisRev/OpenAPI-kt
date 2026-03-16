package io.github.nomisrev.render.test.client.operations.deprecated

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Deprecated

public interface Legacy {
  @Deprecated("Deprecated by the API provider")
  public suspend fun `get`()
}

internal class KtorLegacy(
  private val client: HttpClient,
) : Legacy {
  @Deprecated("Deprecated by the API provider")
  override suspend fun `get`() {
    client.get("/legacy")
  }
}
