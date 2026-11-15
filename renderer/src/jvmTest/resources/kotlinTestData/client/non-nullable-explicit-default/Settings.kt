package io.github.nomisrev.render.test.client.non.nullable.explicit.default

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlinx.serialization.Serializable

public class Settings internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(sort: String? = null, kind: Kind? = null): Preferences {
      val response = __client.get("settings") {
        if (sort != null) {
          this.url.encodedParameters.append("sort", sort.encodeURLParameter())
        }
        if (kind != null) {
          this.url.encodedParameters.append("kind", kind.toString().encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @Serializable
    public enum class Kind {
      S,
      O,
    }
  }
}
