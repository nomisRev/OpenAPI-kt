package io.github.api

import io.github.model.BasicError
import io.github.model.ClassroomAcceptedAssignment
import io.github.model.ClassroomAssignment
import io.github.model.ClassroomAssignmentGrade
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Long
import kotlin.collections.List

public class Assignments internal constructor(
  private val client: HttpClient,
) {
  public fun assignmentId(assignmentId: Long): AssignmentIdPath = AssignmentIdPath(client, assignmentId)

  public class AssignmentIdPath internal constructor(
    private val client: HttpClient,
    private val assignmentId: Long,
  ) {
    public val `get`: Get = Get(client, assignmentId)

    public val acceptedAssignments: AcceptedAssignments = AcceptedAssignments(client, assignmentId)

    public val grades: Grades = Grades(client, assignmentId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val assignmentId: Long,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/assignments/$assignmentId")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: ClassroomAssignment,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class AcceptedAssignments internal constructor(
      private val client: HttpClient,
      private val assignmentId: Long,
    ) {
      public val `get`: Get = Get(client, assignmentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val assignmentId: Long,
      ) {
        public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<ClassroomAcceptedAssignment> = client.get("/assignments/$assignmentId/accepted_assignments") {
          page?.let { parameter("page", it) }
          perPage?.let { parameter("per_page", it) }
        }.body()
      }
    }

    public class Grades internal constructor(
      private val client: HttpClient,
      private val assignmentId: Long,
    ) {
      public val `get`: Get = Get(client, assignmentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val assignmentId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/assignments/$assignmentId/grades")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<ClassroomAssignmentGrade>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }
}
