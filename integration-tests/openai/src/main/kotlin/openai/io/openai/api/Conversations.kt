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
import io.openai.model.ConversationItem
import io.openai.model.ConversationItemList
import io.openai.model.ConversationResource
import io.openai.model.CreateConversationBody
import io.openai.model.DeletedConversationResource
import io.openai.model.IncludeEnum
import io.openai.model.InputItem
import io.openai.model.UpdateConversationBody
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Conversations internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public fun conversationId(conversationId: String): ConversationIdPath = ConversationIdPath(client, conversationId)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateConversationBody? = null): ConversationResource = client.post("/conversations") {
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
  }

  public class ConversationIdPath internal constructor(
    private val client: HttpClient,
    private val conversationId: String,
  ) {
    public val delete: Delete = Delete(client, conversationId)

    public val `get`: Get = Get(client, conversationId)

    public val post: Post = Post(client, conversationId)

    public val items: Items = Items(client, conversationId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public suspend operator fun invoke(): DeletedConversationResource = client.delete("/conversations/$conversationId").body()
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public suspend operator fun invoke(): ConversationResource = client.get("/conversations/$conversationId").body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public suspend operator fun invoke(body: UpdateConversationBody? = null): ConversationResource = client.post("/conversations/$conversationId") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }

    public class Items internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public val `get`: Get = Get(client, conversationId)

      public val post: Post = Post(client, conversationId)

      public fun itemId(itemId: String): ItemIdPath = ItemIdPath(client, conversationId, itemId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val conversationId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = null,
          after: String? = null,
          include: List<IncludeEnum>? = null,
        ): ConversationItemList = client.get("/conversations/$conversationId/items") {
          limit?.let { parameter("limit", it) }
          order?.let { parameter("order", it.value) }
          after?.let { parameter("after", it) }
          include?.let { parameter("include", it) }
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
        private val conversationId: String,
      ) {
        public suspend operator fun invoke(items: List<InputItem>, include: List<IncludeEnum>? = null): ConversationItemList = client.post("/conversations/$conversationId/items") {
          include?.let { parameter("include", it) }
          contentType(ContentType.Application.Json)
          setBody(Body(items = items))
        }.body()

        @JvmInline
        @Serializable
        internal value class Body(
          public val items: List<InputItem>,
        )
      }

      public class ItemIdPath internal constructor(
        private val client: HttpClient,
        private val conversationId: String,
        private val itemId: String,
      ) {
        public val delete: Delete = Delete(client, conversationId, itemId)

        public val `get`: Get = Get(client, conversationId, itemId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val conversationId: String,
          private val itemId: String,
        ) {
          public suspend operator fun invoke(): ConversationResource = client.delete("/conversations/$conversationId/items/$itemId").body()
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val conversationId: String,
          private val itemId: String,
        ) {
          public suspend operator fun invoke(include: List<IncludeEnum>? = null): ConversationItem = client.get("/conversations/$conversationId/items/$itemId") {
            include?.let { parameter("include", it) }
          }.body()
        }
      }
    }
  }
}
