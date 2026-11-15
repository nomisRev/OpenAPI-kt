package io.github.nomisrev.render.test.client.path.identifier.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Owners internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val slash: Slash = Slash(__client)

  public val slash2: Slash2 = Slash2(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = __client.get("owners")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Slash internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("owners/")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class Slash2 internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("owners/slash")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
