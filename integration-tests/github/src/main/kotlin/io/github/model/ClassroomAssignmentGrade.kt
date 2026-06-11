package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Grade for a student or groups GitHub Classroom assignment
 */
@Serializable
public data class ClassroomAssignmentGrade(
  @SerialName("assignment_name")
  public val assignmentName: String,
  @SerialName("assignment_url")
  public val assignmentUrl: String,
  @SerialName("starter_code_url")
  public val starterCodeUrl: String,
  @SerialName("github_username")
  public val githubUsername: String,
  @SerialName("roster_identifier")
  public val rosterIdentifier: String,
  @SerialName("student_repository_name")
  public val studentRepositoryName: String,
  @SerialName("student_repository_url")
  public val studentRepositoryUrl: String,
  @SerialName("submission_timestamp")
  public val submissionTimestamp: String,
  @SerialName("points_awarded")
  public val pointsAwarded: Long,
  @SerialName("points_available")
  public val pointsAvailable: Long,
  @SerialName("group_name")
  public val groupName: String? = null,
)
