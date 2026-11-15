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
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public fun containerId(containerId: String): ContainerIdPath = ContainerIdPath(__client, containerId)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Long? = 20L,
      order: Order? = Order.Desc,
      after: String? = null,
      name: String? = null,
    ): ContainerListResource {
      val response = __client.get("containers") {
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          this.url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (name != null) {
          this.url.encodedParameters.append("name", name.encodeURLParameter())
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
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateContainerBody? = null): ContainerResource {
      val response = __client.post("containers") {
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
    private val __client: HttpClient,
    private val _containerId: String,
  ) {
    public val delete: Delete = Delete(__client, _containerId)

    public val `get`: Get = Get(__client, _containerId)

    public val files: Files = Files(__client, _containerId)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _containerId: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.delete("containers/${this._containerId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _containerId: String,
    ) {
      public suspend operator fun invoke(): ContainerResource {
        val response = __client.get("containers/${this._containerId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Files internal constructor(
      private val __client: HttpClient,
      private val _containerId: String,
    ) {
      public val `get`: Get = Get(__client, _containerId)

      public val post: Post = Post(__client, _containerId)

      public fun fileId(fileId: String): FileIdPath = FileIdPath(__client, _containerId, fileId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _containerId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
        ): ContainerFileListResource {
          val response = __client.get("containers/${this._containerId.encodeURLParameter()}/files") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
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
        private val __client: HttpClient,
        private val _containerId: String,
      ) {
        public suspend operator fun invoke(body: CreateContainerFileBody): ContainerFileResource {
          val response = __client.post("containers/${this._containerId.encodeURLParameter()}/files") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(fileId: String? = null, `file`: ByteArray? = null): ContainerFileResource {
          val response = __client.post("containers/${this._containerId.encodeURLParameter()}/files") {
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
        private val __client: HttpClient,
        private val _containerId: String,
        private val _fileId: String,
      ) {
        public val delete: Delete = Delete(__client, _containerId, _fileId)

        public val `get`: Get = Get(__client, _containerId, _fileId)

        public val content: Content = Content(__client, _containerId, _fileId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _containerId: String,
          private val _fileId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("containers/${this._containerId.encodeURLParameter()}/files/${this._fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _containerId: String,
          private val _fileId: String,
        ) {
          public suspend operator fun invoke(): ContainerFileResource {
            val response = __client.get("containers/${this._containerId.encodeURLParameter()}/files/${this._fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Content internal constructor(
          private val __client: HttpClient,
          private val _containerId: String,
          private val _fileId: String,
        ) {
          public val `get`: Get = Get(__client, _containerId, _fileId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _containerId: String,
            private val _fileId: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.get("containers/${this._containerId.encodeURLParameter()}/files/${this._fileId.encodeURLParameter()}/content")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }
        }
      }
    }
  }
}
