package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Storage internal constructor(
  private val __client: HttpClient,
) {
  public fun `object`(`object`: String): ObjectPath = ObjectPath(__client, `object`)

  public class ObjectPath internal constructor(
    private val __client: HttpClient,
    private val _object: String,
  ) {
    public val `get`: Get = Get(__client, _object)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _object: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("storage/${this._object.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
