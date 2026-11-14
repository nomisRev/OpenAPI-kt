package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Media internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public fun extension(extension: String): ExtensionPath = ExtensionPath(client, id, extension)

    public class ExtensionPath internal constructor(
      private val client: HttpClient,
      private val id: String,
      private val extension: String,
    ) {
      public val `get`: Get = Get(client, id, extension)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
        private val extension: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("media/${this.id.encodeURLParameter()}.${this.extension.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
