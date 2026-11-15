package io.github.nomisrev.render.test.client.primitive.input.`object`.guard

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlinx.serialization.Serializable

public class Tags internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(filter: Filter? = null) {
      val response = __client.get("tags") {
        if (filter != null) {
          this.url.encodedParameters.append("value", filter.value.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @Serializable
    public data class Filter(
      public val `value`: String,
    )
  }
}
