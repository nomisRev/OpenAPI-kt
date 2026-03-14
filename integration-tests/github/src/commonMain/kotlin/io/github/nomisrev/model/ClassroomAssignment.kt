package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ClassroomAssignment(
    val id: Long,
    @SerialName("public_repo") val publicRepo: Boolean,
    val title: String,
    val type: Type,
    @SerialName("invite_link") val inviteLink: String,
    @SerialName("invitations_enabled") val invitationsEnabled: Boolean,
    val slug: String,
    @SerialName("students_are_repo_admins") val studentsAreRepoAdmins: Boolean,
    @SerialName("feedback_pull_requests_enabled") val feedbackPullRequestsEnabled: Boolean,
    @SerialName("max_teams") val maxTeams: Long?,
    @SerialName("max_members") val maxMembers: Long?,
    val editor: String,
    val accepted: Long,
    val submitted: Long,
    val passing: Long,
    val language: String,
    val deadline: LocalDateTime?,
    @SerialName("starter_code_repository") val starterCodeRepository: SimpleClassroomRepository,
    val classroom: Classroom,
) {
    @Serializable
    enum class Type {
        @SerialName("individual") Individual, @SerialName("group") Group;
    }
}
