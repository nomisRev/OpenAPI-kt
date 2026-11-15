package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Nasa internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public fun format(format: String): FormatPath = FormatPath(__client, _id, format)

    public class FormatPath internal constructor(
      private val __client: HttpClient,
      private val _id: String,
      private val _format: String,
    ) {
      public val `get`: Get = Get(__client, _id, _format)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _format: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("nasa/${this._id.encodeURLParameter()}${this._format.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
