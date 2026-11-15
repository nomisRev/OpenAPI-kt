package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Pairs internal constructor(
  private val __client: HttpClient,
) {
  public val xY: XY = XY(__client)

  public val xY2: XY2 = XY2(__client)

  public class XY internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("pairs/x@y")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class XY2 internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("pairs/x=y")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
