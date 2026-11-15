package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Kinds internal constructor(
  private val __client: HttpClient,
) {
  public val plain: Plain = Plain(__client)

  public val withSpace: WithSpace = WithSpace(__client)

  public class Plain internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("kinds/plain")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class WithSpace internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("kinds/with%20space")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
