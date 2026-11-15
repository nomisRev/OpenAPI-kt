package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Notes internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public val text: Text = Text(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("notes/${this.id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Text internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("notes/${this.id.encodeURLParameter()}/text")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
