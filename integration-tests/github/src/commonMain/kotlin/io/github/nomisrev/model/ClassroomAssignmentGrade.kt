package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ClassroomAssignmentGrade(
    @SerialName("assignment_name") val assignmentName: String,
    @SerialName("assignment_url") val assignmentUrl: String,
    @SerialName("starter_code_url") val starterCodeUrl: String,
    @SerialName("github_username") val githubUsername: String,
    @SerialName("roster_identifier") val rosterIdentifier: String,
    @SerialName("student_repository_name") val studentRepositoryName: String,
    @SerialName("student_repository_url") val studentRepositoryUrl: String,
    @SerialName("submission_timestamp") val submissionTimestamp: String,
    @SerialName("points_awarded") val pointsAwarded: Long,
    @SerialName("points_available") val pointsAvailable: Long,
    @SerialName("group_name") val groupName: String? = null,
)
