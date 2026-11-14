package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Filters internal constructor(
  private val client: HttpClient,
) {
  public val aB: AB = AB(client)

  public class AB internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("filters/a=b")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
