package io.github.api

import io.github.model.BasicError
import io.github.model.MinimalRepository
import io.github.model.OrganizationInvitation
import io.github.model.SimpleUser
import io.github.model.Team
import io.github.model.TeamFull
import io.github.model.TeamMembership
import io.github.model.TeamRepository
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
import kotlin.Deprecated
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Teams internal constructor(
  private val client: HttpClient,
) {
  public fun teamId(teamId: Long): TeamIdPath = TeamIdPath(client, teamId)

  public class TeamIdPath internal constructor(
    private val client: HttpClient,
    private val teamId: Long,
  ) {
    @Deprecated("Deprecated by the API provider")
    public val delete: Delete = Delete(client, teamId)

    @Deprecated("Deprecated by the API provider")
    public val `get`: Get = Get(client, teamId)

    @Deprecated("Deprecated by the API provider")
    public val patch: Patch = Patch(client, teamId)

    public val invitations: Invitations = Invitations(client, teamId)

    public val members: Members = Members(client, teamId)

    public val memberships: Memberships = Memberships(client, teamId)

    public val repos: Repos = Repos(client, teamId)

    public val teams: Teams = Teams(client, teamId)

    @Deprecated("Deprecated by the API provider")
    public class Delete internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): Response {
        val response = client.delete("/teams/$teamId")
        return when (response.status.value) {
          204 -> Response.NoContent
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data object NoContent : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response
      }
    }

    @Deprecated("Deprecated by the API provider")
    public class Get internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): Response {
        val response = client.get("/teams/$teamId")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: TeamFull,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    @Deprecated("Deprecated by the API provider")
    public class Patch internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(
        name: String,
        description: String? = null,
        privacy: Privacy? = null,
        notificationSetting: NotificationSetting? = null,
        permission: Permission? = null,
        parentTeamId: Long? = null,
      ): Response {
        val response = client.patch("/teams/$teamId") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name, description = description, privacy = privacy, notificationSetting = notificationSetting, permission = permission, parentTeamId = parentTeamId))
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          201 -> Response.Created(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Privacy(
        public val `value`: String,
      ) {
        @SerialName("secret")
        Secret("secret"),
        @SerialName("closed")
        Closed("closed"),
        ;
      }

      @Serializable
      public enum class NotificationSetting(
        public val `value`: String,
      ) {
        @SerialName("notifications_enabled")
        NotificationsEnabled("notifications_enabled"),
        @SerialName("notifications_disabled")
        NotificationsDisabled("notifications_disabled"),
        ;
      }

      @Serializable
      public enum class Permission(
        public val `value`: String,
      ) {
        @SerialName("pull")
        Pull("pull"),
        @SerialName("push")
        Push("push"),
        @SerialName("admin")
        Admin("admin"),
        ;
      }

      @Serializable
      internal data class Body(
        public val name: String,
        public val description: String? = null,
        public val privacy: Privacy? = null,
        @SerialName("notification_setting")
        public val notificationSetting: NotificationSetting? = null,
        public val permission: Permission? = null,
        @SerialName("parent_team_id")
        public val parentTeamId: Long? = null,
      )

      public sealed interface Response {
        public data class Ok(
          public val `value`: TeamFull,
        ) : Response

        public data class Created(
          public val `value`: TeamFull,
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

    public class Invitations internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(client, teamId)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<OrganizationInvitation> = client.get("/teams/$teamId/invitations") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Members internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(client, teamId)

      public fun username(username: String): UsernamePath = UsernamePath(client, teamId, username)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(
          role: Role? = Role.All,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = client.get("/teams/$teamId/members") {
            role?.let { parameter("role", it.value) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Role(
          public val `value`: String,
        ) {
          @SerialName("member")
          Member("member"),
          @SerialName("maintainer")
          Maintainer("maintainer"),
          @SerialName("all")
          All("all"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<SimpleUser>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
        private val username: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.delete("/teams/$teamId/members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotFound : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Get internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.get("/teams/$teamId/members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotFound : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Put internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.put("/teams/$teamId/members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data object NotFound : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class Memberships internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      public fun username(username: String): UsernamePath = UsernamePath(client, teamId, username)

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
        private val username: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(client, teamId, username)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.delete("/teams/$teamId/memberships/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object Forbidden : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Get internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.get("/teams/$teamId/memberships/$username")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: TeamMembership,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Put internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(role: Role? = null): Response {
            val response = client.put("/teams/$teamId/memberships/$username") {
              if (role != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(role = role))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Role(
            public val `value`: String,
          ) {
            @SerialName("member")
            Member("member"),
            @SerialName("maintainer")
            Maintainer("maintainer"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val role: Role? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: TeamMembership,
            ) : Response

            public data object Forbidden : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class Repos internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(client, teamId)

      public fun owner(owner: String): OwnerPath = OwnerPath(client, teamId, owner)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/teams/$teamId/repos") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<MinimalRepository>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class OwnerPath internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
        private val owner: String,
      ) {
        public fun repo(repo: String): RepoPath = RepoPath(client, teamId, owner, repo)

        public class RepoPath internal constructor(
          private val client: HttpClient,
          private val teamId: Long,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val delete: Delete = Delete(client, teamId, owner, repo)

          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(client, teamId, owner, repo)

          @Deprecated("Deprecated by the API provider")
          public val put: Put = Put(client, teamId, owner, repo)

          @Deprecated("Deprecated by the API provider")
          public class Delete internal constructor(
            private val client: HttpClient,
            private val teamId: Long,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke() {
              client.delete("/teams/$teamId/repos/$owner/$repo")
            }
          }

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val client: HttpClient,
            private val teamId: Long,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(): Response {
              val response = client.get("/teams/$teamId/repos/$owner/$repo")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                204 -> Response.NoContent
                404 -> Response.NotFound
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: TeamRepository,
              ) : Response

              public data object NoContent : Response

              public data object NotFound : Response
            }
          }

          @Deprecated("Deprecated by the API provider")
          public class Put internal constructor(
            private val client: HttpClient,
            private val teamId: Long,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(permission: Permission? = null): Response {
              val response = client.put("/teams/$teamId/repos/$owner/$repo") {
                if (permission != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(permission = permission))
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Permission(
              public val `value`: String,
            ) {
              @SerialName("pull")
              Pull("pull"),
              @SerialName("push")
              Push("push"),
              @SerialName("admin")
              Admin("admin"),
              ;
            }

            @JvmInline
            @Serializable
            internal value class Body(
              public val permission: Permission? = null,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
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

    public class Teams internal constructor(
      private val client: HttpClient,
      private val teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(client, teamId)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val client: HttpClient,
        private val teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/teams/$teamId/teams") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<Team>,
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
