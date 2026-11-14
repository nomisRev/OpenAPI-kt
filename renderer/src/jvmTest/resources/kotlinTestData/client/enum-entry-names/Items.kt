package io.github.nomisrev.render.test.client.`enum`.entry.names

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      ordering: Ordering? = Ordering.MinusLastActivity,
      `operator`: Operator? = Operator.LessThanOrEqual,
      kind: Kind? = Kind.Empty,
    ) {
      val response = client.get("items") {
        if (ordering != null) {
          this.url.encodedParameters.append("ordering", ordering.value.encodeURLParameter())
        }
        if (operator != null) {
          this.url.encodedParameters.append("operator", operator.value.encodeURLParameter())
        }
        if (kind != null) {
          this.url.encodedParameters.append("kind", kind.value.encodeURLParameter())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    public enum class Ordering(
      public val `value`: String,
    ) {
      @SerialName("last_activity")
      LastActivity("last_activity"),
      @SerialName("-last_activity")
      MinusLastActivity("-last_activity"),
      ;
    }

    @Serializable
    public enum class Kind(
      public val `value`: String,
    ) {
      @SerialName("")
      Empty(""),
      @SerialName("spiders")
      Spiders("spiders"),
      ;
    }
  }
}
