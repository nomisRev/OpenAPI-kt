package io.github.api

import io.github.model.BasicError
import io.github.model.ClassroomAcceptedAssignment
import io.github.model.ClassroomAssignment
import io.github.model.ClassroomAssignmentGrade
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Long
import kotlin.collections.List

public class Assignments internal constructor(
  private val __client: HttpClient,
) {
  public fun assignmentId(assignmentId: Long): AssignmentIdPath = AssignmentIdPath(__client, assignmentId)

  public class AssignmentIdPath internal constructor(
    private val __client: HttpClient,
    private val _assignmentId: Long,
  ) {
    public val `get`: Get = Get(__client, _assignmentId)

    public val acceptedAssignments: AcceptedAssignments =
        AcceptedAssignments(__client, _assignmentId)

    public val grades: Grades = Grades(__client, _assignmentId)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _assignmentId: Long,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("assignments/${this._assignmentId}")
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
      private val __client: HttpClient,
      private val _assignmentId: Long,
    ) {
      public val `get`: Get = Get(__client, _assignmentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _assignmentId: Long,
      ) {
        public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<ClassroomAcceptedAssignment> {
          val response = __client.get("assignments/${this._assignmentId}/accepted_assignments") {
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class Grades internal constructor(
      private val __client: HttpClient,
      private val _assignmentId: Long,
    ) {
      public val `get`: Get = Get(__client, _assignmentId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _assignmentId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.get("assignments/${this._assignmentId}/grades")
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
