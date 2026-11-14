package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Reports internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(client: String) {
      val response = this.client.get("reports") {
        this.url.encodedParameters.append("client", client.encodeURLParameter())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
