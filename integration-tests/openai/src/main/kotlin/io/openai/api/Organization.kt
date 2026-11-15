package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
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
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Organization internal constructor(
  private val __client: HttpClient,
) {
  public val adminApiKeys: AdminApiKeys = AdminApiKeys(__client)

  public val auditLogs: AuditLogs = AuditLogs(__client)

  public val certificates: Certificates = Certificates(__client)

  public val costs: Costs = Costs(__client)

  public val groups: Groups = Groups(__client)

  public val invites: Invites = Invites(__client)

  public val projects: Projects = Projects(__client)

  public val roles: Roles = Roles(__client)

  public val usage: Usage = Usage(__client)

  public val users: Users = Users(__client)

  public class AdminApiKeys internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun keyId(keyId: String): KeyIdPath = KeyIdPath(__client, keyId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        after: String? = null,
        order: Order? = Order.Asc,
        limit: Long? = 20L,
      ): ApiKeyList {
        val response = __client.get("organization/admin_api_keys") {
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(name: String): AdminApiKey {
        val response = __client.post("organization/admin_api_keys") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name))
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      @Serializable
      internal data class Body(
        public val name: String,
      )
    }

    public class KeyIdPath internal constructor(
      private val __client: HttpClient,
      private val _keyId: String,
    ) {
      public val delete: Delete = Delete(__client, _keyId)

      public val `get`: Get = Get(__client, _keyId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _keyId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.delete("organization/admin_api_keys/${this._keyId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Response(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val id: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val `object`: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val deleted: Boolean? = null,
        )
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _keyId: String,
      ) {
        public suspend operator fun invoke(): AdminApiKey {
          val response = __client.get("organization/admin_api_keys/${this._keyId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class AuditLogs internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
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
      ): ListAuditLogsResponse {
        val response = __client.get("organization/audit_logs") {
          if (effectiveAt != null) {
            if (effectiveAt.gt != null) {
              this.url.encodedParameters.append("gt", effectiveAt.gt.toString())
            }
            if (effectiveAt.gte != null) {
              this.url.encodedParameters.append("gte", effectiveAt.gte.toString())
            }
            if (effectiveAt.lt != null) {
              this.url.encodedParameters.append("lt", effectiveAt.lt.toString())
            }
            if (effectiveAt.lte != null) {
              this.url.encodedParameters.append("lte", effectiveAt.lte.toString())
            }
          }
          projectIds?.forEach { this.url.encodedParameters.append("project_ids%5B%5D", it.encodeURLParameter()) }
          eventTypes?.forEach { this.url.encodedParameters.append("event_types%5B%5D", it.value.encodeURLParameter()) }
          actorIds?.forEach { this.url.encodedParameters.append("actor_ids%5B%5D", it.encodeURLParameter()) }
          actorEmails?.forEach { this.url.encodedParameters.append("actor_emails%5B%5D", it.encodeURLParameter()) }
          resourceIds?.forEach { this.url.encodedParameters.append("resource_ids%5B%5D", it.encodeURLParameter()) }
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (before != null) {
            this.url.encodedParameters.append("before", before.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class EffectiveAt(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val gt: Long? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val gte: Long? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val lt: Long? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val lte: Long? = null,
      )
    }
  }

  public class Certificates internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public val activate: Activate = Activate(__client)

    public val deactivate: Deactivate = Deactivate(__client)

    public fun certificateId(certificateId: String): CertificateIdPath = CertificateIdPath(__client, certificateId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        order: Order? = Order.Desc,
      ): ListCertificatesResponse {
        val response = __client.get("organization/certificates") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: UploadCertificateRequest): Certificate {
        val response = __client.post("organization/certificates") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Activate internal constructor(
      private val __client: HttpClient,
    ) {
      public val post: Post = Post(__client)

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse {
          val response = __client.post("organization/certificates/activate") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Deactivate internal constructor(
      private val __client: HttpClient,
    ) {
      public val post: Post = Post(__client)

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse {
          val response = __client.post("organization/certificates/deactivate") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class CertificateIdPath internal constructor(
      private val __client: HttpClient,
      private val _certificateId: String,
    ) {
      public val delete: Delete = Delete(__client, _certificateId)

      public val `get`: Get = Get(__client, _certificateId)

      public val post: Post = Post(__client, _certificateId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _certificateId: String,
      ) {
        public suspend operator fun invoke(): DeleteCertificateResponse {
          val response = __client.delete("organization/certificates/${this._certificateId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _certificateId: String,
      ) {
        public suspend operator fun invoke(include: List<Include>? = null): Certificate {
          val response = __client.get("organization/certificates/${this._certificateId.encodeURLParameter()}") {
            include?.forEach { this.url.encodedParameters.append("include", it.value.encodeURLParameter()) }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
        private val __client: HttpClient,
        private val _certificateId: String,
      ) {
        public suspend operator fun invoke(body: ModifyCertificateRequest): Certificate {
          val response = __client.post("organization/certificates/${this._certificateId.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class Costs internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        startTime: Long,
        endTime: Long? = null,
        bucketWidth: BucketWidth? = BucketWidth.`1d`,
        projectIds: List<String>? = null,
        groupBy: List<GroupBy>? = null,
        limit: Long? = 7L,
        page: String? = null,
      ): UsageResponse {
        val response = __client.get("organization/costs") {
          this.url.encodedParameters.append("start_time", startTime.toString())
          if (endTime != null) {
            this.url.encodedParameters.append("end_time", endTime.toString())
          }
          if (bucketWidth != null) {
            this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
          }
          projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
          groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun groupId(groupId: String): GroupIdPath = GroupIdPath(__client, groupId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 100L,
        after: String? = null,
        order: Order? = Order.Asc,
      ): GroupListResource {
        val response = __client.get("organization/groups") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateGroupBody): GroupResponse {
        val response = __client.post("organization/groups") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class GroupIdPath internal constructor(
      private val __client: HttpClient,
      private val _groupId: String,
    ) {
      public val delete: Delete = Delete(__client, _groupId)

      public val post: Post = Post(__client, _groupId)

      public val roles: Roles = Roles(__client, _groupId)

      public val users: Users = Users(__client, _groupId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _groupId: String,
      ) {
        public suspend operator fun invoke(): GroupDeletedResource {
          val response = __client.delete("organization/groups/${this._groupId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _groupId: String,
      ) {
        public suspend operator fun invoke(body: UpdateGroupBody): GroupResourceWithSuccess {
          val response = __client.post("organization/groups/${this._groupId.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Roles internal constructor(
        private val __client: HttpClient,
        private val _groupId: String,
      ) {
        public val `get`: Get = Get(__client, _groupId)

        public val post: Post = Post(__client, _groupId)

        public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, _groupId, roleId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _groupId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            after: String? = null,
            order: Order? = null,
          ): RoleListResource {
            val response = __client.get("organization/groups/${this._groupId.encodeURLParameter()}/roles") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _groupId: String,
        ) {
          public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): GroupRoleAssignment {
            val response = __client.post("organization/groups/${this._groupId.encodeURLParameter()}/roles") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class RoleIdPath internal constructor(
          private val __client: HttpClient,
          private val _groupId: String,
          private val _roleId: String,
        ) {
          public val delete: Delete = Delete(__client, _groupId, _roleId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _groupId: String,
            private val _roleId: String,
          ) {
            public suspend operator fun invoke(): DeletedRoleAssignmentResource {
              val response = __client.delete("organization/groups/${this._groupId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Users internal constructor(
        private val __client: HttpClient,
        private val _groupId: String,
      ) {
        public val `get`: Get = Get(__client, _groupId)

        public val post: Post = Post(__client, _groupId)

        public fun userId(userId: String): UserIdPath = UserIdPath(__client, _groupId, userId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _groupId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 100L,
            after: String? = null,
            order: Order? = Order.Desc,
          ): UserListResource {
            val response = __client.get("organization/groups/${this._groupId.encodeURLParameter()}/users") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _groupId: String,
        ) {
          public suspend operator fun invoke(body: CreateGroupUserBody): GroupUserAssignment {
            val response = __client.post("organization/groups/${this._groupId.encodeURLParameter()}/users") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class UserIdPath internal constructor(
          private val __client: HttpClient,
          private val _groupId: String,
          private val _userId: String,
        ) {
          public val delete: Delete = Delete(__client, _groupId, _userId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _groupId: String,
            private val _userId: String,
          ) {
            public suspend operator fun invoke(): GroupUserDeletedResource {
              val response = __client.delete("organization/groups/${this._groupId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }
    }
  }

  public class Invites internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun inviteId(inviteId: String): InviteIdPath = InviteIdPath(__client, inviteId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): InviteListResponse {
        val response = __client.get("organization/invites") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: InviteRequest): Invite {
        val response = __client.post("organization/invites") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class InviteIdPath internal constructor(
      private val __client: HttpClient,
      private val _inviteId: String,
    ) {
      public val delete: Delete = Delete(__client, _inviteId)

      public val `get`: Get = Get(__client, _inviteId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _inviteId: String,
      ) {
        public suspend operator fun invoke(): InviteDeleteResponse {
          val response = __client.delete("organization/invites/${this._inviteId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _inviteId: String,
      ) {
        public suspend operator fun invoke(): Invite {
          val response = __client.get("organization/invites/${this._inviteId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class Projects internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun projectId(projectId: String): ProjectIdPath = ProjectIdPath(__client, projectId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        includeArchived: Boolean? = false,
      ): ProjectListResponse {
        val response = __client.get("organization/projects") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (includeArchived != null) {
            this.url.encodedParameters.append("include_archived", includeArchived.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: ProjectCreateRequest): Project {
        val response = __client.post("organization/projects") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class ProjectIdPath internal constructor(
      private val __client: HttpClient,
      private val _projectId: String,
    ) {
      public val `get`: Get = Get(__client, _projectId)

      public val post: Post = Post(__client, _projectId)

      public val apiKeys: ApiKeys = ApiKeys(__client, _projectId)

      public val archive: Archive = Archive(__client, _projectId)

      public val certificates: Certificates = Certificates(__client, _projectId)

      public val groups: Groups = Groups(__client, _projectId)

      public val rateLimits: RateLimits = RateLimits(__client, _projectId)

      public val serviceAccounts: ServiceAccounts = ServiceAccounts(__client, _projectId)

      public val users: Users = Users(__client, _projectId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public suspend operator fun invoke(): Project {
          val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public suspend operator fun invoke(body: ProjectUpdateRequest): Response {
          val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}") {
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
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public fun keyId(keyId: String): KeyIdPath = KeyIdPath(__client, _projectId, keyId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): ProjectApiKeyListResponse {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/api_keys") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class KeyIdPath internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _keyId: String,
        ) {
          public val delete: Delete = Delete(__client, _projectId, _keyId)

          public val `get`: Get = Get(__client, _projectId, _keyId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _keyId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("organization/projects/${this._projectId.encodeURLParameter()}/api_keys/${this._keyId.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _projectId: String,
            private val _keyId: String,
          ) {
            public suspend operator fun invoke(): ProjectApiKey {
              val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/api_keys/${this._keyId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Archive internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val post: Post = Post(__client, _projectId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(): Project {
            val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/archive")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Certificates internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public val activate: Activate = Activate(__client, _projectId)

        public val deactivate: Deactivate = Deactivate(__client, _projectId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 20L,
            after: String? = null,
            order: Order? = Order.Desc,
          ): ListCertificatesResponse {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/certificates") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public val post: Post = Post(__client, _projectId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
          ) {
            public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse {
              val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/certificates/activate") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Deactivate internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public val post: Post = Post(__client, _projectId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
          ) {
            public suspend operator fun invoke(body: ToggleCertificatesRequest): ListCertificatesResponse {
              val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/certificates/deactivate") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Groups internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public val post: Post = Post(__client, _projectId)

        public fun groupId(groupId: String): GroupIdPath = GroupIdPath(__client, _projectId, groupId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 20L,
            after: String? = null,
            order: Order? = Order.Asc,
          ): ProjectGroupListResource {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/groups") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(body: InviteProjectGroupBody): ProjectGroup {
            val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/groups") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class GroupIdPath internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _groupId: String,
        ) {
          public val delete: Delete = Delete(__client, _projectId, _groupId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _groupId: String,
          ) {
            public suspend operator fun invoke(): ProjectGroupDeletedResource {
              val response = __client.delete("organization/projects/${this._projectId.encodeURLParameter()}/groups/${this._groupId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class RateLimits internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public fun rateLimitId(rateLimitId: String): RateLimitIdPath = RateLimitIdPath(__client, _projectId, rateLimitId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = 100L,
            after: String? = null,
            before: String? = null,
          ): ProjectRateLimitListResponse {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/rate_limits") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class RateLimitIdPath internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _rateLimitId: String,
        ) {
          public val post: Post = Post(__client, _projectId, _rateLimitId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _rateLimitId: String,
          ) {
            public suspend operator fun invoke(body: ProjectRateLimitUpdateRequest): Response {
              val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/rate_limits/${this._rateLimitId.encodeURLParameter()}") {
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
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public val post: Post = Post(__client, _projectId)

        public fun serviceAccountId(serviceAccountId: String): ServiceAccountIdPath = ServiceAccountIdPath(__client, _projectId, serviceAccountId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): Response {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/service_accounts") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
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
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(body: ProjectServiceAccountCreateRequest): Response {
            val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/service_accounts") {
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
          private val __client: HttpClient,
          private val _projectId: String,
          private val _serviceAccountId: String,
        ) {
          public val delete: Delete = Delete(__client, _projectId, _serviceAccountId)

          public val `get`: Get = Get(__client, _projectId, _serviceAccountId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _serviceAccountId: String,
          ) {
            public suspend operator fun invoke(): ProjectServiceAccountDeleteResponse {
              val response = __client.delete("organization/projects/${this._projectId.encodeURLParameter()}/service_accounts/${this._serviceAccountId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _serviceAccountId: String,
          ) {
            public suspend operator fun invoke(): ProjectServiceAccount {
              val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/service_accounts/${this._serviceAccountId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Users internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public val `get`: Get = Get(__client, _projectId)

        public val post: Post = Post(__client, _projectId)

        public fun userId(userId: String): UserIdPath = UserIdPath(__client, _projectId, userId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(limit: Long? = 20L, after: String? = null): Response {
            val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/users") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
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
          private val __client: HttpClient,
          private val _projectId: String,
        ) {
          public suspend operator fun invoke(body: ProjectUserCreateRequest): Response {
            val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/users") {
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
          private val __client: HttpClient,
          private val _projectId: String,
          private val _userId: String,
        ) {
          public val delete: Delete = Delete(__client, _projectId, _userId)

          public val `get`: Get = Get(__client, _projectId, _userId)

          public val post: Post = Post(__client, _projectId, _userId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _userId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("organization/projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _projectId: String,
            private val _userId: String,
          ) {
            public suspend operator fun invoke(): ProjectUser {
              val response = __client.get("organization/projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _userId: String,
          ) {
            public suspend operator fun invoke(body: ProjectUserUpdateRequest): Response {
              val response = __client.post("organization/projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}") {
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
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, roleId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 1_000L,
        after: String? = null,
        order: Order? = Order.Asc,
      ): PublicRoleListResource {
        val response = __client.get("organization/roles") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

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
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: PublicCreateOrganizationRoleBody): Role {
        val response = __client.post("organization/roles") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class RoleIdPath internal constructor(
      private val __client: HttpClient,
      private val _roleId: String,
    ) {
      public val delete: Delete = Delete(__client, _roleId)

      public val post: Post = Post(__client, _roleId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _roleId: String,
      ) {
        public suspend operator fun invoke(): RoleDeletedResource {
          val response = __client.delete("organization/roles/${this._roleId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _roleId: String,
      ) {
        public suspend operator fun invoke(body: PublicUpdateOrganizationRoleBody): Role {
          val response = __client.post("organization/roles/${this._roleId.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class Usage internal constructor(
    private val __client: HttpClient,
  ) {
    public val audioSpeeches: AudioSpeeches = AudioSpeeches(__client)

    public val audioTranscriptions: AudioTranscriptions = AudioTranscriptions(__client)

    public val codeInterpreterSessions: CodeInterpreterSessions = CodeInterpreterSessions(__client)

    public val completions: Completions = Completions(__client)

    public val embeddings: Embeddings = Embeddings(__client)

    public val images: Images = Images(__client)

    public val moderations: Moderations = Moderations(__client)

    public val vectorStores: VectorStores = VectorStores(__client)

    public class AudioSpeeches internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/audio_speeches") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/audio_transcriptions") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse {
          val response = __client.get("organization/usage/code_interpreter_sessions") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/completions") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            if (batch != null) {
              this.url.encodedParameters.append("batch", batch.toString())
            }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/embeddings") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/images") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            sources?.forEach { this.url.encodedParameters.append("sources", it.value.encodeURLParameter()) }
            sizes?.forEach { this.url.encodedParameters.append("sizes", it.toString().encodeURLParameter()) }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
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
        ): UsageResponse {
          val response = __client.get("organization/usage/moderations") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            userIds?.forEach { this.url.encodedParameters.append("user_ids", it.encodeURLParameter()) }
            apiKeyIds?.forEach { this.url.encodedParameters.append("api_key_ids", it.encodeURLParameter()) }
            models?.forEach { this.url.encodedParameters.append("models", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          startTime: Long,
          endTime: Long? = null,
          bucketWidth: BucketWidth? = BucketWidth.`1d`,
          projectIds: List<String>? = null,
          groupBy: List<GroupBy>? = null,
          limit: Long? = null,
          page: String? = null,
        ): UsageResponse {
          val response = __client.get("organization/usage/vector_stores") {
            this.url.encodedParameters.append("start_time", startTime.toString())
            if (endTime != null) {
              this.url.encodedParameters.append("end_time", endTime.toString())
            }
            if (bucketWidth != null) {
              this.url.encodedParameters.append("bucket_width", bucketWidth.toString().encodeURLParameter())
            }
            projectIds?.forEach { this.url.encodedParameters.append("project_ids", it.encodeURLParameter()) }
            groupBy?.forEach { this.url.encodedParameters.append("group_by", it.value.encodeURLParameter()) }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

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
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public fun userId(userId: String): UserIdPath = UserIdPath(__client, userId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        limit: Long? = 20L,
        after: String? = null,
        emails: List<String>? = null,
      ): UserListResponse {
        val response = __client.get("organization/users") {
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          emails?.forEach { this.url.encodedParameters.append("emails", it.encodeURLParameter()) }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class UserIdPath internal constructor(
      private val __client: HttpClient,
      private val _userId: String,
    ) {
      public val delete: Delete = Delete(__client, _userId)

      public val `get`: Get = Get(__client, _userId)

      public val post: Post = Post(__client, _userId)

      public val roles: Roles = Roles(__client, _userId)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _userId: String,
      ) {
        public suspend operator fun invoke(): UserDeleteResponse {
          val response = __client.delete("organization/users/${this._userId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _userId: String,
      ) {
        public suspend operator fun invoke(): User {
          val response = __client.get("organization/users/${this._userId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _userId: String,
      ) {
        public suspend operator fun invoke(body: UserRoleUpdateRequest): User {
          val response = __client.post("organization/users/${this._userId.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Roles internal constructor(
        private val __client: HttpClient,
        private val _userId: String,
      ) {
        public val `get`: Get = Get(__client, _userId)

        public val post: Post = Post(__client, _userId)

        public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, _userId, roleId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _userId: String,
        ) {
          public suspend operator fun invoke(
            limit: Long? = null,
            after: String? = null,
            order: Order? = null,
          ): RoleListResource {
            val response = __client.get("organization/users/${this._userId.encodeURLParameter()}/roles") {
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _userId: String,
        ) {
          public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): UserRoleAssignment {
            val response = __client.post("organization/users/${this._userId.encodeURLParameter()}/roles") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class RoleIdPath internal constructor(
          private val __client: HttpClient,
          private val _userId: String,
          private val _roleId: String,
        ) {
          public val delete: Delete = Delete(__client, _userId, _roleId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _userId: String,
            private val _roleId: String,
          ) {
            public suspend operator fun invoke(): DeletedRoleAssignmentResource {
              val response = __client.delete("organization/users/${this._userId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }
    }
  }
}
