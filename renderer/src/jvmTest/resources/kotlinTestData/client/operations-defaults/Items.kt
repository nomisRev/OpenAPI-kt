package io.github.nomisrev.render.test.client.operations.defaults

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Int

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(limit: Int = 20, offset: Int? = 0) {
      client.get("/items") {
        parameter("limit", limit)
        offset?.let { parameter("offset", it) }
      }
    }
  }
}
