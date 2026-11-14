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
    public suspend operator fun invoke(body: CreateConversationBody? = null): ConversationResource {
      val response = client.post("conversations") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
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
      public suspend operator fun invoke(): DeletedConversationResource {
        val response = client.delete("conversations/${conversationId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public suspend operator fun invoke(): ConversationResource {
        val response = client.get("conversations/${conversationId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val conversationId: String,
    ) {
      public suspend operator fun invoke(body: UpdateConversationBody? = null): ConversationResource {
        val response = client.post("conversations/${conversationId.encodeURLParameter()}") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
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
        ): ConversationItemList {
          val response = client.get("conversations/${conversationId.encodeURLParameter()}/items") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
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
        private val conversationId: String,
      ) {
        public suspend operator fun invoke(items: List<InputItem>, include: List<IncludeEnum>? = null): ConversationItemList {
          val response = client.post("conversations/${conversationId.encodeURLParameter()}/items") {
            include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
            contentType(ContentType.Application.Json)
            setBody(Body(items = items))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        internal data class Body(
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
          public suspend operator fun invoke(): ConversationResource {
            val response = client.delete("conversations/${conversationId.encodeURLParameter()}/items/${itemId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val conversationId: String,
          private val itemId: String,
        ) {
          public suspend operator fun invoke(include: List<IncludeEnum>? = null): ConversationItem {
            val response = client.get("conversations/${conversationId.encodeURLParameter()}/items/${itemId.encodeURLParameter()}") {
              include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }
  }
}
