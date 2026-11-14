package io.github.nomisrev.render.test.client.parameter.name.collisions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.String

public class Prices internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      validUntilDate: String? = null,
      validUntilDateLt: String? = null,
      validUntilDateGt: String? = null,
      amountGte: Int? = null,
      amountLte: Int? = null,
      amount: Int? = null,
      statusNot: String? = null,
      status: String? = null,
    ) {
      val response = client.get("prices") {
        if (validUntilDate != null) {
          this.url.encodedParameters.append("ValidUntilDate", validUntilDate.encodeURLParameter())
        }
        if (validUntilDateLt != null) {
          this.url.encodedParameters.append("ValidUntilDate%3C", validUntilDateLt.encodeURLParameter())
        }
        if (validUntilDateGt != null) {
          this.url.encodedParameters.append("ValidUntilDate%3E", validUntilDateGt.encodeURLParameter())
        }
        if (amountGte != null) {
          this.url.encodedParameters.append("amount%3E%3D", amountGte.toString())
        }
        if (amountLte != null) {
          this.url.encodedParameters.append("amount%3C%3D", amountLte.toString())
        }
        if (amount != null) {
          this.url.encodedParameters.append("amount", amount.toString())
        }
        if (statusNot != null) {
          this.url.encodedParameters.append("status%21", statusNot.encodeURLParameter())
        }
        if (status != null) {
          this.url.encodedParameters.append("status", status.encodeURLParameter())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
