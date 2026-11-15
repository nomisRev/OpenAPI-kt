package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Members internal constructor(
  private val __client: HttpClient,
) {
  public val count: Count = Count(__client)

  public class Count internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("members/\$count")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
