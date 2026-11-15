package io.github.nomisrev.render.test.client.schema.names.case

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class B internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): HttpHealthCheck2 {
      val response = __client.get("b")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
