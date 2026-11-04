package io.github.nomisrev.render.test.client.path.identifier.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Owners internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val slash: Slash = Slash(client)

  public val slash2: Slash2 = Slash2(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("owners")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }

  public class Slash internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("owners/")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class Slash2 internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("owners/slash")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
