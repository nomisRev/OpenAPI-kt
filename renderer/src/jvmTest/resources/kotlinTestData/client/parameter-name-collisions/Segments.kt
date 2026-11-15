package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class Segments internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(xList: List<String>? = null, x: String? = null) {
      val response = __client.get("segments") {
        xList?.forEach { this.url.encodedParameters.append("x%5B%5D", it.encodeURLParameter()) }
        if (x != null) {
          this.url.encodedParameters.append("x", x.encodeURLParameter())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
