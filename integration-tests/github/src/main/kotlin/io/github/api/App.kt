package io.github.api

import io.github.model.AppPermissions
import io.github.model.BasicError
import io.github.model.HookDelivery
import io.github.model.HookDeliveryItem
import io.github.model.Installation
import io.github.model.InstallationToken
import io.github.model.Integration
import io.github.model.IntegrationInstallationRequest
import io.github.model.ValidationError
import io.github.model.WebhookConfig
import io.github.model.WebhookConfigContentType
import io.github.model.WebhookConfigInsecureSsl
import io.github.model.WebhookConfigSecret
import io.github.model.WebhookConfigUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

public class App internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val hook: Hook = Hook(__client)

  public val installationRequests: InstallationRequests = InstallationRequests(__client)

  public val installations: Installations = Installations(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Integration? {
      val response = __client.get("app")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Hook internal constructor(
    private val __client: HttpClient,
  ) {
    public val config: Config = Config(__client)

    public val deliveries: Deliveries = Deliveries(__client)

    public class Config internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val patch: Patch = Patch(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(): WebhookConfig {
          val response = __client.get("app/hook/config")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Patch internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          url: WebhookConfigUrl? = null,
          contentType: WebhookConfigContentType? = null,
          secret: WebhookConfigSecret? = null,
          insecureSsl: WebhookConfigInsecureSsl? = null,
        ): WebhookConfig {
          val response = __client.patch("app/hook/config") {
            contentType(ContentType.Application.Json)
            setBody(Body(url = url, contentType = contentType, secret = secret, insecureSsl = insecureSsl))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        internal data class Body(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val url: WebhookConfigUrl? = null,
          @SerialName("content_type")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val contentType: WebhookConfigContentType? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secret: WebhookConfigSecret? = null,
          @SerialName("insecure_ssl")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val insecureSsl: WebhookConfigInsecureSsl? = null,
        )
      }
    }

    public class Deliveries internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public fun deliveryId(deliveryId: Long): DeliveryIdPath = DeliveryIdPath(__client, deliveryId)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, cursor: String? = null): Response {
          val response = __client.get("app/hook/deliveries") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (cursor != null) {
              this.url.encodedParameters.append("cursor", cursor.encodeURLParameter())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            400 -> Response.BadRequest(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<HookDeliveryItem>,
          ) : Response

          public data class BadRequest(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class DeliveryIdPath internal constructor(
        private val __client: HttpClient,
        private val _deliveryId: Long,
      ) {
        public val `get`: Get = Get(__client, _deliveryId)

        public val attempts: Attempts = Attempts(__client, _deliveryId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _deliveryId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("app/hook/deliveries/${this._deliveryId}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: HookDelivery,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Attempts internal constructor(
          private val __client: HttpClient,
          private val _deliveryId: Long,
        ) {
          public val post: Post = Post(__client, _deliveryId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _deliveryId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.post("app/hook/deliveries/${this._deliveryId}/attempts")
              return when (response.status.value) {
                202 -> Response.Accepted(response.body())
                400 -> Response.BadRequest(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Accepted(
                public val `value`: JsonElement,
              ) : Response

              public data class BadRequest(
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

  public class InstallationRequests internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = __client.get("app/installation-requests") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<IntegrationInstallationRequest>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class Installations internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public fun installationId(installationId: Long): InstallationIdPath = InstallationIdPath(__client, installationId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        perPage: Long? = 30L,
        page: Long? = 1L,
        since: Instant? = null,
        outdated: String? = null,
      ): List<Installation> {
        val response = __client.get("app/installations") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
          if (since != null) {
            this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
          }
          if (outdated != null) {
            this.url.encodedParameters.append("outdated", outdated.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class InstallationIdPath internal constructor(
      private val __client: HttpClient,
      private val _installationId: Long,
    ) {
      public val delete: Delete = Delete(__client, _installationId)

      public val `get`: Get = Get(__client, _installationId)

      public val accessTokens: AccessTokens = AccessTokens(__client, _installationId)

      public val suspended: Suspended = Suspended(__client, _installationId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _installationId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.delete("app/installations/${this._installationId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _installationId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.get("app/installations/${this._installationId}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Installation,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class AccessTokens internal constructor(
        private val __client: HttpClient,
        private val _installationId: Long,
      ) {
        public val post: Post = Post(__client, _installationId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _installationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.post("app/installations/${this._installationId}/access_tokens")
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public suspend operator fun invoke(
            repositories: List<String>? = null,
            repositoryIds: List<Long>? = null,
            permissions: AppPermissions? = null,
          ): Response {
            val response = __client.post("app/installations/${this._installationId}/access_tokens") {
              contentType(ContentType.Application.Json)
              setBody(Body(repositories = repositories, repositoryIds = repositoryIds, permissions = permissions))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val repositories: List<String>? = null,
            @SerialName("repository_ids")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val repositoryIds: List<Long>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val permissions: AppPermissions? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: InstallationToken,
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

      public class Suspended internal constructor(
        private val __client: HttpClient,
        private val _installationId: Long,
      ) {
        public val delete: Delete = Delete(__client, _installationId)

        public val put: Put = Put(__client, _installationId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _installationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("app/installations/${this._installationId}/suspended")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _installationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.put("app/installations/${this._installationId}/suspended")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }
  }
}
