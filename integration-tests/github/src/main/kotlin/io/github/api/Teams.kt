package io.github.api

import io.github.model.BasicError
import io.github.model.MinimalRepository
import io.github.model.Optional
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
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Deprecated
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Teams internal constructor(
  private val __client: HttpClient,
) {
  public fun teamId(teamId: Long): TeamIdPath = TeamIdPath(__client, teamId)

  public class TeamIdPath internal constructor(
    private val __client: HttpClient,
    private val _teamId: Long,
  ) {
    @Deprecated("Deprecated by the API provider")
    public val delete: Delete = Delete(__client, _teamId)

    @Deprecated("Deprecated by the API provider")
    public val `get`: Get = Get(__client, _teamId)

    @Deprecated("Deprecated by the API provider")
    public val patch: Patch = Patch(__client, _teamId)

    public val invitations: Invitations = Invitations(__client, _teamId)

    public val members: Members = Members(__client, _teamId)

    public val memberships: Memberships = Memberships(__client, _teamId)

    public val repos: Repos = Repos(__client, _teamId)

    public val teams: Teams = Teams(__client, _teamId)

    @Deprecated("Deprecated by the API provider")
    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): Response {
        val response = __client.delete("teams/${this._teamId}")
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(): Response {
        val response = __client.get("teams/${this._teamId}")
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public suspend operator fun invoke(
        name: String,
        description: String? = null,
        privacy: Privacy? = null,
        notificationSetting: NotificationSetting? = null,
        permission: Permission? = null,
        parentTeamId: Optional<Long> = Optional.Absent,
      ): Response {
        val response = __client.patch("teams/${this._teamId}") {
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

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        public val name: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val description: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val privacy: Privacy? = null,
        @SerialName("notification_setting")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val notificationSetting: NotificationSetting? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val permission: Permission? = null,
        @SerialName("parent_team_id")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val parentTeamId: Optional<Long> = Optional.Absent,
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(__client, _teamId)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<OrganizationInvitation> {
          val response = __client.get("teams/${this._teamId}/invitations") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Members internal constructor(
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(__client, _teamId)

      public fun username(username: String): UsernamePath = UsernamePath(__client, _teamId, username)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(
          role: Role? = Role.All,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = __client.get("teams/${this._teamId}/members") {
            if (role != null) {
              this.url.encodedParameters.append("role", role.value.encodeURLParameter())
            }
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
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
        private val __client: HttpClient,
        private val _teamId: Long,
        private val _username: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.delete("teams/${this._teamId}/members/${this._username.encodeURLParameter()}")
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
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.get("teams/${this._teamId}/members/${this._username.encodeURLParameter()}")
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
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.put("teams/${this._teamId}/members/${this._username.encodeURLParameter()}")
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      public fun username(username: String): UsernamePath = UsernamePath(__client, _teamId, username)

      public class UsernamePath internal constructor(
        private val __client: HttpClient,
        private val _teamId: Long,
        private val _username: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(__client, _teamId, _username)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.delete("teams/${this._teamId}/memberships/${this._username.encodeURLParameter()}")
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
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.get("teams/${this._teamId}/memberships/${this._username.encodeURLParameter()}")
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
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _username: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.put("teams/${this._teamId}/memberships/${this._username.encodeURLParameter()}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(role: Role? = null): Response {
            val response = __client.put("teams/${this._teamId}/memberships/${this._username.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(Body(role = role))
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

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(__client, _teamId)

      public fun owner(owner: String): OwnerPath = OwnerPath(__client, _teamId, owner)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = __client.get("teams/${this._teamId}/repos") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
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
        private val __client: HttpClient,
        private val _teamId: Long,
        private val _owner: String,
      ) {
        public fun repo(repo: String): RepoPath = RepoPath(__client, _teamId, _owner, repo)

        public class RepoPath internal constructor(
          private val __client: HttpClient,
          private val _teamId: Long,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val delete: Delete = Delete(__client, _teamId, _owner, _repo)

          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(__client, _teamId, _owner, _repo)

          @Deprecated("Deprecated by the API provider")
          public val put: Put = Put(__client, _teamId, _owner, _repo)

          @Deprecated("Deprecated by the API provider")
          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _teamId: Long,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke() {
              val response = __client.delete("teams/${this._teamId}/repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val __client: HttpClient,
            private val _teamId: Long,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(): Response {
              val response = __client.get("teams/${this._teamId}/repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _teamId: Long,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(): Response {
              val response = __client.put("teams/${this._teamId}/repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(permission: Permission? = null): Response {
              val response = __client.put("teams/${this._teamId}/repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(permission = permission))
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

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
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
      private val __client: HttpClient,
      private val _teamId: Long,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(__client, _teamId)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val __client: HttpClient,
        private val _teamId: Long,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = __client.get("teams/${this._teamId}/teams") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
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
