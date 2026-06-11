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
  private val client: HttpClient,
) {
  public val sessions: Sessions = Sessions(client)

  public val threads: Threads = Threads(client)

  public class Sessions internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public fun sessionId(sessionId: String): SessionIdPath = SessionIdPath(client, sessionId)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateChatSessionBody? = null): ChatSessionResource = client.post("/chatkit/sessions") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()
    }

    public class SessionIdPath internal constructor(
      private val client: HttpClient,
      private val sessionId: String,
    ) {
      public val cancel: Cancel = Cancel(client, sessionId)

      public class Cancel internal constructor(
        private val client: HttpClient,
        private val sessionId: String,
      ) {
        public val post: Post = Post(client, sessionId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val sessionId: String,
        ) {
          public suspend operator fun invoke(): ChatSessionResource = client.post("/chatkit/sessions/$sessionId/cancel").body()
        }
      }
    }
  }

  public class Threads internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun threadId(threadId: String): ThreadIdPath = ThreadIdPath(client, threadId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = null,
        order: OrderEnum? = null,
        after: String? = null,
        before: String? = null,
        user: String? = null,
      ): ThreadListResource = client.get("/chatkit/threads") {
        limit?.let { parameter("limit", it) }
        order?.let { parameter("order", it.value) }
        after?.let { parameter("after", it) }
        before?.let { parameter("before", it) }
        user?.let { parameter("user", it) }
      }.body()
    }

    public class ThreadIdPath internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public val delete: Delete = Delete(client, threadId)

      public val `get`: Get = Get(client, threadId)

      public val items: Items = Items(client, threadId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(): DeletedThreadResource = client.delete("/chatkit/threads/$threadId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(): ThreadResource = client.get("/chatkit/threads/$threadId").body()
      }

      public class Items internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public val `get`: Get = Get(client, threadId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val threadId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            order: OrderEnum? = null,
            after: String? = null,
            before: String? = null,
          ): ThreadItemListResource = client.get("/chatkit/threads/$threadId/items") {
            limit?.let { parameter("limit", it) }
            order?.let { parameter("order", it.value) }
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
          }.body()
        }
      }
    }
  }
}
