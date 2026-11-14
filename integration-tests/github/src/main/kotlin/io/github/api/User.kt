package io.github.api

import io.github.model.BasicError
import io.github.model.Codespace
import io.github.model.CodespaceExportDetails
import io.github.model.CodespaceMachine
import io.github.model.CodespaceWithFullRepository
import io.github.model.CodespacesSecret
import io.github.model.CodespacesUserPublicKey
import io.github.model.EmptyObject
import io.github.model.FullRepository
import io.github.model.GpgKey
import io.github.model.Installation
import io.github.model.InteractionLimit
import io.github.model.InteractionLimitResponse
import io.github.model.Issue
import io.github.model.Key
import io.github.model.Migration
import io.github.model.MinimalRepository
import io.github.model.NullableLicenseSimple
import io.github.model.OrgMembership
import io.github.model.OrganizationSimple
import io.github.model.Package
import io.github.model.PackageVersion
import io.github.model.PrivateUser
import io.github.model.ProjectsV2ItemSimple
import io.github.model.Repository
import io.github.model.RepositoryInvitation
import io.github.model.SimpleUser
import io.github.model.SocialAccount
import io.github.model.SshSigningKey
import io.github.model.StarredRepository
import io.github.model.TeamFull
import io.github.model.UserMarketplacePurchase
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

