package io.github.nomisrev.render.test.client.operations.optional

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.parameter
import kotlin.Int
import kotlin.String

public interface Items {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke(
      query: String,
      xRequestId: String,
      limit: Int? = null,
      xTraceId: String? = null,
      preference: String? = null,
    )
  }
}

internal class KtorItems(
  private val client: HttpClient,
) : Items {
  override val `get`: Items.Get = object : Items.Get {
    override suspend operator fun invoke(
      query: String,
      xRequestId: String,
      limit: Int?,
      xTraceId: String?,
      preference: String?,
    ) {
      client.get("/items") {
        parameter("query", query)
        limit?.let { parameter("limit", it) }
        `header`("X-Request-Id", xRequestId)
        xTraceId?.let { `header`("X-Trace-Id", it) }
        preference?.let { cookie("preference", it) }
      }
    }
  }
}
