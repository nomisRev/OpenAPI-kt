package io.github.nomisrev.api

import io.github.nomisrev.model.SimpleClassroom
import io.github.nomisrev.model.Classroom
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.SimpleClassroomAssignment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Classrooms {
    val assignments: Classrooms.Assignments

    suspend fun classroomListClassrooms(
        page: Long = 1L,
        perPage: Long = 30L,
    ): List<SimpleClassroom>

    sealed interface ClassroomGetAClassroomResult {
        data class OK(val value: Classroom) : ClassroomGetAClassroomResult

        data class NotFound(val value: BasicError) : ClassroomGetAClassroomResult
    }

    suspend fun classroomGetAClassroom(
        classroomId: Long,
    ): ClassroomGetAClassroomResult

    interface Assignments {
        suspend fun classroomListAssignmentsForAClassroom(
            classroomId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleClassroomAssignment>
    }
}

internal class KtorClassrooms(private val client: HttpClient) : Classrooms {
    override val assignments: Classrooms.Assignments = KtorClassroomsAssignments(client)

    override suspend fun classroomListClassrooms(page: Long, perPage: Long): List<SimpleClassroom> =
        client.get("/classrooms") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun classroomGetAClassroom(classroomId: Long): Classrooms.ClassroomGetAClassroomResult {
        val response = client.get("/classrooms/$classroomId")
        return when (response.status) {
            HttpStatusCode.OK -> Classrooms.ClassroomGetAClassroomResult.OK(response.body())
            HttpStatusCode.NotFound -> Classrooms.ClassroomGetAClassroomResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorClassroomsAssignments(private val client: HttpClient) : Classrooms.Assignments {
    override suspend fun classroomListAssignmentsForAClassroom(classroomId: Long, page: Long, perPage: Long): List<SimpleClassroomAssignment> =
        client.get("/classrooms/$classroomId/assignments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}
