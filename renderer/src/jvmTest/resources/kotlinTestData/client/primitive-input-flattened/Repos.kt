package io.github.nomisrev.render.test.client.primitive.input.flattened

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.parameter
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
        client.get("/repos/$alertNumber") {
          toolName?.let { parameter("tool_name", it) }
          `header`("X-Request-Id", xRequestId.toString())
          session?.let { cookie("session", it.toString()) }
        }
      }
    }
  }
}
