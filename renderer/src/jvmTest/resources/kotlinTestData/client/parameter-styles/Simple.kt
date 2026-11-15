package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.collections.List

public class Simple internal constructor(
  private val __client: HttpClient,
) {
  public fun ids(ids: List<Status>): IdsPath = IdsPath(__client, ids)

  public class IdsPath internal constructor(
    private val __client: HttpClient,
    private val _ids: List<Status>,
  ) {
    public val `get`: Get = Get(__client, _ids)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _ids: List<Status>,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("simple/${this._ids.joinToString(",") { it.value.encodeURLParameter() }}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
