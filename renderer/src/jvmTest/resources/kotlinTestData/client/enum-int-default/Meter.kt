package io.github.nomisrev.render.test.client.`enum`.int.default

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlinx.serialization.Serializable

public class Meter internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      rate: Rate? = Rate.`300`,
      limit: Long? = 25L,
      ratio: Double? = 1.5,
      on: Boolean? = true,
    ) {
      val response = client.get("meter") {
        if (rate != null) {
          url.encodedParameters.append("rate", rate.toString().encodeURLParameter())
        }
        if (limit != null) {
          url.encodedParameters.append("limit", limit.toString())
        }
        if (ratio != null) {
          url.encodedParameters.append("ratio", ratio.toString())
        }
        if (on != null) {
          url.encodedParameters.append("on", on.toString())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable
    public enum class Rate {
      `300`,
      `600`,
      `1200`,
    }
  }
}
