package io.github.nomisrev.render.test.client.optional.nullable.wire

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      filter: Filter? = null,
      deep: Filter? = null,
      xFilter: Filter? = null,
      filterCookie: Filter? = null,
    ) {
      val response = client.get("search") {
        if (filter != null) {
          filter.q.valueOrNull?.let { filterQValue ->
            this.url.encodedParameters.append("q", filterQValue.encodeURLParameter())
          }
          if (filter.limit != null) {
            this.url.encodedParameters.append("limit", filter.limit.toString())
          }
        }
        if (deep != null) {
          deep.q.valueOrNull?.let { deepQValue ->
            this.url.encodedParameters.append("deep%5Bq%5D", deepQValue.encodeURLParameter())
          }
          if (deep.limit != null) {
            this.url.encodedParameters.append("deep%5Blimit%5D", deep.limit.toString())
          }
        }
        if (xFilter != null) {
          `header`("X-Filter", listOfNotNull(xFilter.q.valueOrNull?.let { "q,$it" }, xFilter.limit?.let { "limit,$it" }).joinToString(","))
        }
        if (filterCookie != null) {
          filterCookie.q.valueOrNull?.let { filterCookieQValue ->
            cookie("q", filterCookieQValue)
          }
          if (filterCookie.limit != null) {
            cookie("limit", filterCookie.limit.toString())
          }
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
