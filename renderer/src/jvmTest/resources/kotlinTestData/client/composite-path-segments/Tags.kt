package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Tags internal constructor(
  private val client: HttpClient,
) {
  public fun arn(arn: String): ArnPath = ArnPath(client, arn)

  public class ArnPath internal constructor(
    private val client: HttpClient,
    private val arn: String,
  ) {
    public val `get`: Get = Get(client, arn)

    public val tagKeys: TagKeys = TagKeys(client, arn)

    public class Get internal constructor(
      private val client: HttpClient,
      private val arn: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("tags/${this.arn.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class TagKeys internal constructor(
      private val client: HttpClient,
      private val arn: String,
    ) {
      public val delete: Delete = Delete(client, arn)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val arn: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.delete("tags/${this.arn.encodeURLParameter()}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
