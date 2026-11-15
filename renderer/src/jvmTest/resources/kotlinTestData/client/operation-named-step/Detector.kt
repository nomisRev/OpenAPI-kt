package io.github.nomisrev.render.test.client.operation.named.step

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Detector internal constructor(
  private val __client: HttpClient,
) {
  public fun detectorId(detectorId: String): DetectorIdPath = DetectorIdPath(__client, detectorId)

  public class DetectorIdPath internal constructor(
    private val __client: HttpClient,
    private val _detectorId: String,
  ) {
    public val member: Member = Member(__client, _detectorId)

    public class Member internal constructor(
      private val __client: HttpClient,
      private val _detectorId: String,
    ) {
      public val `get`: Get = Get(__client, _detectorId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _detectorId: String,
      ) {
        public val `get`: Get = Get(__client, _detectorId)

        public val post: Post = Post(__client, _detectorId)

        public suspend operator fun invoke() {
          val response = __client.get("detector/${this._detectorId.encodeURLParameter()}/member")
          if (response.status.value != 204) throw ResponseException(response, "")
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _detectorId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("detector/${this._detectorId.encodeURLParameter()}/member/get")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _detectorId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.post("detector/${this._detectorId.encodeURLParameter()}/member/get")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
