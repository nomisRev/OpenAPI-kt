package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.openai.model.CreateVectorStoreFileBatchRequest
import io.openai.model.CreateVectorStoreFileRequest
import io.openai.model.CreateVectorStoreRequest
import io.openai.model.DeleteVectorStoreFileResponse
import io.openai.model.DeleteVectorStoreResponse
import io.openai.model.ListVectorStoreFilesResponse
import io.openai.model.ListVectorStoresResponse
import io.openai.model.UpdateVectorStoreFileAttributesRequest
import io.openai.model.UpdateVectorStoreRequest
import io.openai.model.VectorStoreFileBatchObject
import io.openai.model.VectorStoreFileContentResponse
import io.openai.model.VectorStoreFileObject
import io.openai.model.VectorStoreObject
import io.openai.model.VectorStoreSearchRequest
import io.openai.model.VectorStoreSearchResultsPage
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class VectorStores internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun vectorStoreId(vectorStoreId: String): VectorStoreIdPath = VectorStoreIdPath(client, vectorStoreId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Long? = 20L,
      order: Order? = Order.Desc,
      after: String? = null,
      before: String? = null,
    ): ListVectorStoresResponse {
      val response = client.get("vector_stores") {
        if (limit != null) {
          url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (before != null) {
          url.encodedParameters.append("before", before.encodeURLParameter())
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
    public suspend operator fun invoke(body: CreateVectorStoreRequest): VectorStoreObject {
      val response = client.post("vector_stores") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class VectorStoreIdPath internal constructor(
    private val client: HttpClient,
    private val vectorStoreId: String,
  ) {
    public val delete: Delete = Delete(client, vectorStoreId)

    public val `get`: Get = Get(client, vectorStoreId)

    public val post: Post = Post(client, vectorStoreId)

    public val fileBatches: FileBatches = FileBatches(client, vectorStoreId)

    public val files: Files = Files(client, vectorStoreId)

    public val search: Search = Search(client, vectorStoreId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public suspend operator fun invoke(): DeleteVectorStoreResponse {
        val response = client.delete("vector_stores/${vectorStoreId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public suspend operator fun invoke(): VectorStoreObject {
        val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public suspend operator fun invoke(body: UpdateVectorStoreRequest): VectorStoreObject {
        val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class FileBatches internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public val post: Post = Post(client, vectorStoreId)

      public fun batchId(batchId: String): BatchIdPath = BatchIdPath(client, vectorStoreId, batchId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
      ) {
        public suspend operator fun invoke(body: CreateVectorStoreFileBatchRequest): VectorStoreFileBatchObject {
          val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}/file_batches") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class BatchIdPath internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
        private val batchId: String,
      ) {
        public val `get`: Get = Get(client, vectorStoreId, batchId)

        public val cancel: Cancel = Cancel(client, vectorStoreId, batchId)

        public val files: Files = Files(client, vectorStoreId, batchId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val batchId: String,
        ) {
          public suspend operator fun invoke(): VectorStoreFileBatchObject {
            val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}/file_batches/${batchId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Cancel internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val batchId: String,
        ) {
          public val post: Post = Post(client, vectorStoreId, batchId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val vectorStoreId: String,
            private val batchId: String,
          ) {
            public suspend operator fun invoke(): VectorStoreFileBatchObject {
              val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}/file_batches/${batchId.encodeURLParameter()}/cancel")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Files internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val batchId: String,
        ) {
          public val `get`: Get = Get(client, vectorStoreId, batchId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val vectorStoreId: String,
            private val batchId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = 20L,
              order: Order? = Order.Desc,
              after: String? = null,
              before: String? = null,
              filter: Filter? = null,
            ): ListVectorStoreFilesResponse {
              val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}/file_batches/${batchId.encodeURLParameter()}/files") {
                if (limit != null) {
                  url.encodedParameters.append("limit", limit.toString())
                }
                if (order != null) {
                  url.encodedParameters.append("order", order.value.encodeURLParameter())
                }
                if (after != null) {
                  url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (before != null) {
                  url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (filter != null) {
                  url.encodedParameters.append("filter", filter.value.encodeURLParameter())
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

            @Serializable
            public enum class Filter(
              public val `value`: String,
            ) {
              @SerialName("in_progress")
              InProgress("in_progress"),
              @SerialName("completed")
              Completed("completed"),
              @SerialName("failed")
              Failed("failed"),
              @SerialName("cancelled")
              Cancelled("cancelled"),
              ;
            }
          }
        }
      }
    }

    public class Files internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public val `get`: Get = Get(client, vectorStoreId)

      public val post: Post = Post(client, vectorStoreId)

      public fun fileId(fileId: String): FileIdPath = FileIdPath(client, vectorStoreId, fileId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
          before: String? = null,
          filter: Filter? = null,
        ): ListVectorStoreFilesResponse {
          val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}/files") {
            if (limit != null) {
              url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              url.encodedParameters.append("after", after.encodeURLParameter())
            }
            if (before != null) {
              url.encodedParameters.append("before", before.encodeURLParameter())
            }
            if (filter != null) {
              url.encodedParameters.append("filter", filter.value.encodeURLParameter())
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

        @Serializable
        public enum class Filter(
          public val `value`: String,
        ) {
          @SerialName("in_progress")
          InProgress("in_progress"),
          @SerialName("completed")
          Completed("completed"),
          @SerialName("failed")
          Failed("failed"),
          @SerialName("cancelled")
          Cancelled("cancelled"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
      ) {
        public suspend operator fun invoke(body: CreateVectorStoreFileRequest): VectorStoreFileObject {
          val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}/files") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class FileIdPath internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
        private val fileId: String,
      ) {
        public val delete: Delete = Delete(client, vectorStoreId, fileId)

        public val `get`: Get = Get(client, vectorStoreId, fileId)

        public val post: Post = Post(client, vectorStoreId, fileId)

        public val content: Content = Content(client, vectorStoreId, fileId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke(): DeleteVectorStoreFileResponse {
            val response = client.delete("vector_stores/${vectorStoreId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke(): VectorStoreFileObject {
            val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val fileId: String,
        ) {
          public suspend operator fun invoke(body: UpdateVectorStoreFileAttributesRequest): VectorStoreFileObject {
            val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Content internal constructor(
          private val client: HttpClient,
          private val vectorStoreId: String,
          private val fileId: String,
        ) {
          public val `get`: Get = Get(client, vectorStoreId, fileId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val vectorStoreId: String,
            private val fileId: String,
          ) {
            public suspend operator fun invoke(): VectorStoreFileContentResponse {
              val response = client.get("vector_stores/${vectorStoreId.encodeURLParameter()}/files/${fileId.encodeURLParameter()}/content")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }
    }

    public class Search internal constructor(
      private val client: HttpClient,
      private val vectorStoreId: String,
    ) {
      public val post: Post = Post(client, vectorStoreId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val vectorStoreId: String,
      ) {
        public suspend operator fun invoke(body: VectorStoreSearchRequest): VectorStoreSearchResultsPage {
          val response = client.post("vector_stores/${vectorStoreId.encodeURLParameter()}/search") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
