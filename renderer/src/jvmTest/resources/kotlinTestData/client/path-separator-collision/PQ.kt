package io.github.nomisrev.render.test.client.path.separator.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.put

public class PQ internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val put: Put = Put(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.get("p-q")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Put internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.put("p_q")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
