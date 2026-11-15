package io.github.nomisrev.render.test.client.path.trailing.slash

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class A internal constructor(
  private val __client: HttpClient,
) {
  public val b: B = B(__client)

  public class B internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("a//b")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
