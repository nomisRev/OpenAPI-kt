package io.github.nomisrev.render.test.client.operations.params

import kotlin.String

public interface Search {
  public suspend fun `get`(
    query: String,
    xApiKey: String,
    session: String,
  )
}
