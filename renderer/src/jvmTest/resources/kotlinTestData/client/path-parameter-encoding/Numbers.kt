package io.github.nomisrev.render.test.client.path.parameter.encoding

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Long

public class Numbers internal constructor(
  private val __client: HttpClient,
) {
  public fun n(n: Long): NPath = NPath(__client, n)

  public class NPath internal constructor(
    private val __client: HttpClient,
    private val _n: Long,
  ) {
    public val `get`: Get = Get(__client, _n)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _n: Long,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("numbers/${this._n}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
