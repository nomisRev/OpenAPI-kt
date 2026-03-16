package io.github.nomisrev.render.test.client.operations.optional

import kotlin.Int
import kotlin.String

public interface Items {
  public suspend fun `get`(
    query: String,
    xRequestId: String,
    limit: Int? = null,
    xTraceId: String? = null,
    preference: String? = null,
  )
}
