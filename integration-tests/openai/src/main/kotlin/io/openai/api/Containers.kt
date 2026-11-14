package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
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
    ): ContainerListResource {
      val response = client.get("containers") {
        if (limit != null) {
          url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (name != null) {
          url.encodedParameters.append("name", name.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

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
    public suspend operator fun invoke(body: CreateContainerBody? = null): ContainerResource {
      val response = client.post("containers") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
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
        val response = client.delete("containers/${containerId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val containerId: String,
    ) {
      public suspend operator fun invoke(): ContainerResource {
        val response = client.get("containers/${containerId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
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
        ): ContainerFileListResource {
          val response = client.get("containers/${containerId.encodeURLParameter()}/files") {
            if (limit != null) {
              url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              url.encodedParameters.append("after", after.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
        public suspend operator fun invoke(body: CreateContainerFileBody): ContainerFileResource {
          val response = client.post("containers/${containerId.encodeURLParameter()}/files") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(fileId: String? = null, `file`: ByteArray? = null): ContainerFileResource {
          val response = client.post("containers/${containerId.encodeURLParameter()}/files") {
            setBody(MultiPartFormDataContent(formData {
              if (fileId != null) {
                append("file_id", fileId)
              }
              if (file != null) {
                append("file", file)
              }
            }))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
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
            val response = client.delete("containers/${containerId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val containerId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke(): ContainerFileResource {
            val response = client.get("containers/${containerId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
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
              val response = client.get("containers/${containerId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}/content")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }
        }
      }
    }
  }
}
