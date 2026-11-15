package io.github.api

import io.github.model.BasicError
import io.github.model.Classroom
import io.github.model.SimpleClassroom
import io.github.model.SimpleClassroomAssignment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Long
import kotlin.collections.List

public class Classrooms internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun classroomId(classroomId: Long): ClassroomIdPath = ClassroomIdPath(__client, classroomId)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<SimpleClassroom> {
      val response = __client.get("classrooms") {
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

  public class ClassroomIdPath internal constructor(
    private val __client: HttpClient,
    private val _classroomId: Long,
  ) {
    public val `get`: Get = Get(__client, _classroomId)

    public val assignments: Assignments = Assignments(__client, _classroomId)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _classroomId: Long,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.get("classrooms/${this._classroomId}")
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
      private val __client: HttpClient,
      private val _classroomId: Long,
    ) {
      public val `get`: Get = Get(__client, _classroomId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _classroomId: Long,
      ) {
        public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): List<SimpleClassroomAssignment> {
          val response = __client.get("classrooms/${this._classroomId}/assignments") {
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
  }
}
