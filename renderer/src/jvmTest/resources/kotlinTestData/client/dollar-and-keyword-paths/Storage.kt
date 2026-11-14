package io.github.nomisrev.render.test.client.dollar.and.keyword.paths

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Storage internal constructor(
  private val client: HttpClient,
) {
  public fun `object`(`object`: String): ObjectPath = ObjectPath(client, `object`)

  public class ObjectPath internal constructor(
    private val client: HttpClient,
    private val `object`: String,
  ) {
    public val `get`: Get = Get(client, `object`)

    public class Get internal constructor(
      private val client: HttpClient,
      private val `object`: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("storage/${this.`object`.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
