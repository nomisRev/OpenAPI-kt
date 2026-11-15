package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Handles internal constructor(
  private val __client: HttpClient,
) {
  public val aB: AB = AB(__client)

  public class AB internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("handles/a@b")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
