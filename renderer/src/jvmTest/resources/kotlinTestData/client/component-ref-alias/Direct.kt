package io.github.nomisrev.render.test.client.component.ref.alias

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Int
import kotlin.String

public class Direct internal constructor(
  private val __client: HttpClient,
) {
  public val put: Put = Put(__client)

  public class Put internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Int,
      xTraceId: String,
      body: Owner,
    ): Owner {
      val response = __client.put("direct") {
        this.url.encodedParameters.append("limit", limit.toString())
        `header`("X-Trace-Id", xTraceId)
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
