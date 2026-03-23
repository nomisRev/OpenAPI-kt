package io.github.api

import io.github.model.AppPermissions
import io.github.model.Authorization
import io.github.model.BasicError
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Applications internal constructor(
  private val client: HttpClient,
) {
  public fun clientId(clientId: String): ClientIdPath = ClientIdPath(client, clientId)

  public class ClientIdPath internal constructor(
    private val client: HttpClient,
    private val clientId: String,
  ) {
    public val grant: Grant = Grant(client, clientId)

    public val token: Token = Token(client, clientId)

    public class Grant internal constructor(
      private val client: HttpClient,
      private val clientId: String,
    ) {
      public val delete: Delete = Delete(client, clientId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val clientId: String,
      ) {
        public suspend operator fun invoke(accessToken: String): Response {
          val response = client.delete("/applications/$clientId/grant") {
            contentType(ContentType.Application.Json)
            setBody(Body(accessToken = accessToken))
          }
          return when (response.status.value) {
            204 -> Response.NoContent
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @JvmInline
        @Serializable
        internal value class Body(
          @SerialName("access_token")
          public val accessToken: String,
        )

        public sealed interface Response {
          public data object NoContent : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }

    public class Token internal constructor(
      private val client: HttpClient,
      private val clientId: String,
    ) {
      public val delete: Delete = Delete(client, clientId)

      public val patch: Patch = Patch(client, clientId)

      public val post: Post = Post(client, clientId)

      public val scoped: Scoped = Scoped(client, clientId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val clientId: String,
      ) {
        public suspend operator fun invoke(accessToken: String): Response {
          val response = client.delete("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(Body(accessToken = accessToken))
          }
          return when (response.status.value) {
            204 -> Response.NoContent
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @JvmInline
        @Serializable
        internal value class Body(
          @SerialName("access_token")
          public val accessToken: String,
        )

        public sealed interface Response {
          public data object NoContent : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class Patch internal constructor(
        private val client: HttpClient,
        private val clientId: String,
      ) {
        public suspend operator fun invoke(accessToken: String): Response {
          val response = client.patch("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(Body(accessToken = accessToken))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @JvmInline
        @Serializable
        internal value class Body(
          @SerialName("access_token")
          public val accessToken: String,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: Authorization,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val clientId: String,
      ) {
        public suspend operator fun invoke(accessToken: String): Response {
          val response = client.post("/applications/$clientId/token") {
            contentType(ContentType.Application.Json)
            setBody(Body(accessToken = accessToken))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @JvmInline
        @Serializable
        internal value class Body(
          @SerialName("access_token")
          public val accessToken: String,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: Authorization,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class Scoped internal constructor(
        private val client: HttpClient,
        private val clientId: String,
      ) {
        public val post: Post = Post(client, clientId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val clientId: String,
        ) {
          public suspend operator fun invoke(
            accessToken: String,
            target: String? = null,
            targetId: Long? = null,
            repositories: List<String>? = null,
            repositoryIds: List<Long>? = null,
            permissions: AppPermissions? = null,
          ): Response {
            val response = client.post("/applications/$clientId/token/scoped") {
              contentType(ContentType.Application.Json)
              setBody(Body(accessToken = accessToken, target = target, targetId = targetId, repositories = repositories, repositoryIds = repositoryIds, permissions = permissions))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("access_token")
            public val accessToken: String,
            public val target: String? = null,
            @SerialName("target_id")
            public val targetId: Long? = null,
            public val repositories: List<String>? = null,
            @SerialName("repository_ids")
            public val repositoryIds: List<Long>? = null,
            public val permissions: AppPermissions? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: Authorization,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }
    }
  }
}
