package io.github.nomisrev.render.test.client.request.builder.shadowing

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Search internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      url: String,
      x: Int,
      parameter: String? = null,
      method: String? = null,
      headers: String? = null,
      append: List<String>? = null,
      `header`: String? = null,
      cookie: String? = null,
    ) {
      val response = __client.get("search") {
        this.url.encodedParameters.append("url", url.encodeURLParameter())
        this.url.encodedParameters.append("x", x.toString())
        if (parameter != null) {
          this.url.encodedParameters.append("parameter", parameter.encodeURLParameter())
        }
        if (method != null) {
          this.url.encodedParameters.append("method", method.encodeURLParameter())
        }
        if (headers != null) {
          this.url.encodedParameters.append("headers", headers.encodeURLParameter())
        }
        append?.forEach { this.url.encodedParameters.append("append", it.encodeURLParameter()) }
        if (header != null) {
          `header`("header", header)
        }
        if (cookie != null) {
          cookie("cookie", cookie)
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
