package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.collections.List

public class MatrixExploded internal constructor(
  private val __client: HttpClient,
) {
  public fun ids(ids: List<Int>): IdsPath = IdsPath(__client, ids)

  public class IdsPath internal constructor(
    private val __client: HttpClient,
    private val _ids: List<Int>,
  ) {
    public val `get`: Get = Get(__client, _ids)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _ids: List<Int>,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("matrix-exploded/${this._ids.joinToString("") { ";ids=$it" }}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
