package io.github.nomisrev.render.test.client.path.item.ref

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String

public class Referenced internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val put: Put = Put(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(limit: Int, xTraceId: String): Owner {
      val response = client.get("referenced") {
        this.url.encodedParameters.append("limit", limit.toString())
        `header`("X-Trace-Id", xTraceId)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(xTraceId: String, body: Owner) {
      val response = client.put("referenced") {
        `header`("X-Trace-Id", xTraceId)
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
