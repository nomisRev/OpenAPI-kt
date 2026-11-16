package io.github.nomisrev.render.test.client.recursive.allof.ref

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Nodes internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Node {
      val response = __client.get("nodes")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
