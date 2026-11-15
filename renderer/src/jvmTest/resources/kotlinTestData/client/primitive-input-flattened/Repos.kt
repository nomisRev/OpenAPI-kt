package io.github.nomisrev.render.test.client.primitive.input.flattened

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class Repos internal constructor(
  private val __client: HttpClient,
) {
  public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(__client, alertNumber)

  public class AlertNumberPath internal constructor(
    private val __client: HttpClient,
    private val _alertNumber: Long,
  ) {
    public val `get`: Get = Get(__client, _alertNumber)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _alertNumber: Long,
    ) {
      @OptIn(ExperimentalUuidApi::class)
      public suspend operator fun invoke(
        xRequestId: Uuid,
        toolName: String? = null,
        session: Instant? = null,
      ) {
        val response = __client.get("repos/${this._alertNumber}") {
          if (toolName != null) {
            this.url.encodedParameters.append("tool_name", toolName.encodeURLParameter())
          }
          `header`("X-Request-Id", xRequestId.toString())
          if (session != null) {
            cookie("session", session.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }
  }
}
