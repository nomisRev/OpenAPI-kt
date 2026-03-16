package io.github.nomisrev.render.test.client.response.with.body

import kotlin.Int
import kotlin.String

public interface Pets {
  public suspend fun post(body: String): PostResult

  public sealed interface PostResult {
    public data class Created(
      public val `value`: String,
    ) : PostResult

    public data class BadRequest(
      public val `value`: Int,
    ) : PostResult
  }
}
