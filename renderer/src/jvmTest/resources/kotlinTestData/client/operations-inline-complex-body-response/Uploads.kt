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
    public suspend fun post(
      `file`: ByteArray,
      checksum: String,
      retries: Int,
      tags: List<String>,
    ): PostResult

    public sealed interface PostResult {
      public data class Created(
        public val `value`: String,
      ) : PostResult

      public data class `Multi-status`(
        public val `value`: List<String>,
      ) : PostResult

      public data class UnprocessableEntity(
        public val `value`: Int,
      ) : PostResult

      public data class Default(
        public val status: HttpStatusCode,
        public val `value`: Boolean,
      ) : PostResult
    }
  }
}

internal class KtorUploads(
  private val client: HttpClient,
) : Uploads {
  override fun uploadId(uploadId: String): Uploads.UploadId = KtorUploadId(client, uploadId)
}

internal class KtorUploadId(
  private val client: HttpClient,
  private val uploadId: String,
) : Uploads.UploadId {
  override suspend fun post(
    `file`: ByteArray,
    checksum: String,
    retries: Int,
    tags: List<String>,
  ): Uploads.UploadId.PostResult {
    val response = client.post("/uploads/$uploadId") {
      setBody(MultiPartFormDataContent(formData {
        append("file", file)
        append("checksum", checksum)
        append("retries", retries)
        append("tags", tags)
      }))
    }
    return when (response.status.value) {
      201 -> Uploads.UploadId.PostResult.Created(response.body())
      207 -> Uploads.UploadId.PostResult.`Multi-status`(response.body())
      422 -> Uploads.UploadId.PostResult.UnprocessableEntity(response.body())
      else -> Uploads.UploadId.PostResult.Default(response.status, response.body())
    }
  }
}
