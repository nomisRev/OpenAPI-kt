package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Media internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public fun extension(extension: String): ExtensionPath = ExtensionPath(__client, _id, extension)

    public class ExtensionPath internal constructor(
      private val __client: HttpClient,
      private val _id: String,
      private val _extension: String,
    ) {
      public val `get`: Get = Get(__client, _id, _extension)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
        private val _extension: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("media/${this._id.encodeURLParameter()}.${this._extension.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
