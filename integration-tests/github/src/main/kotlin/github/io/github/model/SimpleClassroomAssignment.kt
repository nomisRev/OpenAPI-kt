package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub Classroom assignment
 */
@Serializable
public data class SimpleClassroomAssignment(
  public val id: Long,
  @SerialName("public_repo")
  public val publicRepo: Boolean,
  public val title: String,
  public val type: Type,
  @SerialName("invite_link")
  public val inviteLink: String,
  @SerialName("invitations_enabled")
  public val invitationsEnabled: Boolean,
  public val slug: String,
  @SerialName("students_are_repo_admins")
  public val studentsAreRepoAdmins: Boolean,
  @SerialName("feedback_pull_requests_enabled")
  public val feedbackPullRequestsEnabled: Boolean,
  @SerialName("max_teams")
  public val maxTeams: Long? = null,
  @SerialName("max_members")
  public val maxMembers: Long? = null,
  public val editor: String,
  public val accepted: Long,
  public val submitted: Long,
  public val passing: Long,
  public val language: String,
  public val deadline: Instant?,
  public val classroom: SimpleClassroom,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("individual")
    Individual("individual"),
    @SerialName("group")
    Group("group"),
    ;
  }
}
