package io.github.nomisrev.render.test.client.operations.params

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Search internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String,
      xApiKey: String,
      session: String,
    ) {
      val response = __client.get("search") {
        this.url.encodedParameters.append("query", query.encodeURLParameter())
        `header`("X-Api-Key", xApiKey)
        cookie("session", session)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
