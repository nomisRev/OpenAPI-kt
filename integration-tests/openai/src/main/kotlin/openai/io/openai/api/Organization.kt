package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.AdminApiKey
import io.openai.model.ApiKeyList
import io.openai.model.AuditLogEventType
import io.openai.model.Certificate
import io.openai.model.CreateGroupBody
import io.openai.model.CreateGroupUserBody
import io.openai.model.DeleteCertificateResponse
import io.openai.model.DeletedRoleAssignmentResource
import io.openai.model.ErrorResponse
import io.openai.model.GroupDeletedResource
import io.openai.model.GroupListResource
import io.openai.model.GroupResourceWithSuccess
import io.openai.model.GroupResponse
import io.openai.model.GroupRoleAssignment
import io.openai.model.GroupUserAssignment
import io.openai.model.GroupUserDeletedResource
import io.openai.model.Invite
import io.openai.model.InviteDeleteResponse
import io.openai.model.InviteListResponse
import io.openai.model.InviteProjectGroupBody
import io.openai.model.InviteRequest
import io.openai.model.ListAuditLogsResponse
import io.openai.model.ListCertificatesResponse
import io.openai.model.ModifyCertificateRequest
import io.openai.model.Project
import io.openai.model.ProjectApiKey
import io.openai.model.ProjectApiKeyDeleteResponse
import io.openai.model.ProjectApiKeyListResponse
import io.openai.model.ProjectCreateRequest
import io.openai.model.ProjectGroup
import io.openai.model.ProjectGroupDeletedResource
import io.openai.model.ProjectGroupListResource
import io.openai.model.ProjectListResponse
import io.openai.model.ProjectRateLimit
import io.openai.model.ProjectRateLimitListResponse
import io.openai.model.ProjectRateLimitUpdateRequest
import io.openai.model.ProjectServiceAccount
import io.openai.model.ProjectServiceAccountCreateRequest
import io.openai.model.ProjectServiceAccountCreateResponse
import io.openai.model.ProjectServiceAccountDeleteResponse
import io.openai.model.ProjectServiceAccountListResponse
import io.openai.model.ProjectUpdateRequest
import io.openai.model.ProjectUser
import io.openai.model.ProjectUserCreateRequest
import io.openai.model.ProjectUserDeleteResponse
import io.openai.model.ProjectUserListResponse
import io.openai.model.ProjectUserUpdateRequest
import io.openai.model.PublicAssignOrganizationGroupRoleBody
import io.openai.model.PublicCreateOrganizationRoleBody
import io.openai.model.PublicRoleListResource
import io.openai.model.PublicUpdateOrganizationRoleBody
import io.openai.model.Role
import io.openai.model.RoleDeletedResource
import io.openai.model.RoleListResource
import io.openai.model.ToggleCertificatesRequest
import io.openai.model.UpdateGroupBody
import io.openai.model.UploadCertificateRequest
import io.openai.model.UsageResponse
import io.openai.model.User
import io.openai.model.UserDeleteResponse
import io.openai.model.UserListResource
import io.openai.model.UserListResponse
import io.openai.model.UserRoleAssignment
import io.openai.model.UserRoleUpdateRequest
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Organization internal constructor(
  private val client: HttpClient,
) {
  public val adminApiKeys: AdminApiKeys = AdminApiKeys(client)

  public val auditLogs: AuditLogs = AuditLogs(client)

  public val certificates: Certificates = Certificates(client)

  public val costs: Costs = Costs(client)

  public val groups: Groups = Groups(client)

  public val invites: Invites = Invites(client)

  public val projects: Projects = Projects(client)

  public val roles: Roles = Roles(client)

  public val usage: Usage = Usage(client)

  public val users: Users = Users(client)

  public class AdminApiKeys internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun keyId(keyId: String): KeyIdPath = KeyIdPath(client, keyId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        after: String? = null,
        order: Order? = Order.Asc,
        limit: Long? = 20L,
      ): ApiKeyList = client.get("/organization/admin_api_keys") {
        after?.let { parameter("after", it) }
        order?.let { parameter("order", it.value) }
        limit?.let { parameter("limit", it) }
      }.body()

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(name: String): AdminApiKey = client.post("/organization/admin_api_keys") {
        contentType(ContentType.Application.Json)
        setBody(Body(name = name))
      }.body()

      @JvmInline
      @Serializable
      internal value class Body(
        public val name: String,
      )
    }

    public class KeyIdPath internal constructor(
      private val client: HttpClient,
      private val keyId: String,
    ) {
      public val delete: Delete = Delete(client, keyId)

      public val `get`: Get = Get(client, keyId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val keyId: String,
      ) {
        public suspend operator fun invoke(): Response = client.delete("/organization/admin_api_keys/$keyId").body()

        @Serializable
        public data class Response(
          public val id: String? = null,
          public val `object`: String? = null,
          public val deleted: Boolean? = null,
        )
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val keyId: String,
      ) {
        public suspend operator fun invoke(): AdminApiKey = client.get("/organization/admin_api_keys/$keyId").body()
      }
    }
  }

  public class AuditLogs internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        effectiveAt: EffectiveAt? = null,
        projectIds: List<String>? = null,
        eventTypes: List<AuditLogEventType>? = null,
        actorIds: List<String>? = null,
        actorEmails: List<String>? = null,
        resourceIds: List<String>? = null,
        limit: Long? = 20L,
        after: String? = null,
        before: String? = null,
      ): ListAuditLogsResponse = client.get("/organization/audit_logs") {
        effectiveAt?.let { parameter("effective_at", it) }
        projectIds?.let { parameter("project_ids[]", it) }
        eventTypes?.let { parameter("event_types[]", it) }
        actorIds?.let { parameter("actor_ids[]", it) }
        actorEmails?.let { parameter("actor_emails[]", it) }
        resourceIds?.let { parameter("resource_ids[]", it) }
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        before?.let { parameter("before", it) }
      }.body()

      @Serializable
      public data class EffectiveAt(
        public val gt: Long? = null,
        public val gte: Long? = null,
        public val lt: Long? = null,
        public val lte: Long? = null,
      )
    }
  }

  public class Certificates internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public val activate: Activate = Activate(client)

    public val deactivate: Deactivate = Deactivate(client)

    public fun certificateId(certificateId: String): CertificateIdPath = CertificateIdPath(client, certificateId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        order: Order? = Order.Desc,
      ): ListCertificatesResponse = client.get("/organization/certificates") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        order?.let { parameter("order", it.value) }
      }.body()

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: UploadCertificateRequest): Certificate = client.post("/organization/certificates") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class Activate internal constructor(
      private val client: HttpClient,
    ) {
      public val post: Post = Post(client)

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse = client.post("/organization/certificates/activate") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }

    public class Deactivate internal constructor(
      private val client: HttpClient,
    ) {
      public val post: Post = Post(client)

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse = client.post("/organization/certificates/deactivate") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }

    public class CertificateIdPath internal constructor(
      private val client: HttpClient,
      private val certificateId: String,
    ) {
      public val delete: Delete = Delete(client, certificateId)

      public val `get`: Get = Get(client, certificateId)

      public val post: Post = Post(client, certificateId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val certificateId: String,
      ) {
        public suspend operator fun invoke(): DeleteCertificateResponse = client.delete("/organization/certificates/$certificateId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val certificateId: String,
      ) {
        public suspend operator fun invoke(include: List<Include>? = null): Certificate = client.get("/organization/certificates/$certificateId") {
          include?.let { parameter("include", it) }
        }.body()

        @Serializable
        public enum class Include(
          public val `value`: String,
        ) {
          @SerialName("content")
          Content("content"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val certificateId: String,
      ) {
        public suspend operator fun invoke(body: ModifyCertificateRequest): Certificate = client.post("/organization/certificates/$certificateId") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }
  }

  public class Costs internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        startTime: Long,
        endTime: Long? = null,
        bucketWidth: BucketWidth? = BucketWidth.`1d`,
        projectIds: List<String>? = null,
        groupBy: List<GroupBy>? = null,
        limit: Long? = 7L,
        page: String? = null,
      ): UsageResponse = client.get("/organization/costs") {
        parameter("start_time", startTime)
        endTime?.let { parameter("end_time", it) }
        bucketWidth?.let { parameter("bucket_width", it) }
        projectIds?.let { parameter("project_ids", it) }
        groupBy?.let { parameter("group_by", it) }
        limit?.let { parameter("limit", it) }
        page?.let { parameter("page", it) }
      }.body()

      @Serializable
      public enum class BucketWidth {
        `1d`,
      }

      @Serializable
      public enum class GroupBy(
        public val `value`: String,
      ) {
        @SerialName("project_id")
        ProjectId("project_id"),
        @SerialName("line_item")
        LineItem("line_item"),
        ;
      }
    }
  }

  public class Groups internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun groupId(groupId: String): GroupIdPath = GroupIdPath(client, groupId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 100L,
        after: String? = null,
        order: Order? = Order.Asc,
      ): GroupListResource = client.get("/organization/groups") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        order?.let { parameter("order", it.value) }
      }.body()

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateGroupBody): GroupResponse = client.post("/organization/groups") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class GroupIdPath internal constructor(
      private val client: HttpClient,
      private val groupId: String,
    ) {
      public val delete: Delete = Delete(client, groupId)

      public val post: Post = Post(client, groupId)

      public val roles: Roles = Roles(client, groupId)

      public val users: Users = Users(client, groupId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val groupId: String,
      ) {
        public suspend operator fun invoke(): GroupDeletedResource = client.delete("/organization/groups/$groupId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val groupId: String,
      ) {
        public suspend operator fun invoke(body: UpdateGroupBody): GroupResourceWithSuccess = client.post("/organization/groups/$groupId") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }

      public class Roles internal constructor(
        private val client: HttpClient,
        private val groupId: String,
      ) {
        public val `get`: Get = Get(client, groupId)

        public val post: Post = Post(client, groupId)

        public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, groupId, roleId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val groupId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            after: String? = null,
            order: Order? = null,
          ): RoleListResource = client.get("/organization/groups/$groupId/roles") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val groupId: String,
        ) {
          public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): GroupRoleAssignment = client.post("/organization/groups/$groupId/roles") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }

        public class RoleIdPath internal constructor(
          private val client: HttpClient,
          private val groupId: String,
          private val roleId: String,
        ) {
          public val delete: Delete = Delete(client, groupId, roleId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val groupId: String,
            private val roleId: String,
          ) {
            public suspend operator fun invoke(): DeletedRoleAssignmentResource = client.delete("/organization/groups/$groupId/roles/$roleId").body()
          }
        }
      }

      public class Users internal constructor(
        private val client: HttpClient,
        private val groupId: String,
      ) {
        public val `get`: Get = Get(client, groupId)

        public val post: Post = Post(client, groupId)

        public fun userId(userId: String): UserIdPath = UserIdPath(client, groupId, userId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val groupId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 100L,
            after: String? = null,
            order: Order? = Order.Desc,
          ): UserListResource = client.get("/organization/groups/$groupId/users") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val groupId: String,
        ) {
          public suspend operator fun invoke(body: CreateGroupUserBody): GroupUserAssignment = client.post("/organization/groups/$groupId/users") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }

        public class UserIdPath internal constructor(
          private val client: HttpClient,
          private val groupId: String,
          private val userId: String,
        ) {
          public val delete: Delete = Delete(client, groupId, userId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val groupId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(): GroupUserDeletedResource = client.delete("/organization/groups/$groupId/users/$userId").body()
          }
        }
      }
    }
  }

  public class Invites internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun inviteId(inviteId: String): InviteIdPath = InviteIdPath(client, inviteId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): InviteListResponse = client.get("/organization/invites") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: InviteRequest): Invite = client.post("/organization/invites") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class InviteIdPath internal constructor(
      private val client: HttpClient,
      private val inviteId: String,
    ) {
      public val delete: Delete = Delete(client, inviteId)

      public val `get`: Get = Get(client, inviteId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val inviteId: String,
      ) {
        public suspend operator fun invoke(): InviteDeleteResponse = client.delete("/organization/invites/$inviteId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val inviteId: String,
      ) {
        public suspend operator fun invoke(): Invite = client.get("/organization/invites/$inviteId").body()
      }
    }
  }

  public class Projects internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun projectId(projectId: String): ProjectIdPath = ProjectIdPath(client, projectId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        includeArchived: Boolean? = false,
      ): ProjectListResponse = client.get("/organization/projects") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        includeArchived?.let { parameter("include_archived", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: ProjectCreateRequest): Project = client.post("/organization/projects") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class ProjectIdPath internal constructor(
      private val client: HttpClient,
      private val projectId: String,
    ) {
      public val `get`: Get = Get(client, projectId)

      public val post: Post = Post(client, projectId)

      public val apiKeys: ApiKeys = ApiKeys(client, projectId)

      public val archive: Archive = Archive(client, projectId)

      public val certificates: Certificates = Certificates(client, projectId)

      public val groups: Groups = Groups(client, projectId)

      public val rateLimits: RateLimits = RateLimits(client, projectId)

      public val serviceAccounts: ServiceAccounts = ServiceAccounts(client, projectId)

      public val users: Users = Users(client, projectId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public suspend operator fun invoke(): Project = client.get("/organization/projects/$projectId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public suspend operator fun invoke(body: ProjectUpdateRequest): Response {
          val response = client.post("/organization/projects/$projectId") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            400 -> Response.BadRequest(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: Project,
          ) : Response

          public data class BadRequest(
            public val `value`: ErrorResponse,
          ) : Response
        }
      }

      public class ApiKeys internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public fun keyId(keyId: String): KeyIdPath = KeyIdPath(client, projectId, keyId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): ProjectApiKeyListResponse = client.get("/organization/projects/$projectId/api_keys") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
          }.body()
        }

        public class KeyIdPath internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val keyId: String,
        ) {
          public val delete: Delete = Delete(client, projectId, keyId)

          public val `get`: Get = Get(client, projectId, keyId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val keyId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/organization/projects/$projectId/api_keys/$keyId")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ProjectApiKeyDeleteResponse,
              ) : Response

              public data class BadRequest(
                public val `value`: ErrorResponse,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val keyId: String,
          ) {
            public suspend operator fun invoke(): ProjectApiKey = client.get("/organization/projects/$projectId/api_keys/$keyId").body()
          }
        }
      }

      public class Archive internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val post: Post = Post(client, projectId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(): Project = client.post("/organization/projects/$projectId/archive").body()
        }
      }

      public class Certificates internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public val activate: Activate = Activate(client, projectId)

        public val deactivate: Deactivate = Deactivate(client, projectId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 20L,
            after: String? = null,
            order: Order? = Order.Desc,
          ): ListCertificatesResponse = client.get("/organization/projects/$projectId/certificates") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }

        public class Activate internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public val post: Post = Post(client, projectId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val projectId: String,
          ) {
            public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse = client.post("/organization/projects/$projectId/certificates/activate") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }.body()
          }
        }

        public class Deactivate internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public val post: Post = Post(client, projectId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val projectId: String,
          ) {
            public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse = client.post("/organization/projects/$projectId/certificates/deactivate") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }.body()
          }
        }
      }

      public class Groups internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public val post: Post = Post(client, projectId)

        public fun groupId(groupId: String): GroupIdPath = GroupIdPath(client, projectId, groupId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 20L,
            after: String? = null,
            order: Order? = Order.Asc,
          ): ProjectGroupListResource = client.get("/organization/projects/$projectId/groups") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(body: InviteProjectGroupBody): ProjectGroup = client.post("/organization/projects/$projectId/groups") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }

        public class GroupIdPath internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val groupId: String,
        ) {
          public val delete: Delete = Delete(client, projectId, groupId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val groupId: String,
          ) {
            public suspend operator fun invoke(): ProjectGroupDeletedResource = client.delete("/organization/projects/$projectId/groups/$groupId").body()
          }
        }
      }

      public class RateLimits internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public fun rateLimitId(rateLimitId: String): RateLimitIdPath = RateLimitIdPath(client, projectId, rateLimitId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 100L,
            after: String? = null,
            before: String? = null,
          ): ProjectRateLimitListResponse = client.get("/organization/projects/$projectId/rate_limits") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
          }.body()
        }

        public class RateLimitIdPath internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val rateLimitId: String,
        ) {
          public val post: Post = Post(client, projectId, rateLimitId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val rateLimitId: String,
          ) {
            public suspend operator fun invoke(body: ProjectRateLimitUpdateRequest): Response {
              val response = client.post("/organization/projects/$projectId/rate_limits/$rateLimitId") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ProjectRateLimit,
              ) : Response

              public data class BadRequest(
                public val `value`: ErrorResponse,
              ) : Response
            }
          }
        }
      }

      public class ServiceAccounts internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public val post: Post = Post(client, projectId)

        public fun serviceAccountId(serviceAccountId: String): ServiceAccountIdPath = ServiceAccountIdPath(client, projectId, serviceAccountId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): Response {
            val response = client.get("/organization/projects/$projectId/service_accounts") {
              limit?.let { parameter("limit", it) }
              after?.let { parameter("after", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ProjectServiceAccountListResponse,
            ) : Response

            public data class BadRequest(
              public val `value`: ErrorResponse,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(body: ProjectServiceAccountCreateRequest): Response {
            val response = client.post("/organization/projects/$projectId/service_accounts") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ProjectServiceAccountCreateResponse,
            ) : Response

            public data class BadRequest(
              public val `value`: ErrorResponse,
            ) : Response
          }
        }

        public class ServiceAccountIdPath internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val serviceAccountId: String,
        ) {
          public val delete: Delete = Delete(client, projectId, serviceAccountId)

          public val `get`: Get = Get(client, projectId, serviceAccountId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val serviceAccountId: String,
          ) {
            public suspend operator fun invoke(): ProjectServiceAccountDeleteResponse = client.delete("/organization/projects/$projectId/service_accounts/$serviceAccountId").body()
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val serviceAccountId: String,
          ) {
            public suspend operator fun invoke(): ProjectServiceAccount = client.get("/organization/projects/$projectId/service_accounts/$serviceAccountId").body()
          }
        }
      }

      public class Users internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public val `get`: Get = Get(client, projectId)

        public val post: Post = Post(client, projectId)

        public fun userId(userId: String): UserIdPath = UserIdPath(client, projectId, userId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): Response {
            val response = client.get("/organization/projects/$projectId/users") {
              limit?.let { parameter("limit", it) }
              after?.let { parameter("after", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ProjectUserListResponse,
            ) : Response

            public data class BadRequest(
              public val `value`: ErrorResponse,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
        ) {
          public suspend operator fun invoke(body: ProjectUserCreateRequest): Response {
            val response = client.post("/organization/projects/$projectId/users") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ProjectUser,
            ) : Response

            public data class BadRequest(
              public val `value`: ErrorResponse,
            ) : Response
          }
        }

        public class UserIdPath internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val userId: String,
        ) {
          public val delete: Delete = Delete(client, projectId, userId)

          public val `get`: Get = Get(client, projectId, userId)

          public val post: Post = Post(client, projectId, userId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/organization/projects/$projectId/users/$userId")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ProjectUserDeleteResponse,
              ) : Response

              public data class BadRequest(
                public val `value`: ErrorResponse,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(): ProjectUser = client.get("/organization/projects/$projectId/users/$userId").body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(body: ProjectUserUpdateRequest): Response {
              val response = client.post("/organization/projects/$projectId/users/$userId") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ProjectUser,
              ) : Response

              public data class BadRequest(
                public val `value`: ErrorResponse,
              ) : Response
            }
          }
        }
      }
    }
  }

  public class Roles internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, roleId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 1_000L,
        after: String? = null,
        order: Order? = Order.Asc,
      ): PublicRoleListResource = client.get("/organization/roles") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        order?.let { parameter("order", it.value) }
      }.body()

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("asc")
        Asc("asc"),
        @SerialName("desc")
        Desc("desc"),
        ;
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: PublicCreateOrganizationRoleBody): Role = client.post("/organization/roles") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class RoleIdPath internal constructor(
      private val client: HttpClient,
      private val roleId: String,
    ) {
      public val delete: Delete = Delete(client, roleId)

      public val post: Post = Post(client, roleId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val roleId: String,
      ) {
        public suspend operator fun invoke(): RoleDeletedResource = client.delete("/organization/roles/$roleId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val roleId: String,
      ) {
        public suspend operator fun invoke(body: PublicUpdateOrganizationRoleBody): Role = client.post("/organization/roles/$roleId") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }
  }

  public class Usage internal constructor(
    private val client: HttpClient,
  ) {
    public val audioSpeeches: AudioSpeeches = AudioSpeeches(client)

    public val audioTranscriptions: AudioTranscriptions = AudioTranscriptions(client)

    public val codeInterpreterSessions: CodeInterpreterSessions = CodeInterpreterSessions(client)

    public val completions: Completions = Completions(client)

    public val embeddings: Embeddings = Embeddings(client)

    public val images: Images = Images(client)

    public val moderations: Moderations = Moderations(client)

    public val vectorStores: VectorStores = VectorStores(client)

    public class AudioSpeeches internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/audio_speeches") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          ;
        }
      }
    }

    public class AudioTranscriptions internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/audio_transcriptions") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          ;
        }
      }
    }

    public class CodeInterpreterSessions internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/code_interpreter_sessions") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          ;
        }
      }
    }

    public class Completions internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          batch: Boolean? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/completions") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          batch?.let { parameter("batch", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          @SerialName("batch")
          Batch("batch"),
          @SerialName("service_tier")
          ServiceTier("service_tier"),
          ;
        }
      }
    }

    public class Embeddings internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/embeddings") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          ;
        }
      }
    }

    public class Images internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          sources: List<Sources>? = null,
          sizes: List<Sizes>? = null,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/images") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          sources?.let { parameter("sources", it) }
          sizes?.let { parameter("sizes", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class Sources(
          public val `value`: String,
        ) {
          @SerialName("image.generation")
          ImageGeneration("image.generation"),
          @SerialName("image.edit")
          ImageEdit("image.edit"),
          @SerialName("image.variation")
          ImageVariation("image.variation"),
          ;
        }

        @Serializable
        public enum class Sizes {
          `256x256`,
          `512x512`,
          `1024x1024`,
          `1792x1792`,
          `1024x1792`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          @SerialName("size")
          Size("size"),
          @SerialName("source")
          Source("source"),
          ;
        }
      }
    }

    public class Moderations internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          userIds: List<String>? = null,
          apiKeyIds: List<String>? = null,
          models: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/moderations") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          userIds?.let { parameter("user_ids", it) }
          apiKeyIds?.let { parameter("api_key_ids", it) }
          models?.let { parameter("models", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          @SerialName("user_id")
          UserId("user_id"),
          @SerialName("api_key_id")
          ApiKeyId("api_key_id"),
          @SerialName("model")
          Model("model"),
          ;
        }
      }
    }

    public class VectorStores internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse = client.get("/organization/usage/vector_stores") {
          parameter("start_time", startTime)
          endTime?.let { parameter("end_time", it) }
          bucketWidth?.let { parameter("bucket_width", it) }
          projectIds?.let { parameter("project_ids", it) }
          groupBy?.let { parameter("group_by", it) }
          limit?.let { parameter("limit", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class BucketWidth {
          `1m`,
          `1h`,
          `1d`,
        }

        @Serializable
        public enum class GroupBy(
          public val `value`: String,
        ) {
          @SerialName("project_id")
          ProjectId("project_id"),
          ;
        }
      }
    }
  }

  public class Users internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public fun userId(userId: String): UserIdPath = UserIdPath(client, userId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        emails: List<String>? = null,
      ): UserListResponse = client.get("/organization/users") {
        limit?.let { parameter("limit", it) }
        after?.let { parameter("after", it) }
        emails?.let { parameter("emails", it) }
      }.body()
    }

    public class UserIdPath internal constructor(
      private val client: HttpClient,
      private val userId: String,
    ) {
      public val delete: Delete = Delete(client, userId)

      public val `get`: Get = Get(client, userId)

      public val post: Post = Post(client, userId)

      public val roles: Roles = Roles(client, userId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val userId: String,
      ) {
        public suspend operator fun invoke(): UserDeleteResponse = client.delete("/organization/users/$userId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val userId: String,
      ) {
        public suspend operator fun invoke(): User = client.get("/organization/users/$userId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val userId: String,
      ) {
        public suspend operator fun invoke(body: UserRoleUpdateRequest): User = client.post("/organization/users/$userId") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }

      public class Roles internal constructor(
        private val client: HttpClient,
        private val userId: String,
      ) {
        public val `get`: Get = Get(client, userId)

        public val post: Post = Post(client, userId)

        public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, userId, roleId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val userId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            after: String? = null,
            order: Order? = null,
          ): RoleListResource = client.get("/organization/users/$userId/roles") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val userId: String,
        ) {
          public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): UserRoleAssignment = client.post("/organization/users/$userId/roles") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }

        public class RoleIdPath internal constructor(
          private val client: HttpClient,
          private val userId: String,
          private val roleId: String,
        ) {
          public val delete: Delete = Delete(client, userId, roleId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val userId: String,
            private val roleId: String,
          ) {
            public suspend operator fun invoke(): DeletedRoleAssignmentResource = client.delete("/organization/users/$userId/roles/$roleId").body()
          }
        }
      }
    }
  }
}
