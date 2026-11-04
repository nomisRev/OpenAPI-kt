package io.github.nomisrev.render.test.client.operations.defaults

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(limit: Int = 20, offset: Int? = 0) {
      val response = client.get("items") {
        url.encodedParameters.append("limit", limit.toString())
        if (offset != null) {
          url.encodedParameters.append("offset", offset.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
