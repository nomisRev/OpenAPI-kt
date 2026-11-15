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
import io.openai.model.DeletedRoleAssignmentResource
import io.openai.model.GroupRoleAssignment
import io.openai.model.PublicAssignOrganizationGroupRoleBody
import io.openai.model.PublicCreateOrganizationRoleBody
import io.openai.model.PublicRoleListResource
import io.openai.model.PublicUpdateOrganizationRoleBody
import io.openai.model.Role
import io.openai.model.RoleDeletedResource
import io.openai.model.RoleListResource
import io.openai.model.UserRoleAssignment
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Projects internal constructor(
  private val __client: HttpClient,
) {
  public fun projectId(projectId: String): ProjectIdPath = ProjectIdPath(__client, projectId)

  public class ProjectIdPath internal constructor(
    private val __client: HttpClient,
    private val _projectId: String,
  ) {
    public val groups: Groups = Groups(__client, _projectId)

    public val roles: Roles = Roles(__client, _projectId)

    public val users: Users = Users(__client, _projectId)

    public class Groups internal constructor(
      private val __client: HttpClient,
      private val _projectId: String,
    ) {
      public fun groupId(groupId: String): GroupIdPath = GroupIdPath(__client, _projectId, groupId)

      public class GroupIdPath internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
        private val _groupId: String,
      ) {
        public val roles: Roles = Roles(__client, _projectId, _groupId)

        public class Roles internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _groupId: String,
        ) {
          public val `get`: Get = Get(__client, _projectId, _groupId)

          public val post: Post = Post(__client, _projectId, _groupId)

          public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, _projectId, _groupId, roleId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _groupId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = null,
              after: String? = null,
              order: Order? = null,
            ): RoleListResource {
              val response = __client.get("projects/${this._projectId.encodeURLParameter()}/groups/${this._groupId.encodeURLParameter()}/roles") {
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
            private val _groupId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): GroupRoleAssignment {
              val response = __client.post("projects/${this._projectId.encodeURLParameter()}/groups/${this._groupId.encodeURLParameter()}/roles") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class RoleIdPath internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _groupId: String,
            private val _roleId: String,
          ) {
            public val delete: Delete = Delete(__client, _projectId, _groupId, _roleId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _projectId: String,
              private val _groupId: String,
              private val _roleId: String,
            ) {
              public suspend operator fun invoke(): DeletedRoleAssignmentResource {
                val response = __client.delete("projects/${this._projectId.encodeURLParameter()}/groups/${this._groupId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }
    }

    public class Roles internal constructor(
      private val __client: HttpClient,
      private val _projectId: String,
    ) {
      public val `get`: Get = Get(__client, _projectId)

      public val post: Post = Post(__client, _projectId)

      public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, _projectId, roleId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 1_000L,
          after: String? = null,
          order: Order? = Order.Asc,
        ): PublicRoleListResource {
          val response = __client.get("projects/${this._projectId.encodeURLParameter()}/roles") {
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
        public suspend operator fun invoke(body: PublicCreateOrganizationRoleBody): Role {
          val response = __client.post("projects/${this._projectId.encodeURLParameter()}/roles") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class RoleIdPath internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
        private val _roleId: String,
      ) {
        public val delete: Delete = Delete(__client, _projectId, _roleId)

        public val post: Post = Post(__client, _projectId, _roleId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _roleId: String,
        ) {
          public suspend operator fun invoke(): RoleDeletedResource {
            val response = __client.delete("projects/${this._projectId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _roleId: String,
        ) {
          public suspend operator fun invoke(body: PublicUpdateOrganizationRoleBody): Role {
            val response = __client.post("projects/${this._projectId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
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
      public fun userId(userId: String): UserIdPath = UserIdPath(__client, _projectId, userId)

      public class UserIdPath internal constructor(
        private val __client: HttpClient,
        private val _projectId: String,
        private val _userId: String,
      ) {
        public val roles: Roles = Roles(__client, _projectId, _userId)

        public class Roles internal constructor(
          private val __client: HttpClient,
          private val _projectId: String,
          private val _userId: String,
        ) {
          public val `get`: Get = Get(__client, _projectId, _userId)

          public val post: Post = Post(__client, _projectId, _userId)

          public fun roleId(roleId: String): RoleIdPath = RoleIdPath(__client, _projectId, _userId, roleId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _userId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = null,
              after: String? = null,
              order: Order? = null,
            ): RoleListResource {
              val response = __client.get("projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}/roles") {
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
            private val _userId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): UserRoleAssignment {
              val response = __client.post("projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}/roles") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class RoleIdPath internal constructor(
            private val __client: HttpClient,
            private val _projectId: String,
            private val _userId: String,
            private val _roleId: String,
          ) {
            public val delete: Delete = Delete(__client, _projectId, _userId, _roleId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _projectId: String,
              private val _userId: String,
              private val _roleId: String,
            ) {
              public suspend operator fun invoke(): DeletedRoleAssignmentResource {
                val response = __client.delete("projects/${this._projectId.encodeURLParameter()}/users/${this._userId.encodeURLParameter()}/roles/${this._roleId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }
    }
  }
}
