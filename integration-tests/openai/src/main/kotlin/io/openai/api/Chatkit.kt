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
import io.openai.model.ChatSessionResource
import io.openai.model.CreateChatSessionBody
import io.openai.model.DeletedThreadResource
import io.openai.model.OrderEnum
import io.openai.model.ThreadItemListResource
import io.openai.model.ThreadListResource
import io.openai.model.ThreadResource
import kotlin.Long
import kotlin.String

public class Chatkit internal constructor(
  private val __client: HttpClient,
) {
  public val sessions: Sessions = Sessions(__client)

  public val threads: Threads = Threads(__client)

  public class Sessions internal constructor(
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public fun sessionId(sessionId: String): SessionIdPath = SessionIdPath(__client, sessionId)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateChatSessionBody? = null): ChatSessionResource {
        val response = __client.post("chatkit/sessions") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class SessionIdPath internal constructor(
      private val __client: HttpClient,
      private val _sessionId: String,
    ) {
      public val cancel: Cancel = Cancel(__client, _sessionId)

      public class Cancel internal constructor(
        private val __client: HttpClient,
        private val _sessionId: String,
      ) {
        public val post: Post = Post(__client, _sessionId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _sessionId: String,
        ) {
          public suspend operator fun invoke(): ChatSessionResource {
            val response = __client.post("chatkit/sessions/${this._sessionId.encodeURLParameter()}/cancel")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }
  }

  public class Threads internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public fun threadId(threadId: String): ThreadIdPath = ThreadIdPath(__client, threadId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = null,
        order: OrderEnum? = null,
        after: String? = null,
        before: String? = null,
        user: String? = null,
      ): ThreadListResource {
        val response = __client.get("chatkit/threads") {
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
          if (user != null) {
            this.url.encodedParameters.append("user", user.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class ThreadIdPath internal constructor(
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public val delete: Delete = Delete(__client, _threadId)

      public val `get`: Get = Get(__client, _threadId)

      public val items: Items = Items(__client, _threadId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(): DeletedThreadResource {
          val response = __client.delete("chatkit/threads/${this._threadId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(): ThreadResource {
          val response = __client.get("chatkit/threads/${this._threadId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Items internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public val `get`: Get = Get(__client, _threadId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            order: OrderEnum? = null,
            after: String? = null,
            before: String? = null,
          ): ThreadItemListResource {
            val response = __client.get("chatkit/threads/${this._threadId.encodeURLParameter()}/items") {
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
        }
      }
    }
  }
}
