package io.github.api

import io.github.model.BasicError
import io.github.model.Thread
import io.github.model.ThreadSubscription
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Notifications internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val put: Put = Put(client)

  public val threads: Threads = Threads(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      all: Boolean? = false,
      participating: Boolean? = false,
      since: Instant? = null,
      before: Instant? = null,
      page: Long? = 1L,
      perPage: Long? = 50L,
    ): Response {
      val response = client.get("/notifications") {
        all?.let { parameter("all", it) }
        participating?.let { parameter("participating", it) }
        since?.let { parameter("since", it.toString()) }
        before?.let { parameter("before", it.toString()) }
        page?.let { parameter("page", it) }
        perPage?.let { parameter("per_page", it) }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        401 -> Response.Unauthorized(response.body())
        403 -> Response.Forbidden(response.body())
        422 -> Response.UnprocessableEntity(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<Thread>,
      ) : Response

      public data object NotModified : Response

      public data class Unauthorized(
        public val `value`: BasicError,
      ) : Response

      public data class Forbidden(
        public val `value`: BasicError,
      ) : Response

      public data class UnprocessableEntity(
        public val `value`: ValidationError,
      ) : Response
    }
  }

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(lastReadAt: Instant? = null, read: Boolean? = null): Response {
      val response = client.put("/notifications") {
        if (lastReadAt != null || read != null) {
          contentType(ContentType.Application.Json)
          setBody(Body(lastReadAt = lastReadAt, read = read))
        }
      }
      return when (response.status.value) {
        202 -> response.body<Response.Accepted>()
        205 -> Response.ResetContent
        304 -> Response.NotModified
        401 -> Response.Unauthorized(response.body())
        403 -> Response.Forbidden(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    internal data class Body(
      @SerialName("last_read_at")
      public val lastReadAt: Instant? = null,
      public val read: Boolean? = null,
    )

    public sealed interface Response {
      @JvmInline
      @Serializable
      public value class Accepted(
        public val message: String? = null,
      ) : Response

      public data object ResetContent : Response

      public data object NotModified : Response

      public data class Unauthorized(
        public val `value`: BasicError,
      ) : Response

      public data class Forbidden(
        public val `value`: BasicError,
      ) : Response
    }
  }

  public class Threads internal constructor(
    private val client: HttpClient,
  ) {
    public fun threadId(threadId: Long): ThreadIdPath = ThreadIdPath(client, threadId)

    public class ThreadIdPath internal constructor(
      private val client: HttpClient,
      private val threadId: Long,
    ) {
      public val delete: Delete = Delete(client, threadId)

      public val `get`: Get = Get(client, threadId)

      public val patch: Patch = Patch(client, threadId)

      public val subscription: Subscription = Subscription(client, threadId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val threadId: Long,
      ) {
        public suspend operator fun invoke() {
          client.delete("/notifications/threads/$threadId")
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val threadId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/notifications/threads/$threadId")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Thread,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Patch internal constructor(
        private val client: HttpClient,
        private val threadId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.patch("/notifications/threads/$threadId")
          return when (response.status.value) {
            205 -> Response.ResetContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object ResetContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Subscription internal constructor(
        private val client: HttpClient,
        private val threadId: Long,
      ) {
        public val delete: Delete = Delete(client, threadId)

        public val `get`: Get = Get(client, threadId)

        public val put: Put = Put(client, threadId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val threadId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/notifications/threads/$threadId/subscription")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotModified : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val threadId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/notifications/threads/$threadId/subscription")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ThreadSubscription,
            ) : Response

            public data object NotModified : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val threadId: Long,
        ) {
          public suspend operator fun invoke(ignored: Boolean? = null): Response {
            val response = client.put("/notifications/threads/$threadId/subscription") {
              if (ignored != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(ignored = ignored))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val ignored: Boolean? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: ThreadSubscription,
            ) : Response

            public data object NotModified : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }
  }
}
