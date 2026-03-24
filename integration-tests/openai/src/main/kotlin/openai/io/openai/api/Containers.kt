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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.ContainerFileListResource
import io.openai.model.ContainerFileResource
import io.openai.model.ContainerListResource
import io.openai.model.ContainerResource
import io.openai.model.CreateContainerBody
import io.openai.model.CreateContainerFileBody
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Containers internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun containerId(containerId: String): ContainerIdPath = ContainerIdPath(client, containerId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Long? = 20L,
      order: Order? = Order.Desc,
      after: String? = null,
      name: String? = null,
    ): ContainerListResource = client.get("/containers") {
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      after?.let { parameter("after", it) }
      name?.let { parameter("name", it) }
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
    public suspend operator fun invoke(body: CreateContainerBody? = null): ContainerResource = client.post("/containers") {
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
  }

  public class ContainerIdPath internal constructor(
    private val client: HttpClient,
    private val containerId: String,
  ) {
    public val delete: Delete = Delete(client, containerId)

    public val `get`: Get = Get(client, containerId)

    public val files: Files = Files(client, containerId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val containerId: String,
    ) {
      public suspend operator fun invoke() {
        client.delete("/containers/$containerId")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val containerId: String,
    ) {
      public suspend operator fun invoke(): ContainerResource = client.get("/containers/$containerId").body()
    }

    public class Files internal constructor(
      private val client: HttpClient,
      private val containerId: String,
    ) {
      public val `get`: Get = Get(client, containerId)

      public val post: Post = Post(client, containerId)

      public fun fileId(fileId: String): FileIdPath = FileIdPath(client, containerId, fileId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val containerId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
        ): ContainerFileListResource = client.get("/containers/$containerId/files") {
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
        private val containerId: String,
      ) {
        public suspend operator fun invoke(body: CreateContainerFileBody): ContainerFileResource = client.post("/containers/$containerId/files") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()

        public suspend operator fun invoke(fileId: String? = null, `file`: ByteArray? = null): ContainerFileResource = client.post("/containers/$containerId/files") {
          setBody(MultiPartFormDataContent(formData {
            if (fileId != null) {
              append("file_id", fileId)
            }
            if (file != null) {
              append("file", file)
            }
          }))
        }.body()
      }

      public class FileIdPath internal constructor(
        private val client: HttpClient,
        private val containerId: String,
        private val fileId: String,
      ) {
        public val delete: Delete = Delete(client, containerId, fileId)

        public val `get`: Get = Get(client, containerId, fileId)

        public val content: Content = Content(client, containerId, fileId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val containerId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/containers/$containerId/files/$fileId")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val containerId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke(): ContainerFileResource = client.get("/containers/$containerId/files/$fileId").body()
        }

        public class Content internal constructor(
          private val client: HttpClient,
          private val containerId: String,
          private val fileId: String,
        ) {
          public val `get`: Get = Get(client, containerId, fileId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val containerId: String,
            private val fileId: String,
          ) {
            public suspend operator fun invoke() {
              client.get("/containers/$containerId/files/$fileId/content")
            }
          }
        }
      }
    }
  }
}
