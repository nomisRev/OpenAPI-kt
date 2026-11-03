package io.github.nomisrev.render.test.client.allof.untyped.member

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): List<String> {
      val response = client.get("/tags")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
