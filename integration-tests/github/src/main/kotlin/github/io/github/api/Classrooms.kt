package io.github.api

import io.github.model.BasicError
import io.github.model.Classroom
import io.github.model.SimpleClassroom
import io.github.model.SimpleClassroomAssignment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Long
import kotlin.collections.List

public class Classrooms internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun classroomId(classroomId: Long): ClassroomIdPath = ClassroomIdPath(client, classroomId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<SimpleClassroom> = client.get("/classrooms") {
      page?.let { parameter("page", it) }
      perPage?.let { parameter("per_page", it) }
    }.body()
  }

  public class ClassroomIdPath internal constructor(
    private val client: HttpClient,
    private val classroomId: Long,
  ) {
    public val `get`: Get = Get(client, classroomId)

    public val assignments: Assignments = Assignments(client, classroomId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val classroomId: Long,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/classrooms/$classroomId")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: Classroom,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Assignments internal constructor(
      private val client: HttpClient,
      private val classroomId: Long,
    ) {
      public val `get`: Get = Get(client, classroomId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val classroomId: Long,
      ) {
        public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<SimpleClassroomAssignment> = client.get("/classrooms/$classroomId/assignments") {
          page?.let { parameter("page", it) }
          perPage?.let { parameter("per_page", it) }
        }.body()
      }
    }
  }
}
