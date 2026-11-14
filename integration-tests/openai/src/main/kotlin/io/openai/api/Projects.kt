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
  private val client: HttpClient,
) {
  public fun projectId(projectId: String): ProjectIdPath = ProjectIdPath(client, projectId)

  public class ProjectIdPath internal constructor(
    private val client: HttpClient,
    private val projectId: String,
  ) {
    public val groups: Groups = Groups(client, projectId)

    public val roles: Roles = Roles(client, projectId)

    public val users: Users = Users(client, projectId)

    public class Groups internal constructor(
      private val client: HttpClient,
      private val projectId: String,
    ) {
      public fun groupId(groupId: String): GroupIdPath = GroupIdPath(client, projectId, groupId)

      public class GroupIdPath internal constructor(
        private val client: HttpClient,
        private val projectId: String,
        private val groupId: String,
      ) {
        public val roles: Roles = Roles(client, projectId, groupId)

        public class Roles internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val groupId: String,
        ) {
          public val `get`: Get = Get(client, projectId, groupId)

          public val post: Post = Post(client, projectId, groupId)

          public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, projectId, groupId, roleId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val groupId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = null,
              after: String? = null,
              order: Order? = null,
            ): RoleListResource {
              val response = client.get("projects/${this.projectId.encodeURLParameter()}/groups/${this.groupId.encodeURLParameter()}/roles") {
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
            private val client: HttpClient,
            private val projectId: String,
            private val groupId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): GroupRoleAssignment {
              val response = client.post("projects/${this.projectId.encodeURLParameter()}/groups/${this.groupId.encodeURLParameter()}/roles") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class RoleIdPath internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val groupId: String,
            private val roleId: String,
          ) {
            public val delete: Delete = Delete(client, projectId, groupId, roleId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val projectId: String,
              private val groupId: String,
              private val roleId: String,
            ) {
              public suspend operator fun invoke(): DeletedRoleAssignmentResource {
                val response = client.delete("projects/${this.projectId.encodeURLParameter()}/groups/${this.groupId.encodeURLParameter()}/roles/${this.roleId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }
    }

    public class Roles internal constructor(
      private val client: HttpClient,
      private val projectId: String,
    ) {
      public val `get`: Get = Get(client, projectId)

      public val post: Post = Post(client, projectId)

      public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, projectId, roleId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 1_000L,
          after: String? = null,
          order: Order? = Order.Asc,
        ): PublicRoleListResource {
          val response = client.get("projects/${this.projectId.encodeURLParameter()}/roles") {
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
        private val client: HttpClient,
        private val projectId: String,
      ) {
        public suspend operator fun invoke(body: PublicCreateOrganizationRoleBody): Role {
          val response = client.post("projects/${this.projectId.encodeURLParameter()}/roles") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class RoleIdPath internal constructor(
        private val client: HttpClient,
        private val projectId: String,
        private val roleId: String,
      ) {
        public val delete: Delete = Delete(client, projectId, roleId)

        public val post: Post = Post(client, projectId, roleId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val roleId: String,
        ) {
          public suspend operator fun invoke(): RoleDeletedResource {
            val response = client.delete("projects/${this.projectId.encodeURLParameter()}/roles/${this.roleId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val roleId: String,
        ) {
          public suspend operator fun invoke(body: PublicUpdateOrganizationRoleBody): Role {
            val response = client.post("projects/${this.projectId.encodeURLParameter()}/roles/${this.roleId.encodeURLParameter()}") {
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
      private val client: HttpClient,
      private val projectId: String,
    ) {
      public fun userId(userId: String): UserIdPath = UserIdPath(client, projectId, userId)

      public class UserIdPath internal constructor(
        private val client: HttpClient,
        private val projectId: String,
        private val userId: String,
      ) {
        public val roles: Roles = Roles(client, projectId, userId)

        public class Roles internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val userId: String,
        ) {
          public val `get`: Get = Get(client, projectId, userId)

          public val post: Post = Post(client, projectId, userId)

          public fun roleId(roleId: String): RoleIdPath = RoleIdPath(client, projectId, userId, roleId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = null,
              after: String? = null,
              order: Order? = null,
            ): RoleListResource {
              val response = client.get("projects/${this.projectId.encodeURLParameter()}/users/${this.userId.encodeURLParameter()}/roles") {
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
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): UserRoleAssignment {
              val response = client.post("projects/${this.projectId.encodeURLParameter()}/users/${this.userId.encodeURLParameter()}/roles") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class RoleIdPath internal constructor(
            private val client: HttpClient,
            private val projectId: String,
            private val userId: String,
            private val roleId: String,
          ) {
            public val delete: Delete = Delete(client, projectId, userId, roleId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val projectId: String,
              private val userId: String,
              private val roleId: String,
            ) {
              public suspend operator fun invoke(): DeletedRoleAssignmentResource {
                val response = client.delete("projects/${this.projectId.encodeURLParameter()}/users/${this.userId.encodeURLParameter()}/roles/${this.roleId.encodeURLParameter()}")
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
