package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.openai.model.CreateFileRequest
import io.openai.model.DeleteFileResponse
import io.openai.model.FileExpirationAfter
import io.openai.model.ListFilesResponse
import io.openai.model.OpenAIFile
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

public class Files internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun fileId(fileId: String): FileIdPath = FileIdPath(client, fileId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      purpose: String? = null,
      limit: Long? = 10_000L,
      order: Order? = Order.Desc,
      after: String? = null,
    ): ListFilesResponse = client.get("/files") {
      purpose?.let { parameter("purpose", it) }
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      after?.let { parameter("after", it) }
    }.body()

    @Serializable
    public enum class Order(
      public val `value`: String,
    ) {
      @SerialName("asc")
      Asc("asc"),
      @SerialName("desc")
      Desc("desc"),
      ;
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      `file`: ByteArray,
      purpose: CreateFileRequest.Purpose,
      expiresAfter: FileExpirationAfter? = null,
    ): OpenAIFile = client.post("/files") {
      setBody(MultiPartFormDataContent(formData {
        append("file", file)
        append("purpose", purpose.value)
        if (expiresAfter != null) {
          append("expires_after", Json.encodeToString(expiresAfter))
        }
      }))
    }.body()
  }

  public class FileIdPath internal constructor(
    private val client: HttpClient,
    private val fileId: String,
  ) {
    public val delete: Delete = Delete(client, fileId)

    public val `get`: Get = Get(client, fileId)

    public val content: Content = Content(client, fileId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val fileId: String,
    ) {
      public suspend operator fun invoke(): DeleteFileResponse = client.delete("/files/$fileId").body()
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val fileId: String,
    ) {
      public suspend operator fun invoke(): OpenAIFile = client.get("/files/$fileId").body()
    }

    public class Content internal constructor(
      private val client: HttpClient,
      private val fileId: String,
    ) {
      public val `get`: Get = Get(client, fileId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val fileId: String,
      ) {
        public suspend operator fun invoke(): String = client.get("/files/$fileId/content").body()
      }
    }
  }
}
