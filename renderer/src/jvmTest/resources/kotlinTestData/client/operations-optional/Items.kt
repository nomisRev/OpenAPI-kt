package io.github.nomisrev.render.test.client.operations.optional

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.String

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String,
      xRequestId: String,
      limit: Int? = null,
      xTraceId: String? = null,
      preference: String? = null,
    ) {
      val response = client.get("/items") {
        url.encodedParameters.append("query", query.encodeURLParameter())
        if (limit != null) {
          url.encodedParameters.append("limit", limit.toString())
        }
        `header`("X-Request-Id", xRequestId)
        if (xTraceId != null) {
          `header`("X-Trace-Id", xTraceId)
        }
        if (preference != null) {
          cookie("preference", preference)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
