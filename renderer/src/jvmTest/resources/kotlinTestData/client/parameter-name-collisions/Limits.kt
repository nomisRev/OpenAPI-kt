package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.String

public class Limits internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      maxResults: Long? = null,
      maxResults2: String? = "all",
      maxResults3: Boolean? = null,
    ) {
      val response = client.get("limits") {
        if (maxResults != null) {
          this.url.encodedParameters.append("maxResults", maxResults.toString())
        }
        if (maxResults2 != null) {
          this.url.encodedParameters.append("MaxResults", maxResults2.encodeURLParameter())
        }
        if (maxResults3 != null) {
          this.url.encodedParameters.append("max_results", maxResults3.toString())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
