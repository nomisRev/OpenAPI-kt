package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Undeclared internal constructor(
  private val __client: HttpClient,
) {
  public fun name(name: String): NamePath = NamePath(__client, name)

  public class NamePath internal constructor(
    private val __client: HttpClient,
    private val _name: String,
  ) {
    public val json: Json = Json(__client, _name)

    public class Json internal constructor(
      private val __client: HttpClient,
      private val _name: String,
    ) {
      public val `get`: Get = Get(__client, _name)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _name: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("undeclared/${this._name.encodeURLParameter()}.json")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
