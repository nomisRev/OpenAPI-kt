package io.github.nomisrev.render.test.client.state.name.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Realms internal constructor(
  private val __client: HttpClient,
) {
  public val client: Client = Client(__client)

  public class Client internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("realms/client")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
