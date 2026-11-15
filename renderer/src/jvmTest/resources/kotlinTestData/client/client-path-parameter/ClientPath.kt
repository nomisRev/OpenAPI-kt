package io.github.nomisrev.render.test.client.client.path.parameter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class ClientPath internal constructor(
  private val __client: HttpClient,
  private val _client: String,
) {
  public val things: Things = Things(__client, _client)

  public class Things internal constructor(
    private val __client: HttpClient,
    private val _client: String,
  ) {
    public val `get`: Get = Get(__client, _client)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _client: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("${this._client.encodeURLParameter()}/things")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
