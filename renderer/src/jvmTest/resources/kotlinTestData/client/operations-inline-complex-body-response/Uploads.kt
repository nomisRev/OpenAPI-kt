package io.github.nomisrev.render.test.client.operations.`inline`.complex.body.response

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public interface Uploads {
  public fun uploadId(uploadId: String): UploadId

  public interface UploadId {
    public val post: Post

    public interface Post {
      public suspend operator fun invoke(
        `file`: ByteArray,
        checksum: String,
        retries: Int,
        tags: List<String>,
      ): Response

      public sealed interface Response {
        public data class Created(
          public val `value`: String,
        ) : Response

        public data class `Multi-status`(
          public val `value`: List<String>,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: Int,
        ) : Response

        public data class Default(
          public val status: HttpStatusCode,
          public val `value`: Boolean,
        ) : Response
      }
    }
  }
}

internal class KtorUploads(
  private val client: HttpClient,
) : Uploads {
  override fun uploadId(uploadId: String): Uploads.UploadId = KtorUploadsUploadId(client, uploadId)
}

internal class KtorUploadsUploadId(
  private val client: HttpClient,
  private val uploadId: String,
) : Uploads.UploadId {
  override val post: Uploads.UploadId.Post = object : Uploads.UploadId.Post {
    override suspend operator fun invoke(
      `file`: ByteArray,
      checksum: String,
      retries: Int,
      tags: List<String>,
    ): Uploads.UploadId.Post.Response {
      val response = client.post("/uploads/$uploadId") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          append("checksum", checksum)
          append("retries", retries)
          append("tags", tags)
        }))
      }
      return when (response.status.value) {
        201 -> Uploads.UploadId.Post.Response.Created(response.body())
        207 -> Uploads.UploadId.Post.Response.`Multi-status`(response.body())
        422 -> Uploads.UploadId.Post.Response.UnprocessableEntity(response.body())
        else -> Uploads.UploadId.Post.Response.Default(response.status, response.body())
      }
    }
  }
}
