package io.github.nomisrev.render.test.client.sloppy.path.templates

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Tags internal constructor(
  private val __client: HttpClient,
) {
  public fun arn(arn: String): ArnPath = ArnPath(__client, arn)

  public class ArnPath internal constructor(
    private val __client: HttpClient,
    private val _arn: String,
  ) {
    public val `get`: Get = Get(__client, _arn)

    public val tagKeys: TagKeys = TagKeys(__client, _arn)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _arn: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("tags/${this._arn.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class TagKeys internal constructor(
      private val __client: HttpClient,
      private val _arn: String,
    ) {
      public val delete: Delete = Delete(__client, _arn)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _arn: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.delete("tags/${this._arn.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
