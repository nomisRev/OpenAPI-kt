package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.CompleteUploadRequest
import io.openai.model.CreateUploadRequest
import io.openai.model.Upload
import io.openai.model.UploadPart
import kotlin.ByteArray
import kotlin.String

public class Uploads internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public fun uploadId(uploadId: String): UploadIdPath = UploadIdPath(client, uploadId)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateUploadRequest): Upload = client.post("/uploads") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }

  public class UploadIdPath internal constructor(
    private val client: HttpClient,
    private val uploadId: String,
  ) {
    public val cancel: Cancel = Cancel(client, uploadId)

    public val complete: Complete = Complete(client, uploadId)

    public val parts: Parts = Parts(client, uploadId)

    public class Cancel internal constructor(
      private val client: HttpClient,
      private val uploadId: String,
    ) {
      public val post: Post = Post(client, uploadId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val uploadId: String,
      ) {
        public suspend operator fun invoke(): Upload = client.post("/uploads/$uploadId/cancel").body()
      }
    }

    public class Complete internal constructor(
      private val client: HttpClient,
      private val uploadId: String,
    ) {
      public val post: Post = Post(client, uploadId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val uploadId: String,
      ) {
        public suspend operator fun invoke(body: CompleteUploadRequest): Upload = client.post("/uploads/$uploadId/complete") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }

    public class Parts internal constructor(
      private val client: HttpClient,
      private val uploadId: String,
    ) {
      public val post: Post = Post(client, uploadId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val uploadId: String,
      ) {
        public suspend operator fun invoke(`data`: ByteArray): UploadPart = client.post("/uploads/$uploadId/parts") {
          setBody(MultiPartFormDataContent(formData {
            append("data", data)
          }))
        }.body()
      }
    }
  }
}
