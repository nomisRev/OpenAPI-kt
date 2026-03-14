package io.github.nomisrev.api

import io.github.nomisrev.model.ClassroomAssignment
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ClassroomAcceptedAssignment
import io.github.nomisrev.model.ClassroomAssignmentGrade
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Assignments {
    val acceptedAssignments: Assignments.AcceptedAssignments

    val grades: Assignments.Grades

    sealed interface ClassroomGetAnAssignmentResult {
        data class OK(val value: ClassroomAssignment) : ClassroomGetAnAssignmentResult

        data class NotFound(val value: BasicError) : ClassroomGetAnAssignmentResult
    }

    suspend fun classroomGetAnAssignment(
        assignmentId: Long,
    ): ClassroomGetAnAssignmentResult

    interface AcceptedAssignments {
        suspend fun classroomListAcceptedAssignmentsForAnAssignment(
            assignmentId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<ClassroomAcceptedAssignment>
    }

    interface Grades {
        sealed interface ClassroomGetAssignmentGradesResult {
            data class OK(val value: List<ClassroomAssignmentGrade>) : ClassroomGetAssignmentGradesResult

            data class NotFound(val value: BasicError) : ClassroomGetAssignmentGradesResult
        }

        suspend fun classroomGetAssignmentGrades(
            assignmentId: Long,
        ): ClassroomGetAssignmentGradesResult
    }
}

internal class KtorAssignments(private val client: HttpClient) : Assignments {
    override val acceptedAssignments: Assignments.AcceptedAssignments = KtorAssignmentsAcceptedAssignments(client)

    override val grades: Assignments.Grades = KtorAssignmentsGrades(client)

    override suspend fun classroomGetAnAssignment(assignmentId: Long): Assignments.ClassroomGetAnAssignmentResult {
        val response = client.get("/assignments/$assignmentId")
        return when (response.status) {
            HttpStatusCode.OK -> Assignments.ClassroomGetAnAssignmentResult.OK(response.body())
            HttpStatusCode.NotFound -> Assignments.ClassroomGetAnAssignmentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorAssignmentsAcceptedAssignments(private val client: HttpClient) : Assignments.AcceptedAssignments {
    override suspend fun classroomListAcceptedAssignmentsForAnAssignment(assignmentId: Long, page: Long, perPage: Long): List<ClassroomAcceptedAssignment> =
        client.get("/assignments/$assignmentId/accepted_assignments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorAssignmentsGrades(private val client: HttpClient) : Assignments.Grades {
    override suspend fun classroomGetAssignmentGrades(assignmentId: Long): Assignments.Grades.ClassroomGetAssignmentGradesResult {
        val response = client.get("/assignments/$assignmentId/grades")
        return when (response.status) {
            HttpStatusCode.OK -> Assignments.Grades.ClassroomGetAssignmentGradesResult.OK(response.body())
            HttpStatusCode.NotFound -> Assignments.Grades.ClassroomGetAssignmentGradesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
