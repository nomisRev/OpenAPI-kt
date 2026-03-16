package io.github.nomisrev.render.test.client.operations.defaults

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Int

public interface Items {
  public suspend fun `get`(limit: Int = 20, offset: Int? = 0)
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override suspend fun `get`(limit: Int, offset: Int?) {
    client.get("/items") {
      parameter("limit", limit)
      offset?.let { parameter("offset", it) }
    }
  }
}
