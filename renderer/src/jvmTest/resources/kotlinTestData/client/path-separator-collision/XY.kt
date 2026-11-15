package io.github.nomisrev.render.test.client.path.separator.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class XY internal constructor(
  private val __client: HttpClient,
) {
  public val a: A = A(__client)

  public class A internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("x-y/a")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
