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

public class Uploads internal constructor(
  private val client: HttpClient,
) {
  public fun uploadId(uploadId: String): UploadIdPath = UploadIdPath(client, uploadId)

  public class UploadIdPath internal constructor(
    private val client: HttpClient,
    private val uploadId: String,
  ) {
    public val post: Post = Post(client, uploadId)

    public class Post internal constructor(
      private val client: HttpClient,
      private val uploadId: String,
    ) {
      public suspend operator fun invoke(
        `file`: ByteArray,
        checksum: String,
        retries: Int,
        tags: List<String>,
      ): Response {
        val response = client.post("/uploads/$uploadId") {
          setBody(MultiPartFormDataContent(formData {
            append("file", file)
            append("checksum", checksum)
            append("retries", retries)
            append("tags", tags)
          }))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          207 -> Response.`Multi-status`(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> Response.Default(response.status, response.body())
        }
      }

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
