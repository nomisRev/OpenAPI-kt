package io.github.nomisrev.render.test.client.operations.defaults

import kotlin.Int

public interface Items {
  public suspend fun `get`(limit: Int = 20, offset: Int? = 0)
}
