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
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Users internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun userId(userId: String): UserIdPath = UserIdPath(client, userId)

  public fun username(username: String): UsernamePath = UsernamePath(client, username)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(since: Long? = null, perPage: Long? = 30L): Response {
      val response = client.get("/users") {
        since?.let { parameter("since", it) }
        perPage?.let { parameter("per_page", it) }
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
        public val views: Views = Views(client, userId, projectNumber)

        public class Views internal constructor(
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
            public suspend operator fun invoke(
              name: String,
              layout: Layout,
              filter: String? = null,
              visibleFields: List<Long>? = null,
            ): Response {
              val response = client.post("/users/$userId/projectsV2/$projectNumber/views") {
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

            @Serializable
            internal data class Body(
              public val name: String,
              public val layout: Layout,
              public val filter: String? = null,
              @SerialName("visible_fields")
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
    private val client: HttpClient,
    private val username: String,
  ) {
    public val `get`: Get = Get(client, username)

    public val attestations: Attestations = Attestations(client, username)

    public val docker: Docker = Docker(client, username)

    public val events: Events = Events(client, username)

    public val followers: Followers = Followers(client, username)

    public val following: Following = Following(client, username)

    public val gists: Gists = Gists(client, username)

    public val gpgKeys: GpgKeys = GpgKeys(client, username)

    public val hovercard: Hovercard = Hovercard(client, username)

    public val installation: Installation = Installation(client, username)

    public val keys: Keys = Keys(client, username)

    public val orgs: Orgs = Orgs(client, username)

    public val packages: Packages = Packages(client, username)

    public val projectsV2: ProjectsV2 = ProjectsV2(client, username)

    public val receivedEvents: ReceivedEvents = ReceivedEvents(client, username)

    public val repos: Repos = Repos(client, username)

    public val settings: Settings = Settings(client, username)

    public val socialAccounts: SocialAccounts = SocialAccounts(client, username)

    public val sshSigningKeys: SshSigningKeys = SshSigningKeys(client, username)

    public val starred: Starred = Starred(client, username)

    public val subscriptions: Subscriptions = Subscriptions(client, username)

    public class Get internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/users/$username")
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
            public val plan: Plan? = null,
            @SerialName("business_plus")
            public val businessPlus: Boolean? = null,
            @SerialName("ldap_dn")
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
            public val plan: Plan? = null,
            @SerialName("private_gists")
            public val privateGists: Long? = null,
            @SerialName("total_private_repos")
            public val totalPrivateRepos: Long? = null,
            @SerialName("owned_private_repos")
            public val ownedPrivateRepos: Long? = null,
            @SerialName("disk_usage")
            public val diskUsage: Long? = null,
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val bulkList: BulkList = BulkList(client, username)

      public val deleteRequest: DeleteRequest = DeleteRequest(client, username)

      public val digest: Digest = Digest(client, username)

      public fun attestationId(attestationId: Long): AttestationIdPath = AttestationIdPath(client, username, attestationId)

      public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, username, subjectDigest)

      public class BulkList internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val post: Post = Post(client, username)

        public class Post internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public suspend operator fun invoke(
            subjectDigests: List<String>,
            predicateType: String? = null,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
          ): Response = client.post("/users/$username/attestations/bulk-list") {
            perPage?.let { parameter("per_page", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            contentType(ContentType.Application.Json)
            setBody(Body(subjectDigests = subjectDigests, predicateType = predicateType))
          }.body()

          @Serializable
          internal data class Body(
            @SerialName("subject_digests")
            public val subjectDigests: List<String>,
            @SerialName("predicate_type")
            public val predicateType: String? = null,
          )

          @Serializable
          public data class Response(
            @SerialName("attestations_subject_digests")
            public val attestationsSubjectDigests: List<List<AttestationsSubjectDigests>?>? = null,
            @SerialName("page_info")
            public val pageInfo: PageInfo? = null,
          ) {
            @Serializable
            public data class AttestationsSubjectDigests(
              public val bundle: Bundle? = null,
              @SerialName("repository_id")
              public val repositoryId: Long? = null,
              @SerialName("bundle_url")
              public val bundleUrl: String? = null,
            ) {
              /**
               * The bundle of the attestation.
               */
              @Serializable
              public data class Bundle(
                public val mediaType: String? = null,
                public val verificationMaterial: JsonElement? = null,
                public val dsseEnvelope: JsonElement? = null,
              )
            }

            /**
             * Information about the current page.
             */
            @Serializable
            public data class PageInfo(
              @SerialName("has_next")
              public val hasNext: Boolean? = null,
              @SerialName("has_previous")
              public val hasPrevious: Boolean? = null,
              public val next: String? = null,
              public val previous: String? = null,
            )
          }
        }
      }

      public class DeleteRequest internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val post: Post = Post(client, username)

        public class Post internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public suspend operator fun invoke(body: JsonElement): Response {
            val response = client.post("/users/$username/attestations/delete-request") {
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
        private val client: HttpClient,
        private val username: String,
      ) {
        public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, username, subjectDigest)

        public class SubjectDigestPath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val subjectDigest: String,
        ) {
          public val delete: Delete = Delete(client, username, subjectDigest)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val subjectDigest: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/attestations/digest/$subjectDigest")
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
        private val client: HttpClient,
        private val username: String,
        private val attestationId: Long,
      ) {
        public val delete: Delete = Delete(client, username, attestationId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val attestationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/users/$username/attestations/$attestationId")
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
        private val client: HttpClient,
        private val username: String,
        private val subjectDigest: String,
      ) {
        public val `get`: Get = Get(client, username, subjectDigest)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val subjectDigest: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            predicateType: String? = null,
          ): Response {
            val response = client.get("/users/$username/attestations/$subjectDigest") {
              perPage?.let { parameter("per_page", it) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
              predicateType?.let { parameter("predicate_type", it) }
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
            @JvmInline
            @Serializable
            public value class Ok(
              public val attestations: List<Attestations>? = null,
            ) : Response {
              @Serializable
              public data class Attestations(
                public val bundle: Bundle? = null,
                @SerialName("repository_id")
                public val repositoryId: Long? = null,
                @SerialName("bundle_url")
                public val bundleUrl: String? = null,
                public val initiator: String? = null,
              ) {
                /**
                 * The attestation's Sigstore Bundle.
                 * Refer to the [Sigstore Bundle Specification](https://github.com/sigstore/protobuf-specs/blob/main/protos/sigstore_bundle.proto) for more information.
                 */
                @Serializable
                public data class Bundle(
                  public val mediaType: String? = null,
                  public val verificationMaterial: JsonElement? = null,
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val conflicts: Conflicts = Conflicts(client, username)

      public class Conflicts internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val `get`: Get = Get(client, username)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/users/$username/docker/conflicts")
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public val orgs: Orgs = Orgs(client, username)

      public val `public`: Public = Public(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/users/$username/events") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }

      public class Orgs internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public fun org(org: String): OrgPath = OrgPath(client, username, org)

        public class OrgPath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, username, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val org: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/users/$username/events/orgs/$org") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()
          }
        }
      }

      public class Public internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val `get`: Get = Get(client, username)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/users/$username/events/public") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }
      }
    }

    public class Followers internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> = client.get("/users/$username/followers") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Following internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public fun targetUser(targetUser: String): TargetUserPath = TargetUserPath(client, username, targetUser)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> = client.get("/users/$username/following") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }

      public class TargetUserPath internal constructor(
        private val client: HttpClient,
        private val username: String,
        private val targetUser: String,
      ) {
        public val `get`: Get = Get(client, username, targetUser)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val targetUser: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/users/$username/following/$targetUser")
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(
          since: Instant? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = client.get("/users/$username/gists") {
            since?.let { parameter("since", it.toString()) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<GpgKey> = client.get("/users/$username/gpg_keys") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Hovercard internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(subjectType: SubjectType? = null, subjectId: String? = null): Response {
          val response = client.get("/users/$username/hovercard") {
            subjectType?.let { parameter("subject_type", it.value) }
            subjectId?.let { parameter("subject_id", it) }
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(): io.github.model.Installation = client.get("/users/$username/installation").body()
      }
    }

    public class Keys internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<KeySimple> = client.get("/users/$username/keys") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Orgs internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<OrganizationSimple> = client.get("/users/$username/orgs") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Packages internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public val npm: Npm = Npm(client, username)

      public val maven: Maven = Maven(client, username)

      public val rubygems: Rubygems = Rubygems(client, username)

      public val docker: Docker = Docker(client, username)

      public val nuget: Nuget = Nuget(client, username)

      public val container: Container = Container(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(
          packageType: PackageType,
          visibility: Visibility? = null,
          page: Long? = 1L,
          perPage: Long? = 30L,
        ): Response {
          val response = client.get("/users/$username/packages") {
            parameter("package_type", packageType.value)
            visibility?.let { parameter("visibility", it.value) }
            page?.let { parameter("page", it) }
            perPage?.let { parameter("per_page", it) }
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
        private val client: HttpClient,
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/npm/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/npm/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/npm/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/npm/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/npm/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/npm/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/npm/$packageName/versions/$packageVersionId/restore")
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
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/maven/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/maven/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/maven/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/maven/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/maven/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/maven/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/maven/$packageName/versions/$packageVersionId/restore")
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
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/rubygems/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/rubygems/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/rubygems/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/rubygems/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/rubygems/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/rubygems/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/rubygems/$packageName/versions/$packageVersionId/restore")
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
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/docker/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/docker/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/docker/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/docker/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/docker/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/docker/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/docker/$packageName/versions/$packageVersionId/restore")
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
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/nuget/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/nuget/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/nuget/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/nuget/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/nuget/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/nuget/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/nuget/$packageName/versions/$packageVersionId/restore")
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
        private val username: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, username, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, username, packageName)

          public val `get`: Get = Get(client, username, packageName)

          public val restore: Restore = Restore(client, username, packageName)

          public val versions: Versions = Versions(client, username, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/users/$username/packages/container/$packageName")
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
            private val username: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/users/$username/packages/container/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, username, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/users/$username/packages/container/$packageName/restore") {
                  token?.let { parameter("token", it) }
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
            private val username: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, username, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, username, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/packages/container/$packageName/versions")
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
              private val client: HttpClient,
              private val username: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, username, packageName, packageVersionId)

              public val `get`: Get = Get(client, username, packageName, packageVersionId)

              public val restore: Restore = Restore(client, username, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/users/$username/packages/container/$packageName/versions/$packageVersionId")
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
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/users/$username/packages/container/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, username, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val username: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/users/$username/packages/container/$packageName/versions/$packageVersionId/restore")
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public fun projectNumber(projectNumber: Long): ProjectNumberPath = ProjectNumberPath(client, username, projectNumber)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(
          q: String? = null,
          before: String? = null,
          after: String? = null,
          perPage: Long? = 30L,
        ): Response {
          val response = client.get("/users/$username/projectsV2") {
            q?.let { parameter("q", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            perPage?.let { parameter("per_page", it) }
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
        private val client: HttpClient,
        private val username: String,
        private val projectNumber: Long,
      ) {
        public val `get`: Get = Get(client, username, projectNumber)

        public val fields: Fields = Fields(client, username, projectNumber)

        public val items: Items = Items(client, username, projectNumber)

        public val views: Views = Views(client, username, projectNumber)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
          private val projectNumber: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/users/$username/projectsV2/$projectNumber")
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
          private val client: HttpClient,
          private val username: String,
          private val projectNumber: Long,
        ) {
          public val `get`: Get = Get(client, username, projectNumber)

          public val post: Post = Post(client, username, projectNumber)

          public fun fieldId(fieldId: Long): FieldIdPath = FieldIdPath(client, username, projectNumber, fieldId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              before: String? = null,
              after: String? = null,
            ): Response {
              val response = client.get("/users/$username/projectsV2/$projectNumber/fields") {
                perPage?.let { parameter("per_page", it) }
                before?.let { parameter("before", it) }
                after?.let { parameter("after", it) }
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
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(body: NameAndDataType): Response {
              val response = client.post("/users/$username/projectsV2/$projectNumber/fields") {
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

            public suspend operator fun invoke(body: NameAndDataTypeAndSingleSelectOptions): Response {
              val response = client.post("/users/$username/projectsV2/$projectNumber/fields") {
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

            public suspend operator fun invoke(body: NameAndDataTypeAndIterationConfiguration): Response {
              val response = client.post("/users/$username/projectsV2/$projectNumber/fields") {
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

            @Serializable
            public data class NameAndDataType(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
            ) {
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
            public data class NameAndDataTypeAndSingleSelectOptions(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
              @SerialName("single_select_options")
              public val singleSelectOptions: List<ProjectsV2FieldSingleSelectOption>,
            ) {
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
            public data class NameAndDataTypeAndIterationConfiguration(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
              @SerialName("iteration_configuration")
              public val iterationConfiguration: ProjectsV2FieldIterationConfiguration,
            ) {
              @Serializable
              public enum class DataType(
                public val `value`: String,
              ) {
                @SerialName("iteration")
                Iteration("iteration"),
                ;
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
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
            private val fieldId: Long,
          ) {
            public val `get`: Get = Get(client, username, projectNumber, fieldId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val projectNumber: Long,
              private val fieldId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/users/$username/projectsV2/$projectNumber/fields/$fieldId")
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
          private val client: HttpClient,
          private val username: String,
          private val projectNumber: Long,
        ) {
          public val `get`: Get = Get(client, username, projectNumber)

          public val post: Post = Post(client, username, projectNumber)

          public fun itemId(itemId: Long): ItemIdPath = ItemIdPath(client, username, projectNumber, itemId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              before: String? = null,
              after: String? = null,
              perPage: Long? = 30L,
              q: String? = null,
              fields: Fields? = null,
            ): Response {
              val response = client.get("/users/$username/projectsV2/$projectNumber/items") {
                before?.let { parameter("before", it) }
                after?.let { parameter("after", it) }
                perPage?.let { parameter("per_page", it) }
                q?.let { parameter("q", it) }
                fields?.let { parameter("fields", it) }
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
                  return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              type: Type,
              id: Long? = null,
              owner: String? = null,
              repo: String? = null,
              number: Long? = null,
            ): Response {
              val response = client.post("/users/$username/projectsV2/$projectNumber/items") {
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

            @Serializable
            internal data class Body(
              public val type: Type,
              public val id: Long? = null,
              public val owner: String? = null,
              public val repo: String? = null,
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
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
            private val itemId: Long,
          ) {
            public val delete: Delete = Delete(client, username, projectNumber, itemId)

            public val `get`: Get = Get(client, username, projectNumber, itemId)

            public val patch: Patch = Patch(client, username, projectNumber, itemId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/users/$username/projectsV2/$projectNumber/items/$itemId")
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
              private val client: HttpClient,
              private val username: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(fields: Fields? = null): Response {
                val response = client.get("/users/$username/projectsV2/$projectNumber/items/$itemId") {
                  fields?.let { parameter("fields", it) }
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
                    return json.attemptDeserialize(
                      value,
                      CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
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
              private val client: HttpClient,
              private val username: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(fields: List<Fields>): Response {
                val response = client.patch("/users/$username/projectsV2/$projectNumber/items/$itemId") {
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
                      return json.attemptDeserialize(
                        value,
                        CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                      )
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

              @JvmInline
              @Serializable
              internal value class Body(
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
          private val client: HttpClient,
          private val username: String,
          private val projectNumber: Long,
        ) {
          public fun viewNumber(viewNumber: Long): ViewNumberPath = ViewNumberPath(client, username, projectNumber, viewNumber)

          public class ViewNumberPath internal constructor(
            private val client: HttpClient,
            private val username: String,
            private val projectNumber: Long,
            private val viewNumber: Long,
          ) {
            public val items: Items = Items(client, username, projectNumber, viewNumber)

            public class Items internal constructor(
              private val client: HttpClient,
              private val username: String,
              private val projectNumber: Long,
              private val viewNumber: Long,
            ) {
              public val `get`: Get = Get(client, username, projectNumber, viewNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val username: String,
                private val projectNumber: Long,
                private val viewNumber: Long,
              ) {
                public suspend operator fun invoke(
                  fields: Fields? = null,
                  before: String? = null,
                  after: String? = null,
                  perPage: Long? = 30L,
                ): Response {
                  val response = client.get("/users/$username/projectsV2/$projectNumber/views/$viewNumber/items") {
                    fields?.let { parameter("fields", it) }
                    before?.let { parameter("before", it) }
                    after?.let { parameter("after", it) }
                    perPage?.let { parameter("per_page", it) }
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
                      return json.attemptDeserialize(
                        value,
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                      )
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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public val `public`: Public = Public(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/users/$username/received_events") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }

      public class Public internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val `get`: Get = Get(client, username)

        public class Get internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/users/$username/received_events/public") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }
      }
    }

    public class Repos internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(
          type: Type? = Type.Owner,
          sort: Sort? = Sort.FullName,
          direction: Direction? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): List<MinimalRepository> = client.get("/users/$username/repos") {
          type?.let { parameter("type", it.value) }
          sort?.let { parameter("sort", it.value) }
          direction?.let { parameter("direction", it.value) }
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()

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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val billing: Billing = Billing(client, username)

      public class Billing internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public val premiumRequest: PremiumRequest = PremiumRequest(client, username)

        public val usage: Usage = Usage(client, username)

        public class PremiumRequest internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public val usage: Usage = Usage(client, username)

          public class Usage internal constructor(
            private val client: HttpClient,
            private val username: String,
          ) {
            public val `get`: Get = Get(client, username)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                model: String? = null,
                product: String? = null,
              ): Response {
                val response = client.get("/users/$username/settings/billing/premium_request/usage") {
                  year?.let { parameter("year", it) }
                  month?.let { parameter("month", it) }
                  day?.let { parameter("day", it) }
                  model?.let { parameter("model", it) }
                  product?.let { parameter("product", it) }
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

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }

        public class Usage internal constructor(
          private val client: HttpClient,
          private val username: String,
        ) {
          public val `get`: Get = Get(client, username)

          public val summary: Summary = Summary(client, username)

          public class Get internal constructor(
            private val client: HttpClient,
            private val username: String,
          ) {
            public suspend operator fun invoke(
              year: Long? = null,
              month: Long? = null,
              day: Long? = null,
            ): Response {
              val response = client.get("/users/$username/settings/billing/usage") {
                year?.let { parameter("year", it) }
                month?.let { parameter("month", it) }
                day?.let { parameter("day", it) }
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Summary internal constructor(
            private val client: HttpClient,
            private val username: String,
          ) {
            public val `get`: Get = Get(client, username)

            public class Get internal constructor(
              private val client: HttpClient,
              private val username: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                repository: String? = null,
                product: String? = null,
                sku: String? = null,
              ): Response {
                val response = client.get("/users/$username/settings/billing/usage/summary") {
                  year?.let { parameter("year", it) }
                  month?.let { parameter("month", it) }
                  day?.let { parameter("day", it) }
                  repository?.let { parameter("repository", it) }
                  product?.let { parameter("product", it) }
                  sku?.let { parameter("sku", it) }
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

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class SocialAccounts internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SocialAccount> = client.get("/users/$username/social_accounts") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class SshSigningKeys internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SshSigningKey> = client.get("/users/$username/ssh_signing_keys") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class Starred internal constructor(
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(
          sort: Sort? = Sort.Created,
          direction: Direction? = Direction.Desc,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response = client.get("/users/$username/starred") {
          sort?.let { parameter("sort", it.value) }
          direction?.let { parameter("direction", it.value) }
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()

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
      private val client: HttpClient,
      private val username: String,
    ) {
      public val `get`: Get = Get(client, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val username: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<MinimalRepository> = client.get("/users/$username/subscriptions") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }
  }
}
