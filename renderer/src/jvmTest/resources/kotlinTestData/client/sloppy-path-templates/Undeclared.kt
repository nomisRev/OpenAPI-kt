package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Undeclared internal constructor(
  private val client: HttpClient,
) {
  public fun nameJson(name: String): NameJsonPath = NameJsonPath(client, name)

  public class NameJsonPath internal constructor(
    private val client: HttpClient,
    private val name: String,
  ) {
    public val `get`: Get = Get(client, name)

    public class Get internal constructor(
      private val client: HttpClient,
      private val name: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("undeclared/${this.name.encodeURLParameter()}.json")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
