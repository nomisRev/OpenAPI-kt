package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Open internal constructor(
  private val __client: HttpClient,
) {
  public val brace: Brace = Brace(__client)

  public class Brace internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("open/{brace")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
