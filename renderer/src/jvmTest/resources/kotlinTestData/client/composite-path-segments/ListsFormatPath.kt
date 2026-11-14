package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class ListsFormatPath internal constructor(
  private val client: HttpClient,
  private val format: String,
) {
  public val `get`: Get = Get(client, format)

  public class Get internal constructor(
    private val client: HttpClient,
    private val format: String,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("lists.${this.format.encodeURLParameter()}")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
