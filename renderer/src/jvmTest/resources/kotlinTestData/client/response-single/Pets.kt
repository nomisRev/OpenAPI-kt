package io.github.nomisrev.render.test.client.response.single

import kotlin.String
import kotlin.collections.List

public interface Pets {
  public suspend fun `get`(): List<String>
}
