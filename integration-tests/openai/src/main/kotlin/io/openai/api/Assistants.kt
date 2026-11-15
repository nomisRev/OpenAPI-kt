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
import io.openai.model.AssistantObject
import io.openai.model.CreateAssistantRequest
import io.openai.model.DeleteAssistantResponse
import io.openai.model.ListAssistantsResponse
import io.openai.model.ModifyAssistantRequest
import kotlin.Deprecated
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Assistants internal constructor(
  private val __client: HttpClient,
) {
  @Deprecated("Deprecated by the API provider")
  public val `get`: Get = Get(__client)

  @Deprecated("Deprecated by the API provider")
  public val post: Post = Post(__client)

  public fun assistantId(assistantId: String): AssistantIdPath = AssistantIdPath(__client, assistantId)

  @Deprecated("Deprecated by the API provider")
  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke(
      limit: Long? = 20L,
      order: Order? = Order.Desc,
      after: String? = null,
      before: String? = null,
    ): ListAssistantsResponse {
      val response = __client.get("assistants") {
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          this.url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (before != null) {
          this.url.encodedParameters.append("before", before.encodeURLParameter())
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

  @Deprecated("Deprecated by the API provider")
  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke(body: CreateAssistantRequest): AssistantObject {
      val response = __client.post("assistants") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class AssistantIdPath internal constructor(
    private val __client: HttpClient,
    private val _assistantId: String,
  ) {
    @Deprecated("Deprecated by the API provider")
    public val delete: Delete = Delete(__client, _assistantId)

    @Deprecated("Deprecated by the API provider")
    public val `get`: Get = Get(__client, _assistantId)

    @Deprecated("Deprecated by the API provider")
    public val post: Post = Post(__client, _assistantId)

    @Deprecated("Deprecated by the API provider")
    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): DeleteAssistantResponse {
        val response = __client.delete("assistants/${this._assistantId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    @Deprecated("Deprecated by the API provider")
    public class Get internal constructor(
      private val __client: HttpClient,
      private val _assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): AssistantObject {
        val response = __client.get("assistants/${this._assistantId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    @Deprecated("Deprecated by the API provider")
    public class Post internal constructor(
      private val __client: HttpClient,
      private val _assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(body: ModifyAssistantRequest): AssistantObject {
        val response = __client.post("assistants/${this._assistantId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
