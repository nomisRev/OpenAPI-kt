package io.github.nomisrev.render.test.client.operations.basic

import kotlin.String

public interface Pets {
  public suspend fun `get`()

  public suspend fun post(body: String)
}
