package io.github.nomisrev.render.test.client.operations.deprecated

import kotlin.Deprecated

public interface Legacy {
  @Deprecated("Deprecated by the API provider")
  public suspend fun `get`()
}
