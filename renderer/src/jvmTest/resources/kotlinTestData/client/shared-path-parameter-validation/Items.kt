package io.github.nomisrev.render.test.client.shared.path.parameter.validation

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Items internal constructor(
  private val __client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(__client, id)

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public val parts: Parts = Parts(__client, _id)

    public val tags: Tags = Tags(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("items/${this._id.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Parts internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("items/${this._id.encodeURLParameter()}/parts")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Tags internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val `get`: Get = Get(__client, _id)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("items/${this._id.encodeURLParameter()}/tags")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
