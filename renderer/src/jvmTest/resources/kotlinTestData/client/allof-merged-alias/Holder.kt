package io.github.nomisrev.render.test.client.allof.merged.alias

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Holder internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): MergedAliasHolder {
      val response = client.get("/holder")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
