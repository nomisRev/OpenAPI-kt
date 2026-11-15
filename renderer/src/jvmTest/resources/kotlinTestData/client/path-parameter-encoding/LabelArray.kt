package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List

public class LabelArray internal constructor(
  private val __client: HttpClient,
) {
  public fun ids(ids: List<String>): IdsPath = IdsPath(__client, ids)

  public class IdsPath internal constructor(
    private val __client: HttpClient,
    private val _ids: List<String>,
  ) {
    public val `get`: Get = Get(__client, _ids)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _ids: List<String>,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("label-array/${if (this._ids.isEmpty()) "" else ".${this._ids.joinToString(",") { it.encodeURLParameter() }}"}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