public class User internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val patch: Patch = Patch(client)

  public val blocks: Blocks = Blocks(client)

  public val codespaces: Codespaces = Codespaces(client)

  public val docker: Docker = Docker(client)

  public val email: Email = Email(client)

  public val emails: Emails = Emails(client)

  public val followers: Followers = Followers(client)

  public val following: Following = Following(client)

  public val gpgKeys: GpgKeys = GpgKeys(client)

  public val installations: Installations = Installations(client)

  public val interactionLimits: InteractionLimits = InteractionLimits(client)

  public val issues: Issues = Issues(client)

  public val keys: Keys = Keys(client)

  public val marketplacePurchases: MarketplacePurchases = MarketplacePurchases(client)

  public val memberships: Memberships = Memberships(client)

  public val migrations: Migrations = Migrations(client)

  public val orgs: Orgs = Orgs(client)

  public val packages: Packages = Packages(client)

  public val publicEmails: PublicEmails = PublicEmails(client)

  public val repos: Repos = Repos(client)

  public val repositoryInvitations: RepositoryInvitations = RepositoryInvitations(client)

  public val socialAccounts: SocialAccounts = SocialAccounts(client)

  public val sshSigningKeys: SshSigningKeys = SshSigningKeys(client)

  public val starred: Starred = Starred(client)

  public val subscriptions: Subscriptions = Subscriptions(client)

  public val teams: Teams = Teams(client)

  public fun accountId(accountId: Long): AccountIdPath = AccountIdPath(client, accountId)

  public fun userId(userId: String): UserIdPath = UserIdPath(client, userId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.get("user")
      return when (response.status.value) {
        200 -> response.body<Response.Ok>()
        304 -> Response.NotModified
        401 -> Response.Unauthorized(response.body())
        403 -> Response.Forbidden(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      @OptIn(ExperimentalSerializationApi::class)
      @JsonClassDiscriminator("user_view_type")
      @Serializable
      public sealed interface Ok : Response {
        /**
         * Private User
         */
        @SerialName("private")
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Private(
          public val login: String,
          public val id: Long,
          @SerialName("node_id")
          public val nodeId: String,
          @SerialName("avatar_url")
          public val avatarUrl: String,
          @SerialName("gravatar_id")
          public val gravatarId: String?,
          public val url: String,
          @SerialName("html_url")
          public val htmlUrl: String,
          @SerialName("followers_url")
          public val followersUrl: String,
          @SerialName("following_url")
          public val followingUrl: String,
          @SerialName("gists_url")
          public val gistsUrl: String,
          @SerialName("starred_url")
          public val starredUrl: String,
          @SerialName("subscriptions_url")
          public val subscriptionsUrl: String,
          @SerialName("organizations_url")
          public val organizationsUrl: String,
          @SerialName("repos_url")
          public val reposUrl: String,
          @SerialName("events_url")
          public val eventsUrl: String,
          @SerialName("received_events_url")
          public val receivedEventsUrl: String,
          public val type: String,
          @SerialName("site_admin")
          public val siteAdmin: Boolean,
          public val name: String?,
          public val company: String?,
          public val blog: String?,
          public val location: String?,
          public val email: String?,
          @SerialName("notification_email")
          public val notificationEmail: String? = null,
          public val hireable: Boolean?,
          public val bio: String?,
          @SerialName("twitter_username")
          public val twitterUsername: String? = null,
          @SerialName("public_repos")
          public val publicRepos: Long,
          @SerialName("public_gists")
          public val publicGists: Long,
          public val followers: Long,
          public val following: Long,
          @SerialName("created_at")
          public val createdAt: Instant,
          @SerialName("updated_at")
          public val updatedAt: Instant,
          @SerialName("private_gists")
          public val privateGists: Long,
          @SerialName("total_private_repos")
          public val totalPrivateRepos: Long,
          @SerialName("owned_private_repos")
          public val ownedPrivateRepos: Long,
          @SerialName("disk_usage")
          public val diskUsage: Long,
          public val collaborators: Long,
          @SerialName("two_factor_authentication")
          public val twoFactorAuthentication: Boolean,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val plan: Plan? = null,
          @SerialName("business_plus")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val businessPlus: Boolean? = null,
          @SerialName("ldap_dn")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val ldapDn: String? = null,
        ) : Ok {
          @Serializable
          public data class Plan(
            public val collaborators: Long,
            public val name: String,
            public val space: Long,
            @SerialName("private_repos")
            public val privateRepos: Long,
          )
        }

        /**
         * Public User
         */
        @SerialName("public")
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Public(
          public val login: String,
          public val id: Long,
          @SerialName("node_id")
          public val nodeId: String,
          @SerialName("avatar_url")
          public val avatarUrl: String,
          @SerialName("gravatar_id")
          public val gravatarId: String?,
          public val url: String,
          @SerialName("html_url")
          public val htmlUrl: String,
          @SerialName("followers_url")
          public val followersUrl: String,
          @SerialName("following_url")
          public val followingUrl: String,
          @SerialName("gists_url")
          public val gistsUrl: String,
          @SerialName("starred_url")
          public val starredUrl: String,
          @SerialName("subscriptions_url")
          public val subscriptionsUrl: String,
          @SerialName("organizations_url")
          public val organizationsUrl: String,
          @SerialName("repos_url")
          public val reposUrl: String,
          @SerialName("events_url")
          public val eventsUrl: String,
          @SerialName("received_events_url")
          public val receivedEventsUrl: String,
          public val type: String,
          @SerialName("site_admin")
          public val siteAdmin: Boolean,
          public val name: String?,
          public val company: String?,
          public val blog: String?,
          public val location: String?,
          public val email: String?,
          @SerialName("notification_email")
          public val notificationEmail: String? = null,
          public val hireable: Boolean?,
          public val bio: String?,
          @SerialName("twitter_username")
          public val twitterUsername: String? = null,
          @SerialName("public_repos")
          public val publicRepos: Long,
          @SerialName("public_gists")
          public val publicGists: Long,
          public val followers: Long,
          public val following: Long,
          @SerialName("created_at")
          public val createdAt: Instant,
          @SerialName("updated_at")
          public val updatedAt: Instant,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val plan: Plan? = null,
          @SerialName("private_gists")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val privateGists: Long? = null,
          @SerialName("total_private_repos")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val totalPrivateRepos: Long? = null,
          @SerialName("owned_private_repos")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val ownedPrivateRepos: Long? = null,
          @SerialName("disk_usage")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val diskUsage: Long? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val collaborators: Long? = null,
        ) : Ok {
          @Serializable
          public data class Plan(
            public val collaborators: Long,
            public val name: String,
            public val space: Long,
            @SerialName("private_repos")
            public val privateRepos: Long,
          )
        }
      }

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
  ) {
    public suspend operator fun invoke(): Response {
      val response = client.patch("user")
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        401 -> Response.Unauthorized(response.body())
        403 -> Response.Forbidden(response.body())
        404 -> Response.NotFound(response.body())
        422 -> Response.UnprocessableEntity(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public suspend operator fun invoke(
      name: String? = null,
      email: String? = null,
      blog: String? = null,
      twitterUsername: String? = null,
      company: String? = null,
      location: String? = null,
      hireable: Boolean? = null,
      bio: String? = null,
    ): Response {
      val response = client.patch("user") {
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, email = email, blog = blog, twitterUsername = twitterUsername, company = company, location = location, hireable = hireable, bio = bio))
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
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
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val email: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val blog: String? = null,
      @SerialName("twitter_username")
      public val twitterUsername: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val company: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val location: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val hireable: Boolean? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val bio: String? = null,
    )

    public sealed interface Response {
      public data class Ok(
        public val `value`: PrivateUser,
      ) : Response

      public data object NotModified : Response

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

  public class Blocks internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun username(username: String): UsernamePath = UsernamePath(client, username)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/blocks") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<SimpleUser>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class UsernamePath internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val delete: Delete = Delete(client, username)

      public val `get`: Get = Get(client, username)

      public val put: Put = Put(client, username)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/blocks/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/blocks/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Put internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.put("user/blocks/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
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

  public class Codespaces internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public val secrets: Secrets = Secrets(client)

    public fun codespaceName(codespaceName: String): CodespaceNamePath = CodespaceNamePath(client, codespaceName)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        perPage: Long? = 30L,
        page: Long? = 1L,
        repositoryId: Long? = null,
      ): Response {
        val response = client.get("user/codespaces") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
          if (repositoryId != null) {
            this.url.encodedParameters.append("repository_id", repositoryId.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          500 -> Response.InternalServerError(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          public val codespaces: List<Codespace>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response

        public data class InternalServerError(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: One): Response {
        val response = client.post("user/codespaces") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          202 -> Response.Accepted(response.body())
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: Two): Response {
        val response = client.post("user/codespaces") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          202 -> Response.Accepted(response.body())
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class One(
        @SerialName("repository_id")
        public val repositoryId: Long,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val ref: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val location: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val geo: Geo? = null,
        @SerialName("client_ip")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val clientIp: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val machine: String? = null,
        @SerialName("devcontainer_path")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val devcontainerPath: String? = null,
        @SerialName("multi_repo_permissions_opt_out")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val multiRepoPermissionsOptOut: Boolean? = null,
        @SerialName("working_directory")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val workingDirectory: String? = null,
        @SerialName("idle_timeout_minutes")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val idleTimeoutMinutes: Long? = null,
        @SerialName("display_name")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val displayName: String? = null,
        @SerialName("retention_period_minutes")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val retentionPeriodMinutes: Long? = null,
      ) {
        @Serializable
        public enum class Geo {
          EuropeWest,
          SoutheastAsia,
          UsEast,
          UsWest,
        }
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Two(
        @SerialName("pull_request")
        public val pullRequest: PullRequest,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val location: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val geo: One.Geo? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val machine: String? = null,
        @SerialName("devcontainer_path")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val devcontainerPath: String? = null,
        @SerialName("working_directory")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val workingDirectory: String? = null,
        @SerialName("idle_timeout_minutes")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val idleTimeoutMinutes: Long? = null,
      ) {
        /**
         * Pull request number for this codespace
         */
        @Serializable
        public data class PullRequest(
          @SerialName("pull_request_number")
          public val pullRequestNumber: Long,
          @SerialName("repository_id")
          public val repositoryId: Long,
        )
      }

      public sealed interface Response {
        public data class Created(
          public val `value`: Codespace,
        ) : Response

        public data class Accepted(
          public val `value`: Codespace,
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

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class ServiceUnavailable(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val code: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response
      }
    }

    public class Secrets internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public val publicKey: PublicKey = PublicKey(client)

      public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, secretName)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("user/codespaces/secrets") {
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

        @Serializable
        public data class Response(
          @SerialName("total_count")
          public val totalCount: Long,
          public val secrets: List<CodespacesSecret>,
        )
      }

      public class PublicKey internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(): CodespacesUserPublicKey {
            val response = client.get("user/codespaces/secrets/public-key")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class SecretNamePath internal constructor(
        private val client: HttpClient,
        private val secretName: String,
      ) {
        public val delete: Delete = Delete(client, secretName)

        public val `get`: Get = Get(client, secretName)

        public val put: Put = Put(client, secretName)

        public val repositories: Repositories = Repositories(client, secretName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val secretName: String,
        ) {
          public suspend operator fun invoke() {
            val response = client.delete("user/codespaces/secrets/${this.secretName.encodeURLParameter()}")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val secretName: String,
        ) {
          public suspend operator fun invoke(): CodespacesSecret {
            val response = client.get("user/codespaces/secrets/${this.secretName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val secretName: String,
        ) {
          public suspend operator fun invoke(
            encryptedValue: String? = null,
            keyId: String,
            selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
          ): Response {
            val response = client.put("user/codespaces/secrets/${this.secretName.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(Body(encryptedValue = encryptedValue, keyId = keyId, selectedRepositoryIds = selectedRepositoryIds))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable(with = SelectedRepositoryIds.Serializer::class)
          public sealed interface SelectedRepositoryIds {
            @Serializable
            @JvmInline
            public value class CaseLong(
              public val `value`: Long,
            ) : SelectedRepositoryIds

            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : SelectedRepositoryIds

            public object Serializer : KSerializer<SelectedRepositoryIds> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.User.Codespaces.Secrets.SecretNamePath.Put.SelectedRepositoryIds", PolymorphicKind.SEALED) {
                element("CaseLong", Long.serializer().descriptor)
                element("CaseString", String.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): SelectedRepositoryIds {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                  value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: SelectedRepositoryIds) {
                when(value) {
                  is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                }
              }
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("encrypted_value")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val encryptedValue: String? = null,
            @SerialName("key_id")
            public val keyId: String,
            @SerialName("selected_repository_ids")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: EmptyObject,
            ) : Response

            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val secretName: String,
        ) {
          public val `get`: Get = Get(client, secretName)

          public val put: Put = Put(client, secretName)

          public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, secretName, repositoryId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("user/codespaces/secrets/${this.secretName.encodeURLParameter()}/repositories")
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              @Serializable
              public data class Ok(
                @SerialName("total_count")
                public val totalCount: Long,
                public val repositories: List<MinimalRepository>,
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

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(selectedRepositoryIds: List<Long>): Response {
              val response = client.put("user/codespaces/secrets/${this.secretName.encodeURLParameter()}/repositories") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class RepositoryIdPath internal constructor(
            private val client: HttpClient,
            private val secretName: String,
            private val repositoryId: Long,
          ) {
            public val delete: Delete = Delete(client, secretName, repositoryId)

            public val put: Put = Put(client, secretName, repositoryId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val secretName: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/codespaces/secrets/${this.secretName.encodeURLParameter()}/repositories/${this.repositoryId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val secretName: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.put("user/codespaces/secrets/${this.secretName.encodeURLParameter()}/repositories/${this.repositoryId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class CodespaceNamePath internal constructor(
      private val client: HttpClient,
      private val codespaceName: String,
    ) {
      public val delete: Delete = Delete(client, codespaceName)

      public val `get`: Get = Get(client, codespaceName)

      public val patch: Patch = Patch(client, codespaceName)

      public val exports: Exports = Exports(client, codespaceName)

      public val machines: Machines = Machines(client, codespaceName)

      public val publish: Publish = Publish(client, codespaceName)

      public val start: Start = Start(client, codespaceName)

      public val stop: Stop = Stop(client, codespaceName)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/codespaces/${this.codespaceName.encodeURLParameter()}")
          return when (response.status.value) {
            202 -> Response.Accepted(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Accepted(
            public val `value`: JsonElement,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/codespaces/${this.codespaceName.encodeURLParameter()}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Codespace,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Patch internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.patch("user/codespaces/${this.codespaceName.encodeURLParameter()}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public suspend operator fun invoke(
          machine: String? = null,
          displayName: String? = null,
          recentFolders: List<String>? = null,
        ): Response {
          val response = client.patch("user/codespaces/${this.codespaceName.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(Body(machine = machine, displayName = displayName, recentFolders = recentFolders))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        internal data class Body(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val machine: String? = null,
          @SerialName("display_name")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val displayName: String? = null,
          @SerialName("recent_folders")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val recentFolders: List<String>? = null,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: Codespace,
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
        }
      }

      public class Exports internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public val post: Post = Post(client, codespaceName)

        public fun exportId(exportId: String): ExportIdPath = ExportIdPath(client, codespaceName, exportId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.post("user/codespaces/${this.codespaceName.encodeURLParameter()}/exports")
            return when (response.status.value) {
              202 -> Response.Accepted(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Accepted(
              public val `value`: CodespaceExportDetails,
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

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class ExportIdPath internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
          private val exportId: String,
        ) {
          public val `get`: Get = Get(client, codespaceName, exportId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val codespaceName: String,
            private val exportId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("user/codespaces/${this.codespaceName.encodeURLParameter()}/exports/${this.exportId.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodespaceExportDetails,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Machines internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public val `get`: Get = Get(client, codespaceName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("user/codespaces/${this.codespaceName.encodeURLParameter()}/machines")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @Serializable
            public data class Ok(
              @SerialName("total_count")
              public val totalCount: Long,
              public val machines: List<CodespaceMachine>,
            ) : Response

            public data object NotModified : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Publish internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public val post: Post = Post(client, codespaceName)

        public class Post internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
        ) {
          public suspend operator fun invoke(name: String? = null, `private`: Boolean? = null): Response {
            val response = client.post("user/codespaces/${this.codespaceName.encodeURLParameter()}/publish") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, private = private))
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
            public val name: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val `private`: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: CodespaceWithFullRepository,
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

      public class Start internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public val post: Post = Post(client, codespaceName)

        public class Post internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.post("user/codespaces/${this.codespaceName.encodeURLParameter()}/start")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              400 -> Response.BadRequest(response.body())
              401 -> Response.Unauthorized(response.body())
              402 -> Response.PaymentRequired(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              409 -> Response.Conflict(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: Codespace,
            ) : Response

            public data object NotModified : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class PaymentRequired(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class Conflict(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Stop internal constructor(
        private val client: HttpClient,
        private val codespaceName: String,
      ) {
        public val post: Post = Post(client, codespaceName)

        public class Post internal constructor(
          private val client: HttpClient,
          private val codespaceName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.post("user/codespaces/${this.codespaceName.encodeURLParameter()}/stop")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: Codespace,
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

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }
  }

  public class Docker internal constructor(
    private val client: HttpClient,
  ) {
    public val conflicts: Conflicts = Conflicts(client)

    public class Conflicts internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(): List<Package> {
          val response = client.get("user/docker/conflicts")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class Email internal constructor(
    private val client: HttpClient,
  ) {
    public val visibility: Visibility = Visibility(client)

    public class Visibility internal constructor(
      private val client: HttpClient,
    ) {
      public val patch: Patch = Patch(client)

      public class Patch internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(visibility: Visibility): Response {
          val response = client.patch("user/email/visibility") {
            contentType(ContentType.Application.Json)
            setBody(Body(visibility = visibility))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Visibility(
          public val `value`: String,
        ) {
          @SerialName("public")
          Public("public"),
          @SerialName("private")
          Private("private"),
          ;
        }

        @Serializable
        internal data class Body(
          public val visibility: Visibility,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<io.github.model.Email>,
          ) : Response

          public data object NotModified : Response

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

  public class Emails internal constructor(
    private val client: HttpClient,
  ) {
    public val delete: Delete = Delete(client)

    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public class Delete internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("user/emails")
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: EmailsStrings): Response {
        val response = client.delete("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: List<String>): Response {
        val response = client.delete("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: String): Response {
        val response = client.delete("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(JsonPrimitive(body))
        }
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      /**
       * Deletes one or more email addresses from your GitHub account. Must contain at least one email address. **Note:** Alternatively, you can pass a single email address or an `array` of emails addresses directly, but we recommend that you pass an object using the `emails` key.
       */
      @Serializable
      public data class EmailsStrings(
        public val emails: List<String>,
      )

      public sealed interface Response {
        public data object NoContent : Response

        public data object NotModified : Response

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

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/emails") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<io.github.model.Email>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.post("user/emails")
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: EmailsStrings): Response {
        val response = client.post("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: List<String>): Response {
        val response = client.post("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend operator fun invoke(body: String): Response {
        val response = client.post("user/emails") {
          contentType(ContentType.Application.Json)
          setBody(JsonPrimitive(body))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public data class EmailsStrings(
        public val emails: List<String>,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: List<io.github.model.Email>,
        ) : Response

        public data object NotModified : Response

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

  public class Followers internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/followers") {
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
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<SimpleUser>,
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

  public class Following internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun username(username: String): UsernamePath = UsernamePath(client, username)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/following") {
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
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<SimpleUser>,
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

    public class UsernamePath internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val delete: Delete = Delete(client, username)

      public val `get`: Get = Get(client, username)

      public val put: Put = Put(client, username)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/following/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/following/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Put internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.put("user/following/${this.username.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
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

  public class GpgKeys internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun gpgKeyId(gpgKeyId: Long): GpgKeyIdPath = GpgKeyIdPath(client, gpgKeyId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/gpg_keys") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<GpgKey>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(name: String? = null, armoredPublicKey: String): Response {
        val response = client.post("user/gpg_keys") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name, armoredPublicKey = armoredPublicKey))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
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
        public val name: String? = null,
        @SerialName("armored_public_key")
        public val armoredPublicKey: String,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: GpgKey,
        ) : Response

        public data object NotModified : Response

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

    public class GpgKeyIdPath internal constructor(
      private val client: HttpClient,
      private val gpgKeyId: Long,
    ) {
      public val delete: Delete = Delete(client, gpgKeyId)

      public val `get`: Get = Get(client, gpgKeyId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val gpgKeyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/gpg_keys/${this.gpgKeyId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val gpgKeyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/gpg_keys/${this.gpgKeyId}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: GpgKey,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class Installations internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun installationId(installationId: Long): InstallationIdPath = InstallationIdPath(client, installationId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/installations") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          public val installations: List<Installation>,
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

    public class InstallationIdPath internal constructor(
      private val client: HttpClient,
      private val installationId: Long,
    ) {
      public val repositories: Repositories = Repositories(client, installationId)

      public class Repositories internal constructor(
        private val client: HttpClient,
        private val installationId: Long,
      ) {
        public val `get`: Get = Get(client, installationId)

        public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, installationId, repositoryId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val installationId: Long,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("user/installations/${this.installationId}/repositories") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              304 -> Response.NotModified
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Ok(
              @SerialName("total_count")
              public val totalCount: Long,
              @SerialName("repository_selection")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val repositorySelection: String? = null,
              public val repositories: List<Repositories>,
            ) : Response {
              /**
               * A repository on GitHub.
               */
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Repositories(
                public val id: Long,
                @SerialName("node_id")
                public val nodeId: String,
                public val name: String,
                @SerialName("full_name")
                public val fullName: String,
                public val license: NullableLicenseSimple?,
                public val forks: Long,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val permissions: Permissions? = null,
                public val owner: SimpleUser,
                @Required
                public val `private`: Boolean = false,
                @SerialName("html_url")
                public val htmlUrl: String,
                public val description: String?,
                public val fork: Boolean,
                public val url: String,
                @SerialName("archive_url")
                public val archiveUrl: String,
                @SerialName("assignees_url")
                public val assigneesUrl: String,
                @SerialName("blobs_url")
                public val blobsUrl: String,
                @SerialName("branches_url")
                public val branchesUrl: String,
                @SerialName("collaborators_url")
                public val collaboratorsUrl: String,
                @SerialName("comments_url")
                public val commentsUrl: String,
                @SerialName("commits_url")
                public val commitsUrl: String,
                @SerialName("compare_url")
                public val compareUrl: String,
                @SerialName("contents_url")
                public val contentsUrl: String,
                @SerialName("contributors_url")
                public val contributorsUrl: String,
                @SerialName("deployments_url")
                public val deploymentsUrl: String,
                @SerialName("downloads_url")
                public val downloadsUrl: String,
                @SerialName("events_url")
                public val eventsUrl: String,
                @SerialName("forks_url")
                public val forksUrl: String,
                @SerialName("git_commits_url")
                public val gitCommitsUrl: String,
                @SerialName("git_refs_url")
                public val gitRefsUrl: String,
                @SerialName("git_tags_url")
                public val gitTagsUrl: String,
                @SerialName("git_url")
                public val gitUrl: String,
                @SerialName("issue_comment_url")
                public val issueCommentUrl: String,
                @SerialName("issue_events_url")
                public val issueEventsUrl: String,
                @SerialName("issues_url")
                public val issuesUrl: String,
                @SerialName("keys_url")
                public val keysUrl: String,
                @SerialName("labels_url")
                public val labelsUrl: String,
                @SerialName("languages_url")
                public val languagesUrl: String,
                @SerialName("merges_url")
                public val mergesUrl: String,
                @SerialName("milestones_url")
                public val milestonesUrl: String,
                @SerialName("notifications_url")
                public val notificationsUrl: String,
                @SerialName("pulls_url")
                public val pullsUrl: String,
                @SerialName("releases_url")
                public val releasesUrl: String,
                @SerialName("ssh_url")
                public val sshUrl: String,
                @SerialName("stargazers_url")
                public val stargazersUrl: String,
                @SerialName("statuses_url")
                public val statusesUrl: String,
                @SerialName("subscribers_url")
                public val subscribersUrl: String,
                @SerialName("subscription_url")
                public val subscriptionUrl: String,
                @SerialName("tags_url")
                public val tagsUrl: String,
                @SerialName("teams_url")
                public val teamsUrl: String,
                @SerialName("trees_url")
                public val treesUrl: String,
                @SerialName("clone_url")
                public val cloneUrl: String,
                @SerialName("mirror_url")
                public val mirrorUrl: String?,
                @SerialName("hooks_url")
                public val hooksUrl: String,
                @SerialName("svn_url")
                public val svnUrl: String,
                public val homepage: String?,
                public val language: String?,
                @SerialName("forks_count")
                public val forksCount: Long,
                @SerialName("stargazers_count")
                public val stargazersCount: Long,
                @SerialName("watchers_count")
                public val watchersCount: Long,
                public val size: Long,
                @SerialName("default_branch")
                public val defaultBranch: String,
                @SerialName("open_issues_count")
                public val openIssuesCount: Long,
                @SerialName("is_template")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val isTemplate: Boolean? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val topics: List<String>? = null,
                @SerialName("has_issues")
                @Required
                public val hasIssues: Boolean = true,
                @SerialName("has_projects")
                @Required
                public val hasProjects: Boolean = true,
                @SerialName("has_wiki")
                @Required
                public val hasWiki: Boolean = true,
                @SerialName("has_pages")
                public val hasPages: Boolean,
                @SerialName("has_downloads")
                @Required
                public val hasDownloads: Boolean = true,
                @SerialName("has_discussions")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val hasDiscussions: Boolean? = null,
                @SerialName("has_pull_requests")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val hasPullRequests: Boolean? = null,
                @SerialName("pull_request_creation_policy")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
                @SerialName("has_commit_comments")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val hasCommitComments: Boolean? = null,
                @Required
                public val archived: Boolean = false,
                public val disabled: Boolean,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val visibility: String? = null,
                @SerialName("pushed_at")
                public val pushedAt: Instant?,
                @SerialName("created_at")
                public val createdAt: Instant?,
                @SerialName("updated_at")
                public val updatedAt: Instant?,
                @SerialName("allow_rebase_merge")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowRebaseMerge: Boolean? = null,
                @SerialName("temp_clone_token")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val tempCloneToken: String? = null,
                @SerialName("allow_squash_merge")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowSquashMerge: Boolean? = null,
                @SerialName("allow_auto_merge")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowAutoMerge: Boolean? = null,
                @SerialName("delete_branch_on_merge")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val deleteBranchOnMerge: Boolean? = null,
                @SerialName("allow_update_branch")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowUpdateBranch: Boolean? = null,
                @SerialName("use_squash_pr_title_as_default")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val useSquashPrTitleAsDefault: Boolean? = null,
                @SerialName("squash_merge_commit_title")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
                @SerialName("squash_merge_commit_message")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
                @SerialName("merge_commit_title")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val mergeCommitTitle: MergeCommitTitle? = null,
                @SerialName("merge_commit_message")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val mergeCommitMessage: MergeCommitMessage? = null,
                @SerialName("allow_merge_commit")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowMergeCommit: Boolean? = null,
                @SerialName("allow_forking")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowForking: Boolean? = null,
                @SerialName("web_commit_signoff_required")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val webCommitSignoffRequired: Boolean? = null,
                @SerialName("open_issues")
                public val openIssues: Long,
                public val watchers: Long,
                @SerialName("master_branch")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val masterBranch: String? = null,
                @SerialName("starred_at")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val starredAt: String? = null,
                @SerialName("anonymous_access_enabled")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val anonymousAccessEnabled: Boolean? = null,
                @SerialName("code_search_index_status")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val codeSearchIndexStatus: CodeSearchIndexStatus? = null,
              ) {
                /**
                 * The status of the code search index for this repository
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class CodeSearchIndexStatus(
                  @SerialName("lexical_search_ok")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val lexicalSearchOk: Boolean? = null,
                  @SerialName("lexical_commit_sha")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val lexicalCommitSha: String? = null,
                )

                @Serializable
                public enum class MergeCommitMessage(
                  public val `value`: String,
                ) {
                  @SerialName("PR_BODY")
                  PRBODY("PR_BODY"),
                  @SerialName("PR_TITLE")
                  PRTITLE("PR_TITLE"),
                  BLANK("BLANK"),
                  ;
                }

                @Serializable
                public enum class MergeCommitTitle(
                  public val `value`: String,
                ) {
                  @SerialName("PR_TITLE")
                  PRTITLE("PR_TITLE"),
                  @SerialName("MERGE_MESSAGE")
                  MERGEMESSAGE("MERGE_MESSAGE"),
                  ;
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Permissions(
                  public val admin: Boolean,
                  public val pull: Boolean,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val triage: Boolean? = null,
                  public val push: Boolean,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val maintain: Boolean? = null,
                )

                @Serializable
                public enum class PullRequestCreationPolicy(
                  public val `value`: String,
                ) {
                  @SerialName("all")
                  All("all"),
                  @SerialName("collaborators_only")
                  CollaboratorsOnly("collaborators_only"),
                  ;
                }

                @Serializable
                public enum class SquashMergeCommitMessage(
                  public val `value`: String,
                ) {
                  @SerialName("PR_BODY")
                  PRBODY("PR_BODY"),
                  @SerialName("COMMIT_MESSAGES")
                  COMMITMESSAGES("COMMIT_MESSAGES"),
                  BLANK("BLANK"),
                  ;
                }

                @Serializable
                public enum class SquashMergeCommitTitle(
                  public val `value`: String,
                ) {
                  @SerialName("PR_TITLE")
                  PRTITLE("PR_TITLE"),
                  @SerialName("COMMIT_OR_PR_TITLE")
                  COMMITORPRTITLE("COMMIT_OR_PR_TITLE"),
                  ;
                }
              }
            }

            public data object NotModified : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class RepositoryIdPath internal constructor(
          private val client: HttpClient,
          private val installationId: Long,
          private val repositoryId: Long,
        ) {
          public val delete: Delete = Delete(client, installationId, repositoryId)

          public val put: Put = Put(client, installationId, repositoryId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val installationId: Long,
            private val repositoryId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("user/installations/${this.installationId}/repositories/${this.repositoryId}")
              return when (response.status.value) {
                204 -> Response.NoContent
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val installationId: Long,
            private val repositoryId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.put("user/installations/${this.installationId}/repositories/${this.repositoryId}")
              return when (response.status.value) {
                204 -> Response.NoContent
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }
  }

  public class InteractionLimits internal constructor(
    private val client: HttpClient,
  ) {
    public val delete: Delete = Delete(client)

    public val `get`: Get = Get(client)

    public val put: Put = Put(client)

    public class Delete internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.delete("user/interaction-limits")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("user/interaction-limits")
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          204 -> Response.NoContent
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @Serializable(with = Ok.Serializer::class)
        public sealed interface Ok : Response {
          @Serializable
          @JvmInline
          public value class CaseInteractionLimitResponse(
            public val `value`: InteractionLimitResponse,
          ) : Ok

          @Serializable
          public data object Empty : Ok

          public object Serializer : KSerializer<Ok> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.api.User.InteractionLimits.Get.Response.Ok", PolymorphicKind.SEALED) {
              element("CaseInteractionLimitResponse", InteractionLimitResponse.serializer().descriptor)
              element("Empty", Empty.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): Ok {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseInteractionLimitResponse::class to { CaseInteractionLimitResponse(decodeFromJsonElement(InteractionLimitResponse.serializer(), it)) },
                Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: Ok) {
              when(value) {
                is CaseInteractionLimitResponse -> encoder.encodeSerializableValue(InteractionLimitResponse.serializer(), value.value)
                is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
              }
            }
          }
        }

        public data object NoContent : Response
      }
    }

    public class Put internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: InteractionLimit): Response {
        val response = client.put("user/interaction-limits") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: InteractionLimitResponse,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response
      }
    }
  }

  public class Issues internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        filter: Filter? = Filter.Assigned,
        state: State? = State.Open,
        labels: String? = null,
        sort: Sort? = Sort.Created,
        direction: Direction? = Direction.Desc,
        since: Instant? = null,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("user/issues") {
          if (filter != null) {
            this.url.encodedParameters.append("filter", filter.value.encodeURLParameter())
          }
          if (state != null) {
            this.url.encodedParameters.append("state", state.value.encodeURLParameter())
          }
          if (labels != null) {
            this.url.encodedParameters.append("labels", labels.encodeURLParameter())
          }
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (direction != null) {
            this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
          }
          if (since != null) {
            this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
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
          304 -> Response.NotModified
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Filter(
        public val `value`: String,
      ) {
        @SerialName("assigned")
        Assigned("assigned"),
        @SerialName("created")
        Created("created"),
        @SerialName("mentioned")
        Mentioned("mentioned"),
        @SerialName("subscribed")
        Subscribed("subscribed"),
        @SerialName("repos")
        Repos("repos"),
        @SerialName("all")
        All("all"),
        ;
      }

      @Serializable
      public enum class State(
        public val `value`: String,
      ) {
        @SerialName("open")
        Open("open"),
        @SerialName("closed")
        Closed("closed"),
        @SerialName("all")
        All("all"),
        ;
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("created")
        Created("created"),
        @SerialName("updated")
        Updated("updated"),
        @SerialName("comments")
        Comments("comments"),
        ;
      }

      @Serializable
      public enum class Direction(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<Issue>,
        ) : Response

        public data object NotModified : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class Keys internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun keyId(keyId: Long): KeyIdPath = KeyIdPath(client, keyId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/keys") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<Key>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(title: String? = null, key: String): Response {
        val response = client.post("user/keys") {
          contentType(ContentType.Application.Json)
          setBody(Body(title = title, key = key))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
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
        public val title: String? = null,
        public val key: String,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: Key,
        ) : Response

        public data object NotModified : Response

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

    public class KeyIdPath internal constructor(
      private val client: HttpClient,
      private val keyId: Long,
    ) {
      public val delete: Delete = Delete(client, keyId)

      public val `get`: Get = Get(client, keyId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val keyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/keys/${this.keyId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val keyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/keys/${this.keyId}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Key,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class MarketplacePurchases internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val stubbed: Stubbed = Stubbed(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/marketplace_purchases") {
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
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<UserMarketplacePurchase>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Stubbed internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("user/marketplace_purchases/stubbed") {
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
            public val `value`: List<UserMarketplacePurchase>,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class Memberships internal constructor(
    private val client: HttpClient,
  ) {
    public val orgs: Orgs = Orgs(client)

    public class Orgs internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public fun org(org: String): OrgPath = OrgPath(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          state: State? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = client.get("user/memberships/orgs") {
            if (state != null) {
              this.url.encodedParameters.append("state", state.value.encodeURLParameter())
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
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class State(
          public val `value`: String,
        ) {
          @SerialName("active")
          Active("active"),
          @SerialName("pending")
          Pending("pending"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrgMembership>,
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

      public class OrgPath internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val patch: Patch = Patch(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("user/memberships/orgs/${this.org.encodeURLParameter()}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgMembership,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(state: State): Response {
            val response = client.patch("user/memberships/orgs/${this.org.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(Body(state = state))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class State(
            public val `value`: String,
          ) {
            @SerialName("active")
            Active("active"),
            ;
          }

          @Serializable
          internal data class Body(
            public val state: State,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgMembership,
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

  public class Migrations internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun migrationId(migrationId: Long): MigrationIdPath = MigrationIdPath(client, migrationId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/migrations") {
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
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<Migration>,
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

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        lockRepositories: Boolean? = null,
        excludeMetadata: Boolean? = null,
        excludeGitData: Boolean? = null,
        excludeAttachments: Boolean? = null,
        excludeReleases: Boolean? = null,
        excludeOwnerProjects: Boolean? = null,
        orgMetadataOnly: Boolean? = null,
        exclude: List<Exclude>? = null,
        repositories: List<String>,
      ): Response {
        val response = client.post("user/migrations") {
          contentType(ContentType.Application.Json)
          setBody(Body(lockRepositories = lockRepositories, excludeMetadata = excludeMetadata, excludeGitData = excludeGitData, excludeAttachments = excludeAttachments, excludeReleases = excludeReleases, excludeOwnerProjects = excludeOwnerProjects, orgMetadataOnly = orgMetadataOnly, exclude = exclude, repositories = repositories))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Exclude(
        public val `value`: String,
      ) {
        @SerialName("repositories")
        Repositories("repositories"),
        ;
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        @SerialName("lock_repositories")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val lockRepositories: Boolean? = null,
        @SerialName("exclude_metadata")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val excludeMetadata: Boolean? = null,
        @SerialName("exclude_git_data")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val excludeGitData: Boolean? = null,
        @SerialName("exclude_attachments")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val excludeAttachments: Boolean? = null,
        @SerialName("exclude_releases")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val excludeReleases: Boolean? = null,
        @SerialName("exclude_owner_projects")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val excludeOwnerProjects: Boolean? = null,
        @SerialName("org_metadata_only")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val orgMetadataOnly: Boolean? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val exclude: List<Exclude>? = null,
        public val repositories: List<String>,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: Migration,
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

    public class MigrationIdPath internal constructor(
      private val client: HttpClient,
      private val migrationId: Long,
    ) {
      public val `get`: Get = Get(client, migrationId)

      public val archive: Archive = Archive(client, migrationId)

      public val repos: Repos = Repos(client, migrationId)

      public val repositories: Repositories = Repositories(client, migrationId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val migrationId: Long,
      ) {
        public suspend operator fun invoke(exclude: List<String>? = null): Response {
          val response = client.get("user/migrations/${this.migrationId}") {
            exclude?.forEach { this.url.encodedParameters.append("exclude", it.encodeURLParameter()) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Migration,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Archive internal constructor(
        private val client: HttpClient,
        private val migrationId: Long,
      ) {
        public val delete: Delete = Delete(client, migrationId)

        public val `get`: Get = Get(client, migrationId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val migrationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/migrations/${this.migrationId}/archive")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
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

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val migrationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("user/migrations/${this.migrationId}/archive")
            return when (response.status.value) {
              302 -> Response.Found
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object Found : Response

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

      public class Repos internal constructor(
        private val client: HttpClient,
        private val migrationId: Long,
      ) {
        public fun repoName(repoName: String): RepoNamePath = RepoNamePath(client, migrationId, repoName)

        public class RepoNamePath internal constructor(
          private val client: HttpClient,
          private val migrationId: Long,
          private val repoName: String,
        ) {
          public val lock: Lock = Lock(client, migrationId, repoName)

          public class Lock internal constructor(
            private val client: HttpClient,
            private val migrationId: Long,
            private val repoName: String,
          ) {
            public val delete: Delete = Delete(client, migrationId, repoName)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val migrationId: Long,
              private val repoName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/migrations/${this.migrationId}/repos/${this.repoName.encodeURLParameter()}/lock")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  304 -> Response.NotModified
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
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

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class Repositories internal constructor(
        private val client: HttpClient,
        private val migrationId: Long,
      ) {
        public val `get`: Get = Get(client, migrationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val migrationId: Long,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("user/migrations/${this.migrationId}/repositories") {
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
      }
    }
  }

  public class Orgs internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/orgs") {
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
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<OrganizationSimple>,
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

  public class Packages internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val npm: Npm = Npm(client)

    public val maven: Maven = Maven(client)

    public val rubygems: Rubygems = Rubygems(client)

    public val docker: Docker = Docker(client)

    public val nuget: Nuget = Nuget(client)

    public val container: Container = Container(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        packageType: PackageType,
        visibility: Visibility? = null,
        page: Long? = 1L,
        perPage: Long? = 30L,
      ): Response {
        val response = client.get("user/packages") {
          this.url.encodedParameters.append("package_type", packageType.value.encodeURLParameter())
          if (visibility != null) {
            this.url.encodedParameters.append("visibility", visibility.value.encodeURLParameter())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          400 -> Response.BadRequest
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class PackageType(
        public val `value`: String,
      ) {
        @SerialName("npm")
        Npm("npm"),
        @SerialName("maven")
        Maven("maven"),
        @SerialName("rubygems")
        Rubygems("rubygems"),
        @SerialName("docker")
        Docker("docker"),
        @SerialName("nuget")
        Nuget("nuget"),
        @SerialName("container")
        Container("container"),
        ;
      }

      @Serializable
      public enum class Visibility(
        public val `value`: String,
      ) {
        @SerialName("public")
        Public("public"),
        @SerialName("private")
        Private("private"),
        @SerialName("internal")
        Internal("internal"),
        ;
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<Package>,
        ) : Response

        public data object BadRequest : Response
      }
    }

    public class Npm internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/npm/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/npm/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/npm/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/npm/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/npm/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/npm/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/npm/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Maven internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/maven/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/maven/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/maven/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/maven/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/maven/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/maven/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/maven/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Rubygems internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/rubygems/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/rubygems/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/rubygems/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/rubygems/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/rubygems/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/rubygems/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/rubygems/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Docker internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/docker/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/docker/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/docker/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/docker/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/docker/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/docker/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/docker/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Nuget internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/nuget/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/nuget/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/nuget/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/nuget/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/nuget/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/nuget/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/nuget/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Container internal constructor(
      private val client: HttpClient,
    ) {
      public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, packageName)

      public class PackageNamePath internal constructor(
        private val client: HttpClient,
        private val packageName: String,
      ) {
        public val delete: Delete = Delete(client, packageName)

        public val `get`: Get = Get(client, packageName)

        public val restore: Restore = Restore(client, packageName)

        public val versions: Versions = Versions(client, packageName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/packages/container/${this.packageName.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public suspend operator fun invoke(): Package {
            val response = client.get("user/packages/container/${this.packageName.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Restore internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val post: Post = Post(client, packageName)

          public class Post internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(token: String? = null): Response {
              val response = client.post("user/packages/container/${this.packageName.encodeURLParameter()}/restore") {
                if (token != null) {
                  this.url.encodedParameters.append("token", token.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Versions internal constructor(
          private val client: HttpClient,
          private val packageName: String,
        ) {
          public val `get`: Get = Get(client, packageName)

          public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, packageName, packageVersionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 30L,
              state: State? = State.Active,
            ): Response {
              val response = client.get("user/packages/container/${this.packageName.encodeURLParameter()}/versions") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PackageVersion>,
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
            }
          }

          public class PackageVersionIdPath internal constructor(
            private val client: HttpClient,
            private val packageName: String,
            private val packageVersionId: Long,
          ) {
            public val delete: Delete = Delete(client, packageName, packageVersionId)

            public val `get`: Get = Get(client, packageName, packageVersionId)

            public val restore: Restore = Restore(client, packageName, packageVersionId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("user/packages/container/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public suspend operator fun invoke(): PackageVersion {
                val response = client.get("user/packages/container/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Restore internal constructor(
              private val client: HttpClient,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val post: Post = Post(client, packageName, packageVersionId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("user/packages/container/${this.packageName.encodeURLParameter()}/versions/${this.packageVersionId}/restore")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }
  }

  public class PublicEmails internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/public_emails") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<io.github.model.Email>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class Repos internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        visibility: Visibility? = Visibility.All,
        affiliation: String? = "owner,collaborator,organization_member",
        type: Type? = Type.All,
        sort: Sort? = Sort.FullName,
        direction: Direction? = null,
        perPage: Long? = 30L,
        page: Long? = 1L,
        since: Instant? = null,
        before: Instant? = null,
      ): Response {
        val response = client.get("user/repos") {
          if (visibility != null) {
            this.url.encodedParameters.append("visibility", visibility.value.encodeURLParameter())
          }
          if (affiliation != null) {
            this.url.encodedParameters.append("affiliation", affiliation.encodeURLParameter())
          }
          if (type != null) {
            this.url.encodedParameters.append("type", type.value.encodeURLParameter())
          }
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (direction != null) {
            this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
          if (since != null) {
            this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
          }
          if (before != null) {
            this.url.encodedParameters.append("before", before.toString().encodeURLParameter())
          }
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

      @Serializable
      public enum class Visibility(
        public val `value`: String,
      ) {
        @SerialName("all")
        All("all"),
        @SerialName("public")
        Public("public"),
        @SerialName("private")
        Private("private"),
        ;
      }

      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("all")
        All("all"),
        @SerialName("owner")
        Owner("owner"),
        @SerialName("public")
        Public("public"),
        @SerialName("private")
        Private("private"),
        @SerialName("member")
        Member("member"),
        ;
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("created")
        Created("created"),
        @SerialName("updated")
        Updated("updated"),
        @SerialName("pushed")
        Pushed("pushed"),
        @SerialName("full_name")
        FullName("full_name"),
        ;
      }

      @Serializable
      public enum class Direction(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<Repository>,
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

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        name: String,
        description: String? = null,
        homepage: String? = null,
        `private`: Boolean? = null,
        hasIssues: Boolean? = null,
        hasProjects: Boolean? = null,
        hasWiki: Boolean? = null,
        hasDiscussions: Boolean? = null,
        teamId: Long? = null,
        autoInit: Boolean? = null,
        gitignoreTemplate: String? = null,
        licenseTemplate: String? = null,
        allowSquashMerge: Boolean? = null,
        allowMergeCommit: Boolean? = null,
        allowRebaseMerge: Boolean? = null,
        allowAutoMerge: Boolean? = null,
        deleteBranchOnMerge: Boolean? = null,
        squashMergeCommitTitle: SquashMergeCommitTitle? = null,
        squashMergeCommitMessage: SquashMergeCommitMessage? = null,
        mergeCommitTitle: MergeCommitTitle? = null,
        mergeCommitMessage: MergeCommitMessage? = null,
        hasDownloads: Boolean? = null,
        isTemplate: Boolean? = null,
      ): Response {
        val response = client.post("user/repos") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name, description = description, homepage = homepage, private = private, hasIssues = hasIssues, hasProjects = hasProjects, hasWiki = hasWiki, hasDiscussions = hasDiscussions, teamId = teamId, autoInit = autoInit, gitignoreTemplate = gitignoreTemplate, licenseTemplate = licenseTemplate, allowSquashMerge = allowSquashMerge, allowMergeCommit = allowMergeCommit, allowRebaseMerge = allowRebaseMerge, allowAutoMerge = allowAutoMerge, deleteBranchOnMerge = deleteBranchOnMerge, squashMergeCommitTitle = squashMergeCommitTitle, squashMergeCommitMessage = squashMergeCommitMessage, mergeCommitTitle = mergeCommitTitle, mergeCommitMessage = mergeCommitMessage, hasDownloads = hasDownloads, isTemplate = isTemplate))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          400 -> Response.BadRequest(response.body())
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class SquashMergeCommitTitle(
        public val `value`: String,
      ) {
        @SerialName("PR_TITLE")
        PRTITLE("PR_TITLE"),
        @SerialName("COMMIT_OR_PR_TITLE")
        COMMITORPRTITLE("COMMIT_OR_PR_TITLE"),
        ;
      }

      @Serializable
      public enum class SquashMergeCommitMessage(
        public val `value`: String,
      ) {
        @SerialName("PR_BODY")
        PRBODY("PR_BODY"),
        @SerialName("COMMIT_MESSAGES")
        COMMITMESSAGES("COMMIT_MESSAGES"),
        BLANK("BLANK"),
        ;
      }

      @Serializable
      public enum class MergeCommitTitle(
        public val `value`: String,
      ) {
        @SerialName("PR_TITLE")
        PRTITLE("PR_TITLE"),
        @SerialName("MERGE_MESSAGE")
        MERGEMESSAGE("MERGE_MESSAGE"),
        ;
      }

      @Serializable
      public enum class MergeCommitMessage(
        public val `value`: String,
      ) {
        @SerialName("PR_BODY")
        PRBODY("PR_BODY"),
        @SerialName("PR_TITLE")
        PRTITLE("PR_TITLE"),
        BLANK("BLANK"),
        ;
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        public val name: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val description: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val homepage: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val `private`: Boolean? = null,
        @SerialName("has_issues")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val hasIssues: Boolean? = null,
        @SerialName("has_projects")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val hasProjects: Boolean? = null,
        @SerialName("has_wiki")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val hasWiki: Boolean? = null,
        @SerialName("has_discussions")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val hasDiscussions: Boolean? = null,
        @SerialName("team_id")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val teamId: Long? = null,
        @SerialName("auto_init")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val autoInit: Boolean? = null,
        @SerialName("gitignore_template")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val gitignoreTemplate: String? = null,
        @SerialName("license_template")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val licenseTemplate: String? = null,
        @SerialName("allow_squash_merge")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val allowSquashMerge: Boolean? = null,
        @SerialName("allow_merge_commit")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val allowMergeCommit: Boolean? = null,
        @SerialName("allow_rebase_merge")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val allowRebaseMerge: Boolean? = null,
        @SerialName("allow_auto_merge")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val allowAutoMerge: Boolean? = null,
        @SerialName("delete_branch_on_merge")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val deleteBranchOnMerge: Boolean? = null,
        @SerialName("squash_merge_commit_title")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
        @SerialName("squash_merge_commit_message")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
        @SerialName("merge_commit_title")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val mergeCommitTitle: MergeCommitTitle? = null,
        @SerialName("merge_commit_message")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val mergeCommitMessage: MergeCommitMessage? = null,
        @SerialName("has_downloads")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val hasDownloads: Boolean? = null,
        @SerialName("is_template")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val isTemplate: Boolean? = null,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: FullRepository,
        ) : Response

        public data object NotModified : Response

        public data class BadRequest(
          public val `value`: BasicError,
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

  public class RepositoryInvitations internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun invitationId(invitationId: Long): InvitationIdPath = InvitationIdPath(client, invitationId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/repository_invitations") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<RepositoryInvitation>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class InvitationIdPath internal constructor(
      private val client: HttpClient,
      private val invitationId: Long,
    ) {
      public val delete: Delete = Delete(client, invitationId)

      public val patch: Patch = Patch(client, invitationId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val invitationId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/repository_invitations/${this.invitationId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            409 -> Response.Conflict(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class Conflict(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Patch internal constructor(
        private val client: HttpClient,
        private val invitationId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.patch("user/repository_invitations/${this.invitationId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            409 -> Response.Conflict(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class Conflict(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class SocialAccounts internal constructor(
    private val client: HttpClient,
  ) {
    public val delete: Delete = Delete(client)

    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public class Delete internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(accountUrls: List<String>): Response {
        val response = client.delete("user/social_accounts") {
          contentType(ContentType.Application.Json)
          setBody(Body(accountUrls = accountUrls))
        }
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      internal data class Body(
        @SerialName("account_urls")
        public val accountUrls: List<String>,
      )

      public sealed interface Response {
        public data object NoContent : Response

        public data object NotModified : Response

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

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/social_accounts") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<SocialAccount>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(accountUrls: List<String>): Response {
        val response = client.post("user/social_accounts") {
          contentType(ContentType.Application.Json)
          setBody(Body(accountUrls = accountUrls))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      internal data class Body(
        @SerialName("account_urls")
        public val accountUrls: List<String>,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: List<SocialAccount>,
        ) : Response

        public data object NotModified : Response

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

  public class SshSigningKeys internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun sshSigningKeyId(sshSigningKeyId: Long): SshSigningKeyIdPath = SshSigningKeyIdPath(client, sshSigningKeyId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/ssh_signing_keys") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<SshSigningKey>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(title: String? = null, key: String): Response {
        val response = client.post("user/ssh_signing_keys") {
          contentType(ContentType.Application.Json)
          setBody(Body(title = title, key = key))
        }
        return when (response.status.value) {
          201 -> Response.Created(response.body())
          304 -> Response.NotModified
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
        public val title: String? = null,
        public val key: String,
      )

      public sealed interface Response {
        public data class Created(
          public val `value`: SshSigningKey,
        ) : Response

        public data object NotModified : Response

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

    public class SshSigningKeyIdPath internal constructor(
      private val client: HttpClient,
      private val sshSigningKeyId: Long,
    ) {
      public val delete: Delete = Delete(client, sshSigningKeyId)

      public val `get`: Get = Get(client, sshSigningKeyId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val sshSigningKeyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("user/ssh_signing_keys/${this.sshSigningKeyId}")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val sshSigningKeyId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("user/ssh_signing_keys/${this.sshSigningKeyId}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: SshSigningKey,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class Starred internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun json(
        sort: Sort? = Sort.Created,
        direction: Direction? = Direction.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): JsonResponse {
        val response = client.get("user/starred") {
          `header`(HttpHeaders.Accept, ContentType.Application.Json)
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (direction != null) {
            this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> JsonResponse.Ok(response.body())
          304 -> NotModified
          401 -> Unauthorized(response.body())
          403 -> Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public suspend fun vndGithubV3StarJson(
        sort: Sort? = Sort.Created,
        direction: Direction? = Direction.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): VndGithubV3StarJsonResponse {
        val response = client.get("user/starred") {
          `header`(HttpHeaders.Accept, ContentType("application", "vnd.github.v3.star+json"))
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (direction != null) {
            this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> VndGithubV3StarJsonResponse.Ok(response.body())
          304 -> NotModified
          401 -> Unauthorized(response.body())
          403 -> Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("created")
        Created("created"),
        @SerialName("updated")
        Updated("updated"),
        ;
      }

      @Serializable
      public enum class Direction(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }

      public sealed interface JsonResponse {
        public data class Ok(
          public val `value`: List<Repository>,
        ) : JsonResponse
      }

      public sealed interface VndGithubV3StarJsonResponse {
        public data class Ok(
          public val `value`: List<StarredRepository>,
        ) : VndGithubV3StarJsonResponse
      }

      public data object NotModified : JsonResponse, VndGithubV3StarJsonResponse

      public data class Unauthorized(
        public val `value`: BasicError,
      ) : JsonResponse,
          VndGithubV3StarJsonResponse

      public data class Forbidden(
        public val `value`: BasicError,
      ) : JsonResponse,
          VndGithubV3StarJsonResponse
    }

    public class OwnerPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
    ) {
      public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

      public class RepoPath internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("user/starred/${this.owner.encodeURLParameter()}/${this.repo.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
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

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("user/starred/${this.owner.encodeURLParameter()}/${this.repo.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
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

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.put("user/starred/${this.owner.encodeURLParameter()}/${this.repo.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
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

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }
  }

  public class Subscriptions internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/subscriptions") {
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
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<MinimalRepository>,
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

  public class Teams internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = client.get("user/teams") {
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
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<TeamFull>,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class AccountIdPath internal constructor(
    private val client: HttpClient,
    private val accountId: Long,
  ) {
    public val `get`: Get = Get(client, accountId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val accountId: Long,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("user/${this.accountId}")
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @OptIn(ExperimentalSerializationApi::class)
        @JsonClassDiscriminator("user_view_type")
        @Serializable
        public sealed interface Ok : Response {
          /**
           * Private User
           */
          @SerialName("private")
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Private(
            public val login: String,
            public val id: Long,
            @SerialName("node_id")
            public val nodeId: String,
            @SerialName("avatar_url")
            public val avatarUrl: String,
            @SerialName("gravatar_id")
            public val gravatarId: String?,
            public val url: String,
            @SerialName("html_url")
            public val htmlUrl: String,
            @SerialName("followers_url")
            public val followersUrl: String,
            @SerialName("following_url")
            public val followingUrl: String,
            @SerialName("gists_url")
            public val gistsUrl: String,
            @SerialName("starred_url")
            public val starredUrl: String,
            @SerialName("subscriptions_url")
            public val subscriptionsUrl: String,
            @SerialName("organizations_url")
            public val organizationsUrl: String,
            @SerialName("repos_url")
            public val reposUrl: String,
            @SerialName("events_url")
            public val eventsUrl: String,
            @SerialName("received_events_url")
            public val receivedEventsUrl: String,
            public val type: String,
            @SerialName("site_admin")
            public val siteAdmin: Boolean,
            public val name: String?,
            public val company: String?,
            public val blog: String?,
            public val location: String?,
            public val email: String?,
            @SerialName("notification_email")
            public val notificationEmail: String? = null,
            public val hireable: Boolean?,
            public val bio: String?,
            @SerialName("twitter_username")
            public val twitterUsername: String? = null,
            @SerialName("public_repos")
            public val publicRepos: Long,
            @SerialName("public_gists")
            public val publicGists: Long,
            public val followers: Long,
            public val following: Long,
            @SerialName("created_at")
            public val createdAt: Instant,
            @SerialName("updated_at")
            public val updatedAt: Instant,
            @SerialName("private_gists")
            public val privateGists: Long,
            @SerialName("total_private_repos")
            public val totalPrivateRepos: Long,
            @SerialName("owned_private_repos")
            public val ownedPrivateRepos: Long,
            @SerialName("disk_usage")
            public val diskUsage: Long,
            public val collaborators: Long,
            @SerialName("two_factor_authentication")
            public val twoFactorAuthentication: Boolean,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val plan: Plan? = null,
            @SerialName("business_plus")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val businessPlus: Boolean? = null,
            @SerialName("ldap_dn")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val ldapDn: String? = null,
          ) : Ok {
            @Serializable
            public data class Plan(
              public val collaborators: Long,
              public val name: String,
              public val space: Long,
              @SerialName("private_repos")
              public val privateRepos: Long,
            )
          }

          /**
           * Public User
           */
          @SerialName("public")
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Public(
            public val login: String,
            public val id: Long,
            @SerialName("node_id")
            public val nodeId: String,
            @SerialName("avatar_url")
            public val avatarUrl: String,
            @SerialName("gravatar_id")
            public val gravatarId: String?,
            public val url: String,
            @SerialName("html_url")
            public val htmlUrl: String,
            @SerialName("followers_url")
            public val followersUrl: String,
            @SerialName("following_url")
            public val followingUrl: String,
            @SerialName("gists_url")
            public val gistsUrl: String,
            @SerialName("starred_url")
            public val starredUrl: String,
            @SerialName("subscriptions_url")
            public val subscriptionsUrl: String,
            @SerialName("organizations_url")
            public val organizationsUrl: String,
            @SerialName("repos_url")
            public val reposUrl: String,
            @SerialName("events_url")
            public val eventsUrl: String,
            @SerialName("received_events_url")
            public val receivedEventsUrl: String,
            public val type: String,
            @SerialName("site_admin")
            public val siteAdmin: Boolean,
            public val name: String?,
            public val company: String?,
            public val blog: String?,
            public val location: String?,
            public val email: String?,
            @SerialName("notification_email")
            public val notificationEmail: String? = null,
            public val hireable: Boolean?,
            public val bio: String?,
            @SerialName("twitter_username")
            public val twitterUsername: String? = null,
            @SerialName("public_repos")
            public val publicRepos: Long,
            @SerialName("public_gists")
            public val publicGists: Long,
            public val followers: Long,
            public val following: Long,
            @SerialName("created_at")
            public val createdAt: Instant,
            @SerialName("updated_at")
            public val updatedAt: Instant,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val plan: Plan? = null,
            @SerialName("private_gists")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val privateGists: Long? = null,
            @SerialName("total_private_repos")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val totalPrivateRepos: Long? = null,
            @SerialName("owned_private_repos")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val ownedPrivateRepos: Long? = null,
            @SerialName("disk_usage")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val diskUsage: Long? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val collaborators: Long? = null,
          ) : Ok {
            @Serializable
            public data class Plan(
              public val collaborators: Long,
              public val name: String,
              public val space: Long,
              @SerialName("private_repos")
              public val privateRepos: Long,
            )
          }
        }

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class UserIdPath internal constructor(
    private val client: HttpClient,
    private val userId: String,
  ) {
    public val projectsV2: ProjectsV2 = ProjectsV2(client, userId)

    public class ProjectsV2 internal constructor(
      private val client: HttpClient,
      private val userId: String,
    ) {
      public fun projectNumber(projectNumber: Long): ProjectNumberPath = ProjectNumberPath(client, userId, projectNumber)

      public class ProjectNumberPath internal constructor(
        private val client: HttpClient,
        private val userId: String,
        private val projectNumber: Long,
      ) {
        public val drafts: Drafts = Drafts(client, userId, projectNumber)

        public class Drafts internal constructor(
          private val client: HttpClient,
          private val userId: String,
          private val projectNumber: Long,
        ) {
          public val post: Post = Post(client, userId, projectNumber)

          public class Post internal constructor(
            private val client: HttpClient,
            private val userId: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(title: String, body: String? = null): Response {
              val response = client.post("user/${this.userId.encodeURLParameter()}/projectsV2/${this.projectNumber}/drafts") {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, body = body))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val title: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val body: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2ItemSimple,
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
}
