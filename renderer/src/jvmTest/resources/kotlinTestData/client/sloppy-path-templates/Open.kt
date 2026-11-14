package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Open internal constructor(
  private val client: HttpClient,
) {
  public val brace: `{brace` = `{brace`(client)

  public class `{brace` internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("open/{brace")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
