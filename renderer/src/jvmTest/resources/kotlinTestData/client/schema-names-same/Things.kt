package io.github.nomisrev.render.test.client.schema.names.same

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Things internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Thing {
      val response = __client.get("things")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
