package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Detector internal constructor(
  private val client: HttpClient,
) {
  public fun detectorId(detectorId: String): DetectorIdPath = DetectorIdPath(client, detectorId)

  public class DetectorIdPath internal constructor(
    private val client: HttpClient,
    private val detectorId: String,
  ) {
    public val member: Member = Member(client, detectorId)

    public class Member internal constructor(
      private val client: HttpClient,
      private val detectorId: String,
    ) {
      public val `get`: Get = Get(client, detectorId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val detectorId: String,
      ) {
        public val `get`: Get = Get(client, detectorId)

        public val post: Post = Post(client, detectorId)

        public suspend operator fun invoke() {
          val response = client.get("detector/${this.detectorId.encodeURLParameter()}/member")
          if (response.status.value != 204) throw ResponseException(response, "")
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val detectorId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("detector/${this.detectorId.encodeURLParameter()}/member/get")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val detectorId: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.post("detector/${this.detectorId.encodeURLParameter()}/member/get")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
