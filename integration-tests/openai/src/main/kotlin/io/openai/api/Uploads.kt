package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.openai.model.CompleteUploadRequest
import io.openai.model.CreateUploadRequest
import io.openai.model.Upload
import io.openai.model.UploadPart
import kotlin.ByteArray
import kotlin.String

public class Uploads internal constructor(
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public fun uploadId(uploadId: String): UploadIdPath = UploadIdPath(__client, uploadId)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateUploadRequest): Upload {
      val response = __client.post("uploads") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class UploadIdPath internal constructor(
    private val __client: HttpClient,
    private val _uploadId: String,
  ) {
    public val cancel: Cancel = Cancel(__client, _uploadId)

    public val complete: Complete = Complete(__client, _uploadId)

    public val parts: Parts = Parts(__client, _uploadId)

    public class Cancel internal constructor(
      private val __client: HttpClient,
      private val _uploadId: String,
    ) {
      public val post: Post = Post(__client, _uploadId)

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _uploadId: String,
      ) {
        public suspend operator fun invoke(): Upload {
          val response = __client.post("uploads/${this._uploadId.encodeURLParameter()}/cancel")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Complete internal constructor(
      private val __client: HttpClient,
      private val _uploadId: String,
    ) {
      public val post: Post = Post(__client, _uploadId)

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _uploadId: String,
      ) {
        public suspend operator fun invoke(body: CompleteUploadRequest): Upload {
          val response = __client.post("uploads/${this._uploadId.encodeURLParameter()}/complete") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Parts internal constructor(
      private val __client: HttpClient,
      private val _uploadId: String,
    ) {
      public val post: Post = Post(__client, _uploadId)

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _uploadId: String,
      ) {
        public suspend operator fun invoke(`data`: ByteArray): UploadPart {
          val response = __client.post("uploads/${this._uploadId.encodeURLParameter()}/parts") {
            setBody(MultiPartFormDataContent(formData {
              append("data", data)
            }))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
