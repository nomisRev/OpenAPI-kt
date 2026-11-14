package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Nasa internal constructor(
  private val client: HttpClient,
) {
  public fun idFormat(id: String, format: String): IdFormatPath = IdFormatPath(client, id, format)

  public class IdFormatPath internal constructor(
    private val client: HttpClient,
    private val id: String,
    private val format: String,
  ) {
    public val `get`: Get = Get(client, id, format)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
      private val format: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("nasa/${this.id.encodeURLParameter()}${this.format.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
