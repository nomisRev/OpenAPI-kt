package io.github.nomisrev.render.test.client.path.separator.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.put

public class PQ internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val put: Put = Put(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("p-q")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.put("p_q")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
