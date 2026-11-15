package io.github.nomisrev.render.test.client.shared.path.parameter.validation

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Items internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public val parts: Parts = Parts(client, id)

    public val tags: Tags = Tags(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("items/${this.id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Parts internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("items/${this.id.encodeURLParameter()}/parts")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Tags internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public val `get`: Get = Get(client, id)

      public class Get internal constructor(
        private val client: HttpClient,
        private val id: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("items/${this.id.encodeURLParameter()}/tags")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
