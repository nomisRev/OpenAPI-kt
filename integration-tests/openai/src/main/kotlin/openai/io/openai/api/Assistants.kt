package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
  private val client: HttpClient,
) {
  @Deprecated("Deprecated by the API provider")
  public val `get`: Get = Get(client)

  @Deprecated("Deprecated by the API provider")
  public val post: Post = Post(client)

  public fun assistantId(assistantId: String): AssistantIdPath = AssistantIdPath(client, assistantId)

  @Deprecated("Deprecated by the API provider")
  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke(
      limit: Long? = 20L,
      order: Order? = Order.Desc,
      after: String? = null,
      before: String? = null,
    ): ListAssistantsResponse = client.get("/assistants") {
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      after?.let { parameter("after", it) }
      before?.let { parameter("before", it) }
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

  @Deprecated("Deprecated by the API provider")
  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    @Deprecated("Deprecated by the API provider")
    public suspend operator fun invoke(body: CreateAssistantRequest): AssistantObject = client.post("/assistants") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
  }

  public class AssistantIdPath internal constructor(
    private val client: HttpClient,
    private val assistantId: String,
  ) {
    @Deprecated("Deprecated by the API provider")
    public val delete: Delete = Delete(client, assistantId)

    @Deprecated("Deprecated by the API provider")
    public val `get`: Get = Get(client, assistantId)

    @Deprecated("Deprecated by the API provider")
    public val post: Post = Post(client, assistantId)

    @Deprecated("Deprecated by the API provider")
    public class Delete internal constructor(
      private val client: HttpClient,
      private val assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): DeleteAssistantResponse = client.delete("/assistants/$assistantId").body()
    }

    @Deprecated("Deprecated by the API provider")
    public class Get internal constructor(
      private val client: HttpClient,
      private val assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): AssistantObject = client.get("/assistants/$assistantId").body()
    }

    @Deprecated("Deprecated by the API provider")
    public class Post internal constructor(
      private val client: HttpClient,
      private val assistantId: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(body: ModifyAssistantRequest): AssistantObject = client.post("/assistants/$assistantId") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }
  }
}
