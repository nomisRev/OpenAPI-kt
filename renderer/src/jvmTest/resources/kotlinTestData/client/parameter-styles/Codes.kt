package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Codes internal constructor(
  private val __client: HttpClient,
) {
  public fun code(code: String): CodePath = CodePath(__client, code)

  public class CodePath internal constructor(
    private val __client: HttpClient,
    private val _code: String,
  ) {
    public val `get`: Get = Get(__client, _code)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _code: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("codes/;code=${this._code.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
