package io.github.nomisrev.render.test.client.discriminated.subtype.redeclaration

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Feed internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Activity {
      val response = __client.get("feed")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
