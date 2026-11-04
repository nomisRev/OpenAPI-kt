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
  private val client: HttpClient,
) {
  public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(client, alertNumber)

  public class AlertNumberPath internal constructor(
    private val client: HttpClient,
    private val alertNumber: Long,
  ) {
    public val `get`: Get = Get(client, alertNumber)

    public class Get internal constructor(
      private val client: HttpClient,
      private val alertNumber: Long,
    ) {
      @OptIn(ExperimentalUuidApi::class)
      public suspend operator fun invoke(
        xRequestId: Uuid,
        toolName: String? = null,
        session: Instant? = null,
      ) {
        val response = client.get("/repos/$alertNumber") {
          if (toolName != null) {
            url.encodedParameters.append("tool_name", toolName.encodeURLParameter())
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
