package io.github.api

import io.github.model.BaseGist
import io.github.model.BasicError
import io.github.model.BillingPremiumRequestUsageReportUser
import io.github.model.BillingUsageReportUser
import io.github.model.BillingUsageSummaryReportUser
import io.github.model.EmptyObject
import io.github.model.Event
import io.github.model.GpgKey
import io.github.model.KeySimple
import io.github.model.MinimalRepository
import io.github.model.Optional
import io.github.model.OrganizationSimple
import io.github.model.Package
import io.github.model.PackageVersion
import io.github.model.ProjectsV2Field
import io.github.model.ProjectsV2FieldIterationConfiguration
import io.github.model.ProjectsV2FieldSingleSelectOption
import io.github.model.ProjectsV2ItemSimple
import io.github.model.ProjectsV2ItemWithContent
import io.github.model.ProjectsV2View
import io.github.model.Repository
import io.github.model.SimpleUser
import io.github.model.SocialAccount
import io.github.model.SshSigningKey
import io.github.model.StarredRepository
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

public class Users internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun userId(userId: String): UserIdPath = UserIdPath(__client, userId)

  public fun username(username: String): UsernamePath = UsernamePath(__client, username)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(since: Long? = null, perPage: Long? = 30L): Response {
      val response = __client.get("users") {
        if (since != null) {
          this.url.encodedParameters.append("since", since.toString())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<SimpleUser>,
      ) : Response

      public data object NotModified : Response
    }
  }

  public class UserIdPath internal constructor(
    private val __client: HttpClient,
    private val _userId: String,
  ) {
    public val projectsV2: ProjectsV2 = ProjectsV2(__client, _userId)

    public class ProjectsV2 internal constructor(
      private val __client: HttpClient,
      private val _userId: String,
    ) {
      public fun projectNumber(projectNumber: Long): ProjectNumberPath = ProjectNumberPath(__client, _userId, projectNumber)

      public class ProjectNumberPath internal constructor(
        private val __client: HttpClient,
        private val _userId: String,
        private val _projectNumber: Long,
      ) {
        public val views: Views = Views(__client, _userId, _projectNumber)

        public class Views internal constructor(
          private val __client: HttpClient,
          private val _userId: String,
          private val _projectNumber: Long,
        ) {
          public val post: Post = Post(__client, _userId, _projectNumber)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _userId: String,
            private val _projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              name: String,
              layout: Layout,
              filter: String? = null,
              visibleFields: List<Long>? = null,
            ): Response {
              val response = __client.post("users/${this._userId.encodeURLParameter()}/projectsV2/${this._projectNumber}/views") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, layout = layout, filter = filter, visibleFields = visibleFields))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> Response.ServiceUnavailable(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Layout(
              public val `value`: String,
            ) {
              @SerialName("table")
              Table("table"),
              @SerialName("board")
              Board("board"),
              @SerialName("roadmap")
              Roadmap("roadmap"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val name: String,
              public val layout: Layout,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val filter: String? = null,
              @SerialName("visible_fields")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val visibleFields: List<Long>? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2View,
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

              public data class ServiceUnavailable(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }
  }

  public class UsernamePath internal constructor(
    private val __client: HttpClient,
    private val _username: String,
  ) {
    public val `get`: Get = Get(__client, _username)

    public val attestations: Attestations = Attestations(__client, _username)

    public val docker: Docker = Docker(__client, _username)

    public val events: Events = Events(__client, _username)

    public val followers: Followers = Followers(__client, _username)

    public val following: Following = Following(__client, _username)

    public val gists: Gists = Gists(__client, _username)

    public val gpgKeys: GpgKeys = GpgKeys(__client, _username)

    public val hovercard: Hovercard = Hovercard(__client, _username)

    public val installation: Installation = Installation(__client, _username)

    public val keys: Keys = Keys(__client, _username)

    public val orgs: Orgs = Orgs(__client, _username)

    public val packages: Packages = Packages(__client, _username)

    public val projectsV2: ProjectsV2 = ProjectsV2(__client, _username)

    public val receivedEvents: ReceivedEvents = ReceivedEvents(__client, _username)

    public val repos: Repos = Repos(__client, _username)

    public val settings: Settings = Settings(__client, _username)

    public val socialAccounts: SocialAccounts = SocialAccounts(__client, _username)

    public val sshSigningKeys: SshSigningKeys = SshSigningKeys(__client, _username)

    public val starred: Starred = Starred(__client, _username)

    public val subscriptions: Subscriptions = Subscriptions(__client, _username)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("users/${this._username.encodeURLParameter()}")
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
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val notificationEmail: Optional<String> = Optional.Absent,
            public val hireable: Boolean?,
            public val bio: String?,
            @SerialName("twitter_username")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val twitterUsername: Optional<String> = Optional.Absent,
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
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val notificationEmail: Optional<String> = Optional.Absent,
            public val hireable: Boolean?,
            public val bio: String?,
            @SerialName("twitter_username")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val twitterUsername: Optional<String> = Optional.Absent,
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

    public class Attestations internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val bulkList: BulkList = BulkList(__client, _username)

      public val deleteRequest: DeleteRequest = DeleteRequest(__client, _username)

      public val digest: Digest = Digest(__client, _username)

      public fun attestationId(attestationId: Long): AttestationIdPath = AttestationIdPath(__client, _username, attestationId)

      public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(__client, _username, subjectDigest)

      public class BulkList internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val post: Post = Post(__client, _username)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public suspend operator fun invoke(
            subjectDigests: List<String>,
            predicateType: String? = null,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
          ): Response {
            val response = __client.post("users/${this._username.encodeURLParameter()}/attestations/bulk-list") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              contentType(ContentType.Application.Json)
              setBody(Body(subjectDigests = subjectDigests, predicateType = predicateType))
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("subject_digests")
            public val subjectDigests: List<String>,
            @SerialName("predicate_type")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val predicateType: String? = null,
          )

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Response(
            @SerialName("attestations_subject_digests")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val attestationsSubjectDigests:
                Map<String, List<AttestationsSubjectDigests>?>? = null,
            @SerialName("page_info")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val pageInfo: PageInfo? = null,
          ) {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class AttestationsSubjectDigests(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val bundle: Bundle? = null,
              @SerialName("repository_id")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val repositoryId: Long? = null,
              @SerialName("bundle_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val bundleUrl: String? = null,
            ) {
              /**
               * The bundle of the attestation.
               */
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Bundle(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val mediaType: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val verificationMaterial: JsonElement? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dsseEnvelope: JsonElement? = null,
              )
            }

            /**
             * Information about the current page.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class PageInfo(
              @SerialName("has_next")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val hasNext: Boolean? = null,
              @SerialName("has_previous")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val hasPrevious: Boolean? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val next: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val previous: String? = null,
            )
          }
        }
      }

      public class DeleteRequest internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val post: Post = Post(__client, _username)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public suspend operator fun invoke(body: JsonElement): Response {
            val response = __client.post("users/${this._username.encodeURLParameter()}/attestations/delete-request") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object Ok : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Digest internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(__client, _username, subjectDigest)

        public class SubjectDigestPath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _subjectDigest: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _subjectDigest)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _subjectDigest: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/attestations/digest/${this._subjectDigest.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object Ok : Response

              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class AttestationIdPath internal constructor(
        private val __client: HttpClient,
        private val _username: String,
        private val _attestationId: Long,
      ) {
        public val delete: Delete = Delete(__client, _username, _attestationId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _attestationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("users/${this._username.encodeURLParameter()}/attestations/${this._attestationId}")
            return when (response.status.value) {
              200 -> Response.Ok
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object Ok : Response

            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class SubjectDigestPath internal constructor(
        private val __client: HttpClient,
        private val _username: String,
        private val _subjectDigest: String,
      ) {
        public val `get`: Get = Get(__client, _username, _subjectDigest)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _subjectDigest: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            predicateType: String? = null,
          ): Response {
            val response = __client.get("users/${this._username.encodeURLParameter()}/attestations/${this._subjectDigest.encodeURLParameter()}") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (predicateType != null) {
                this.url.encodedParameters.append("predicate_type", predicateType.encodeURLParameter())
              }
            }
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              201 -> Response.Created(response.body())
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Ok(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val attestations: List<Attestations>? = null,
            ) : Response {
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Attestations(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val bundle: Bundle? = null,
                @SerialName("repository_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val repositoryId: Long? = null,
                @SerialName("bundle_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val bundleUrl: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val initiator: String? = null,
              ) {
                /**
                 * The attestation's Sigstore Bundle.
                 * Refer to the [Sigstore Bundle Specification](https://github.com/sigstore/protobuf-specs/blob/main/protos/sigstore_bundle.proto) for more information.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Bundle(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val mediaType: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val verificationMaterial: JsonElement? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val dsseEnvelope: JsonElement? = null,
                )
              }
            }

            public data class Created(
              public val `value`: EmptyObject,
            ) : Response

            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Docker internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val conflicts: Conflicts = Conflicts(__client, _username)

      public class Conflicts internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val `get`: Get = Get(__client, _username)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("users/${this._username.encodeURLParameter()}/docker/conflicts")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Package>,
            ) : Response

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

    public class Events internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public val orgs: Orgs = Orgs(__client, _username)

      public val `public`: Public = Public(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/events") {
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

      public class Orgs internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun org(org: String): OrgPath = OrgPath(__client, _username, org)

        public class OrgPath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _org: String,
        ) {
          public val `get`: Get = Get(__client, _username, _org)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _org: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
              val response = __client.get("users/${this._username.encodeURLParameter()}/events/orgs/${this._org.encodeURLParameter()}") {
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
      }

      public class Public internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val `get`: Get = Get(__client, _username)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
            val response = __client.get("users/${this._username.encodeURLParameter()}/events/public") {
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
    }

    public class Followers internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/followers") {
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

    public class Following internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public fun targetUser(targetUser: String): TargetUserPath = TargetUserPath(__client, _username, targetUser)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/following") {
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

      public class TargetUserPath internal constructor(
        private val __client: HttpClient,
        private val _username: String,
        private val _targetUser: String,
      ) {
        public val `get`: Get = Get(__client, _username, _targetUser)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _targetUser: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("users/${this._username.encodeURLParameter()}/following/${this._targetUser.encodeURLParameter()}")
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
      }
    }

    public class Gists internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(
          since: Instant? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = __client.get("users/${this._username.encodeURLParameter()}/gists") {
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
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<BaseGist>,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }

    public class GpgKeys internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<GpgKey> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/gpg_keys") {
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

    public class Hovercard internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(subjectType: SubjectType? = null, subjectId: String? = null): Response {
          val response = __client.get("users/${this._username.encodeURLParameter()}/hovercard") {
            if (subjectType != null) {
              this.url.encodedParameters.append("subject_type", subjectType.value.encodeURLParameter())
            }
            if (subjectId != null) {
              this.url.encodedParameters.append("subject_id", subjectId.encodeURLParameter())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class SubjectType(
          public val `value`: String,
        ) {
          @SerialName("organization")
          Organization("organization"),
          @SerialName("repository")
          Repository("repository"),
          @SerialName("issue")
          Issue("issue"),
          @SerialName("pull_request")
          PullRequest("pull_request"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: io.github.model.Hovercard,
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

    public class Installation internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(): io.github.model.Installation {
          val response = __client.get("users/${this._username.encodeURLParameter()}/installation")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Keys internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<KeySimple> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/keys") {
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

    public class Orgs internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<OrganizationSimple> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/orgs") {
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

    public class Packages internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public val npm: Npm = Npm(__client, _username)

      public val maven: Maven = Maven(__client, _username)

      public val rubygems: Rubygems = Rubygems(__client, _username)

      public val docker: Docker = Docker(__client, _username)

      public val nuget: Nuget = Nuget(__client, _username)

      public val container: Container = Container(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(
          packageType: PackageType,
          visibility: Visibility? = null,
          page: Long? = 1L,
          perPage: Long? = 30L,
        ): Response {
          val response = __client.get("users/${this._username.encodeURLParameter()}/packages") {
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
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
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

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Npm internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/npm/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/maven/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/rubygems/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/docker/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/nuget/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(__client, _username, packageName)

        public class PackageNamePath internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _packageName: String,
        ) {
          public val delete: Delete = Delete(__client, _username, _packageName)

          public val `get`: Get = Get(__client, _username, _packageName)

          public val restore: Restore = Restore(__client, _username, _packageName)

          public val versions: Versions = Versions(__client, _username, _packageName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public suspend operator fun invoke(): Package {
              val response = __client.get("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Restore internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val post: Post = Post(__client, _username, _packageName)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = __client.post("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}/restore") {
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
            private val __client: HttpClient,
            private val _username: String,
            private val _packageName: String,
          ) {
            public val `get`: Get = Get(__client, _username, _packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(__client, _username, _packageName, packageVersionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}/versions")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
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
              private val __client: HttpClient,
              private val _username: String,
              private val _packageName: String,
              private val _packageVersionId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _username, _packageName, _packageVersionId)

              public val `get`: Get = Get(__client, _username, _packageName, _packageVersionId)

              public val restore: Restore =
                  Restore(__client, _username, _packageName, _packageVersionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
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
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Restore internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _packageName: String,
                private val _packageVersionId: Long,
              ) {
                public val post: Post = Post(__client, _username, _packageName, _packageVersionId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _username: String,
                  private val _packageName: String,
                  private val _packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("users/${this._username.encodeURLParameter()}/packages/container/${this._packageName.encodeURLParameter()}/versions/${this._packageVersionId}/restore")
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

    public class ProjectsV2 internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public fun projectNumber(projectNumber: Long): ProjectNumberPath = ProjectNumberPath(__client, _username, projectNumber)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(
          q: String? = null,
          before: String? = null,
          after: String? = null,
          perPage: Long? = 30L,
        ): Response {
          val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2") {
            if (q != null) {
              this.url.encodedParameters.append("q", q.encodeURLParameter())
            }
            if (before != null) {
              this.url.encodedParameters.append("before", before.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
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
            public val `value`: List<io.github.model.ProjectsV2>,
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

      public class ProjectNumberPath internal constructor(
        private val __client: HttpClient,
        private val _username: String,
        private val _projectNumber: Long,
      ) {
        public val `get`: Get = Get(__client, _username, _projectNumber)

        public val fields: Fields = Fields(__client, _username, _projectNumber)

        public val items: Items = Items(__client, _username, _projectNumber)

        public val views: Views = Views(__client, _username, _projectNumber)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _projectNumber: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}")
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
              public val `value`: io.github.model.ProjectsV2,
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

        public class Fields internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _projectNumber: Long,
        ) {
          public val `get`: Get = Get(__client, _username, _projectNumber)

          public val post: Post = Post(__client, _username, _projectNumber)

          public fun fieldId(fieldId: Long): FieldIdPath = FieldIdPath(__client, _username, _projectNumber, fieldId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              before: String? = null,
              after: String? = null,
            ): Response {
              val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/fields") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
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
                public val `value`: List<ProjectsV2Field>,
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
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
          ) {
            public suspend operator fun invoke(body: Body): Response {
              val response = __client.post("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/fields") {
                contentType(ContentType.Application.Json)
                setBody(body)
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

            @Serializable(with = Body.Serializer::class)
            public sealed interface Body {
              @Serializable
              public data class NameAndDataType(
                public val name: String,
                @SerialName("data_type")
                public val dataType: DataType,
              ) : Body {
                @Serializable
                public enum class DataType(
                  public val `value`: String,
                ) {
                  @SerialName("text")
                  Text("text"),
                  @SerialName("number")
                  Number("number"),
                  @SerialName("date")
                  Date("date"),
                  ;
                }
              }

              @Serializable
              public data class SingleSelect(
                public val name: String,
                @SerialName("data_type")
                public val dataType: DataType,
                @SerialName("single_select_options")
                public val singleSelectOptions: List<ProjectsV2FieldSingleSelectOption>,
              ) : Body {
                @Serializable
                public enum class DataType(
                  public val `value`: String,
                ) {
                  @SerialName("single_select")
                  SingleSelect("single_select"),
                  ;
                }
              }

              @Serializable
              public data class Iteration(
                public val name: String,
                @SerialName("data_type")
                public val dataType: DataType,
                @SerialName("iteration_configuration")
                public val iterationConfiguration: ProjectsV2FieldIterationConfiguration,
              ) : Body {
                @Serializable
                public enum class DataType(
                  public val `value`: String,
                ) {
                  @SerialName("iteration")
                  Iteration("iteration"),
                  ;
                }
              }

              public object Serializer : KSerializer<Body> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Fields.Post.Body", PolymorphicKind.SEALED) {
                  element("NameAndDataType", NameAndDataType.serializer().descriptor)
                  element("SingleSelect", SingleSelect.serializer().descriptor)
                  element("Iteration", Iteration.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Body {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  val obj = value as? JsonObject
                  val tag = (obj?.get("data_type") as? JsonPrimitive)?.content
                  when(tag) {
                    "text", "number", "date" -> return json.decodeFromJsonElement(NameAndDataType.serializer(), value)
                    "single_select" -> return json.decodeFromJsonElement(SingleSelect.serializer(), value)
                    "iteration" -> return json.decodeFromJsonElement(Iteration.serializer(), value)
                    else -> throw SerializationException("Unknown tag: " + tag + " for io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Fields.Post.Body")
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Body) {
                  when(value) {
                    is NameAndDataType -> encoder.encodeSerializableValue(NameAndDataType.serializer(), value)
                    is SingleSelect -> encoder.encodeSerializableValue(SingleSelect.serializer(), value)
                    is Iteration -> encoder.encodeSerializableValue(Iteration.serializer(), value)
                  }
                }
              }
            }

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2Field,
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

          public class FieldIdPath internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
            private val _fieldId: Long,
          ) {
            public val `get`: Get = Get(__client, _username, _projectNumber, _fieldId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _projectNumber: Long,
              private val _fieldId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/fields/${this._fieldId}")
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
                  public val `value`: ProjectsV2Field,
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

        public class Items internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _projectNumber: Long,
        ) {
          public val `get`: Get = Get(__client, _username, _projectNumber)

          public val post: Post = Post(__client, _username, _projectNumber)

          public fun itemId(itemId: Long): ItemIdPath = ItemIdPath(__client, _username, _projectNumber, itemId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              before: String? = null,
              after: String? = null,
              perPage: Long? = 30L,
              q: String? = null,
              fields: Fields? = null,
            ): Response {
              val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/items") {
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (q != null) {
                  this.url.encodedParameters.append("q", q.encodeURLParameter())
                }
                if (fields != null) {
                  when (fields) {
                    is Fields.CaseString -> {
                      this.url.encodedParameters.append("fields", fields.value.encodeURLParameter())
                    }
                    is Fields.CaseStrings -> {
                      fields.value.forEach { this.url.encodedParameters.append("fields", it.encodeURLParameter()) }
                    }
                  }
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

            @Serializable(with = Fields.Serializer::class)
            public sealed interface Fields {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Fields

              @Serializable
              @JvmInline
              public value class CaseStrings(
                public val `value`: List<String>,
              ) : Fields

              public object Serializer : KSerializer<Fields> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Items.Get.Fields", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

                override fun deserialize(decoder: Decoder): Fields {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonArray -> json.attemptDeserialize(
                      value,
                      CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Fields) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                  }
                }
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<ProjectsV2ItemWithContent>,
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
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              type: Type,
              id: Long? = null,
              owner: String? = null,
              repo: String? = null,
              number: Long? = null,
            ): Response {
              val response = __client.post("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/items") {
                contentType(ContentType.Application.Json)
                setBody(Body(type = type, id = id, owner = owner, repo = repo, number = number))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Type {
              Issue,
              PullRequest,
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val type: Type,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val id: Long? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val owner: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val repo: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val number: Long? = null,
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

          public class ItemIdPath internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
            private val _itemId: Long,
          ) {
            public val delete: Delete = Delete(__client, _username, _projectNumber, _itemId)

            public val `get`: Get = Get(__client, _username, _projectNumber, _itemId)

            public val patch: Patch = Patch(__client, _username, _projectNumber, _itemId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _projectNumber: Long,
              private val _itemId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/items/${this._itemId}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
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
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _projectNumber: Long,
              private val _itemId: Long,
            ) {
              public suspend operator fun invoke(fields: Fields? = null): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/items/${this._itemId}") {
                  if (fields != null) {
                    when (fields) {
                      is Fields.CaseString -> {
                        this.url.encodedParameters.append("fields", fields.value.encodeURLParameter())
                      }
                      is Fields.CaseStrings -> {
                        fields.value.forEach { this.url.encodedParameters.append("fields", it.encodeURLParameter()) }
                      }
                    }
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

              @Serializable(with = Fields.Serializer::class)
              public sealed interface Fields {
                @Serializable
                @JvmInline
                public value class CaseString(
                  public val `value`: String,
                ) : Fields

                @Serializable
                @JvmInline
                public value class CaseStrings(
                  public val `value`: List<String>,
                ) : Fields

                public object Serializer : KSerializer<Fields> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Items.ItemIdPath.Get.Fields", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Fields {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return when {
                      value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                        value,
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                      )
                      value is JsonArray -> json.attemptDeserialize(
                        value,
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                      )
                      else -> throw UnionSerializationException(value, emptyMap())
                    }
                  }

                  override fun serialize(encoder: Encoder, `value`: Fields) {
                    when(value) {
                      is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                      is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                  }
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProjectsV2ItemWithContent,
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
              private val __client: HttpClient,
              private val _username: String,
              private val _projectNumber: Long,
              private val _itemId: Long,
            ) {
              public suspend operator fun invoke(fields: List<Fields>): Response {
                val response = __client.patch("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/items/${this._itemId}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(fields = fields))
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
              public data class Fields(
                public val id: Long,
                public val `value`: Value?,
              ) {
                /**
                 * The new value for the field:
                 * - For text, number, and date fields, provide the new value directly.
                 * - For single select and iteration fields, provide the ID of the option or iteration.
                 * - To clear the field, set this to null.
                 */
                @Serializable(with = Value.Serializer::class)
                public sealed interface Value {
                  @Serializable
                  @JvmInline
                  public value class CaseString(
                    public val `value`: String,
                  ) : Value

                  @Serializable
                  @JvmInline
                  public value class CaseDouble(
                    public val `value`: Double,
                  ) : Value

                  public object Serializer : KSerializer<Value> {
                    @OptIn(
                      InternalSerializationApi::class,
                      ExperimentalSerializationApi::class,
                    )
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Items.ItemIdPath.Patch.Fields.Value", PolymorphicKind.SEALED) {
                      element("CaseString", String.serializer().descriptor)
                      element("CaseDouble", Double.serializer().descriptor)
                    }

                    override fun deserialize(decoder: Decoder): Value {
                      val value = decoder.decodeSerializableValue(JsonElement.serializer())
                      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                      return when {
                        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                          value,
                          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                        value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                          value,
                          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                        )
                        else -> throw UnionSerializationException(value, emptyMap())
                      }
                    }

                    override fun serialize(encoder: Encoder, `value`: Value) {
                      when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                      }
                    }
                  }
                }
              }

              @Serializable
              internal data class Body(
                public val fields: List<Fields>,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProjectsV2ItemWithContent,
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

        public class Views internal constructor(
          private val __client: HttpClient,
          private val _username: String,
          private val _projectNumber: Long,
        ) {
          public fun viewNumber(viewNumber: Long): ViewNumberPath = ViewNumberPath(__client, _username, _projectNumber, viewNumber)

          public class ViewNumberPath internal constructor(
            private val __client: HttpClient,
            private val _username: String,
            private val _projectNumber: Long,
            private val _viewNumber: Long,
          ) {
            public val items: Items = Items(__client, _username, _projectNumber, _viewNumber)

            public class Items internal constructor(
              private val __client: HttpClient,
              private val _username: String,
              private val _projectNumber: Long,
              private val _viewNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _username, _projectNumber, _viewNumber)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _username: String,
                private val _projectNumber: Long,
                private val _viewNumber: Long,
              ) {
                public suspend operator fun invoke(
                  fields: Fields? = null,
                  before: String? = null,
                  after: String? = null,
                  perPage: Long? = 30L,
                ): Response {
                  val response = __client.get("users/${this._username.encodeURLParameter()}/projectsV2/${this._projectNumber}/views/${this._viewNumber}/items") {
                    if (fields != null) {
                      when (fields) {
                        is Fields.CaseString -> {
                          this.url.encodedParameters.append("fields", fields.value.encodeURLParameter())
                        }
                        is Fields.CaseStrings -> {
                          fields.value.forEach { this.url.encodedParameters.append("fields", it.encodeURLParameter()) }
                        }
                      }
                    }
                    if (before != null) {
                      this.url.encodedParameters.append("before", before.encodeURLParameter())
                    }
                    if (after != null) {
                      this.url.encodedParameters.append("after", after.encodeURLParameter())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
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

                @Serializable(with = Fields.Serializer::class)
                public sealed interface Fields {
                  @Serializable
                  @JvmInline
                  public value class CaseString(
                    public val `value`: String,
                  ) : Fields

                  @Serializable
                  @JvmInline
                  public value class CaseStrings(
                    public val `value`: List<String>,
                  ) : Fields

                  public object Serializer : KSerializer<Fields> {
                    @OptIn(
                      InternalSerializationApi::class,
                      ExperimentalSerializationApi::class,
                    )
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.api.Users.UsernamePath.ProjectsV2.ProjectNumberPath.Views.ViewNumberPath.Items.Get.Fields", PolymorphicKind.SEALED) {
                      element("CaseString", String.serializer().descriptor)
                      element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                    }

                    override fun deserialize(decoder: Decoder): Fields {
                      val value = decoder.decodeSerializableValue(JsonElement.serializer())
                      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                      return when {
                        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                          value,
                          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                        value is JsonArray -> json.attemptDeserialize(
                          value,
                          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                        )
                        else -> throw UnionSerializationException(value, emptyMap())
                      }
                    }

                    override fun serialize(encoder: Encoder, `value`: Fields) {
                      when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                      }
                    }
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<ProjectsV2ItemWithContent>,
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
        }
      }
    }

    public class ReceivedEvents internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public val `public`: Public = Public(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/received_events") {
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

      public class Public internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val `get`: Get = Get(__client, _username)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
            val response = __client.get("users/${this._username.encodeURLParameter()}/received_events/public") {
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
    }

    public class Repos internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(
          type: Type? = Type.Owner,
          sort: Sort? = Sort.FullName,
          direction: Direction? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): List<MinimalRepository> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/repos") {
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
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("owner")
          Owner("owner"),
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
      }
    }

    public class Settings internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val billing: Billing = Billing(__client, _username)

      public class Billing internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public val premiumRequest: PremiumRequest = PremiumRequest(__client, _username)

        public val usage: Usage = Usage(__client, _username)

        public class PremiumRequest internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public val usage: Usage = Usage(__client, _username)

          public class Usage internal constructor(
            private val __client: HttpClient,
            private val _username: String,
          ) {
            public val `get`: Get = Get(__client, _username)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                model: String? = null,
                product: String? = null,
              ): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/settings/billing/premium_request/usage") {
                  if (year != null) {
                    this.url.encodedParameters.append("year", year.toString())
                  }
                  if (month != null) {
                    this.url.encodedParameters.append("month", month.toString())
                  }
                  if (day != null) {
                    this.url.encodedParameters.append("day", day.toString())
                  }
                  if (model != null) {
                    this.url.encodedParameters.append("model", model.encodeURLParameter())
                  }
                  if (product != null) {
                    this.url.encodedParameters.append("product", product.encodeURLParameter())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: BillingPremiumRequestUsageReportUser,
                ) : Response

                public data class BadRequest(
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
          }
        }

        public class Usage internal constructor(
          private val __client: HttpClient,
          private val _username: String,
        ) {
          public val `get`: Get = Get(__client, _username)

          public val summary: Summary = Summary(__client, _username)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _username: String,
          ) {
            public suspend operator fun invoke(
              year: Long? = null,
              month: Long? = null,
              day: Long? = null,
            ): Response {
              val response = __client.get("users/${this._username.encodeURLParameter()}/settings/billing/usage") {
                if (year != null) {
                  this.url.encodedParameters.append("year", year.toString())
                }
                if (month != null) {
                  this.url.encodedParameters.append("month", month.toString())
                }
                if (day != null) {
                  this.url.encodedParameters.append("day", day.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                500 -> Response.InternalServerError(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: BillingUsageReportUser,
              ) : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
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

          public class Summary internal constructor(
            private val __client: HttpClient,
            private val _username: String,
          ) {
            public val `get`: Get = Get(__client, _username)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _username: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                repository: String? = null,
                product: String? = null,
                sku: String? = null,
              ): Response {
                val response = __client.get("users/${this._username.encodeURLParameter()}/settings/billing/usage/summary") {
                  if (year != null) {
                    this.url.encodedParameters.append("year", year.toString())
                  }
                  if (month != null) {
                    this.url.encodedParameters.append("month", month.toString())
                  }
                  if (day != null) {
                    this.url.encodedParameters.append("day", day.toString())
                  }
                  if (repository != null) {
                    this.url.encodedParameters.append("repository", repository.encodeURLParameter())
                  }
                  if (product != null) {
                    this.url.encodedParameters.append("product", product.encodeURLParameter())
                  }
                  if (sku != null) {
                    this.url.encodedParameters.append("sku", sku.encodeURLParameter())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: BillingUsageSummaryReportUser,
                ) : Response

                public data class BadRequest(
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
          }
        }
      }
    }

    public class SocialAccounts internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SocialAccount> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/social_accounts") {
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

    public class SshSigningKeys internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SshSigningKey> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/ssh_signing_keys") {
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

    public class Starred internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(
          sort: Sort? = Sort.Created,
          direction: Direction? = Direction.Desc,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = __client.get("users/${this._username.encodeURLParameter()}/starred") {
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
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
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

        @Serializable(with = Response.Serializer::class)
        public sealed interface Response {
          @Serializable
          @JvmInline
          public value class CaseStarredRepositoryList(
            public val `value`: List<StarredRepository>,
          ) : Response

          @Serializable
          @JvmInline
          public value class CaseRepositoryList(
            public val `value`: List<Repository>,
          ) : Response

          public object Serializer : KSerializer<Response> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.api.Users.UsernamePath.Starred.Get.Response", PolymorphicKind.SEALED) {
              element("CaseStarredRepositoryList", ListSerializer(StarredRepository.serializer()).descriptor)
              element("CaseRepositoryList", ListSerializer(Repository.serializer()).descriptor)
            }

            override fun deserialize(decoder: Decoder): Response {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseStarredRepositoryList::class to { CaseStarredRepositoryList(decodeFromJsonElement(ListSerializer(StarredRepository.serializer()), it)) },
                CaseRepositoryList::class to { CaseRepositoryList(decodeFromJsonElement(ListSerializer(Repository.serializer()), it)) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: Response) {
              when(value) {
                is CaseStarredRepositoryList -> encoder.encodeSerializableValue(ListSerializer(StarredRepository.serializer()), value.value)
                is CaseRepositoryList -> encoder.encodeSerializableValue(ListSerializer(Repository.serializer()), value.value)
              }
            }
          }
        }
      }
    }

    public class Subscriptions internal constructor(
      private val __client: HttpClient,
      private val _username: String,
    ) {
      public val `get`: Get = Get(__client, _username)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<MinimalRepository> {
          val response = __client.get("users/${this._username.encodeURLParameter()}/subscriptions") {
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
  }
}
