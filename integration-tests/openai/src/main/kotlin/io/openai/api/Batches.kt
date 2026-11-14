package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.openai.model.Batch
import io.openai.model.BatchFileExpirationAfter
import io.openai.model.ListBatchesResponse
import io.openai.model.Metadata
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Batches internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun batchId(batchId: String): BatchIdPath = BatchIdPath(client, batchId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(after: String? = null, limit: Long? = 20L): ListBatchesResponse {
      val response = client.get("batches") {
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      inputFileId: String,
      endpoint: Endpoint,
      completionWindow: CompletionWindow,
      metadata: Metadata? = null,
      outputExpiresAfter: BatchFileExpirationAfter? = null,
    ): Batch {
      val response = client.post("batches") {
        contentType(ContentType.Application.Json)
        setBody(Body(inputFileId = inputFileId, endpoint = endpoint, completionWindow = completionWindow, metadata = metadata, outputExpiresAfter = outputExpiresAfter))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    @Serializable
    public enum class Endpoint(
      public val `value`: String,
    ) {
      @SerialName("/v1/responses")
      V1Responses("/v1/responses"),
      @SerialName("/v1/chat/completions")
      V1ChatCompletions("/v1/chat/completions"),
      @SerialName("/v1/embeddings")
      V1Embeddings("/v1/embeddings"),
      @SerialName("/v1/completions")
      V1Completions("/v1/completions"),
      @SerialName("/v1/moderations")
      V1Moderations("/v1/moderations"),
      @SerialName("/v1/images/generations")
      V1ImagesGenerations("/v1/images/generations"),
      @SerialName("/v1/images/edits")
      V1ImagesEdits("/v1/images/edits"),
      ;
    }

    @Serializable
    public enum class CompletionWindow {
      `24h`,
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      @SerialName("input_file_id")
      public val inputFileId: String,
      public val endpoint: Endpoint,
      @SerialName("completion_window")
      public val completionWindow: CompletionWindow,
      public val metadata: Metadata? = null,
      @SerialName("output_expires_after")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val outputExpiresAfter: BatchFileExpirationAfter? = null,
    )
  }

  public class BatchIdPath internal constructor(
    private val client: HttpClient,
    private val batchId: String,
  ) {
    public val `get`: Get = Get(client, batchId)

    public val cancel: Cancel = Cancel(client, batchId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val batchId: String,
    ) {
      public suspend operator fun invoke(): Batch {
        val response = client.get("batches/${batchId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Cancel internal constructor(
      private val client: HttpClient,
      private val batchId: String,
    ) {
      public val post: Post = Post(client, batchId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val batchId: String,
      ) {
        public suspend operator fun invoke(): Batch {
          val response = client.post("batches/${batchId.encodeURLParameter()}/cancel")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
