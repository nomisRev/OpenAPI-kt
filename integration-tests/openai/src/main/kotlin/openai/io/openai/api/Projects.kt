package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
            ): RoleListResource = client.get("/projects/$projectId/groups/$groupId/roles") {
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
            private val groupId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): GroupRoleAssignment = client.post("/projects/$projectId/groups/$groupId/roles") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }.body()
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
              public suspend operator fun invoke(): DeletedRoleAssignmentResource = client.delete("/projects/$projectId/groups/$groupId/roles/$roleId").body()
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
        ): PublicRoleListResource = client.get("/projects/$projectId/roles") {
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
        public suspend operator fun invoke(body: PublicCreateOrganizationRoleBody): Role = client.post("/projects/$projectId/roles") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
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
          public suspend operator fun invoke(): RoleDeletedResource = client.delete("/projects/$projectId/roles/$roleId").body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val projectId: String,
          private val roleId: String,
        ) {
          public suspend operator fun invoke(body: PublicUpdateOrganizationRoleBody): Role = client.post("/projects/$projectId/roles/$roleId") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
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
            ): RoleListResource = client.get("/projects/$projectId/users/$userId/roles") {
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
            private val userId: String,
          ) {
            public suspend operator fun invoke(body: PublicAssignOrganizationGroupRoleBody): UserRoleAssignment = client.post("/projects/$projectId/users/$userId/roles") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }.body()
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
              public suspend operator fun invoke(): DeletedRoleAssignmentResource = client.delete("/projects/$projectId/users/$userId/roles/$roleId").body()
            }
          }
        }
      }
    }
  }
}
