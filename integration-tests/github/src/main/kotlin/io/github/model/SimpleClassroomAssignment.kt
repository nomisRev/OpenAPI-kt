package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub Classroom assignment
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxTeams: Optional<Long> = Optional.Absent,
  @SerialName("max_members")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxMembers: Optional<Long> = Optional.Absent,
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
