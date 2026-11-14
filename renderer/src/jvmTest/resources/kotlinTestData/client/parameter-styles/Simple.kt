package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.collections.List

public class Simple internal constructor(
  private val client: HttpClient,
) {
  public fun ids(ids: List<Status>): IdsPath = IdsPath(client, ids)

  public class IdsPath internal constructor(
    private val client: HttpClient,
    private val ids: List<Status>,
  ) {
    public val `get`: Get = Get(client, ids)

    public class Get internal constructor(
      private val client: HttpClient,
      private val ids: List<Status>,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("simple/${this.ids.joinToString(",") { it.value.encodeURLParameter() }}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
