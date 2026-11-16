package io.github.nomisrev.render.test.client.allof.closed.`object`

import io.github.nomisrev.render.test.client.allof.closed.object_.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Errors internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): ApiException {
      val response = __client.get("errors")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
