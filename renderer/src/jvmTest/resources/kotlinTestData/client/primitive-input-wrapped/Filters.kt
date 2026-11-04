package io.github.nomisrev.render.test.client.primitive.input.wrapped

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter

public class Filters internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(state: State? = null, selector: Selector? = null) {
      val response = client.get("/filters") {
        if (state != null) {
          url.encodedParameters.append("state", state.value.encodeURLParameter())
        }
        if (selector != null) {
          when (selector) {
            is Selector.CaseString -> {
              url.encodedParameters.append("selector", selector.value.encodeURLParameter())
            }
            is Selector.CaseInt -> {
              url.encodedParameters.append("selector", selector.value.toString())
            }
          }
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }
}
