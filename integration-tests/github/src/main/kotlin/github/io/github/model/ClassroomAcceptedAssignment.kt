package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub Classroom accepted assignment
 */
@Serializable
public data class ClassroomAcceptedAssignment(
  public val id: Long,
  public val submitted: Boolean,
  public val passing: Boolean,
  @SerialName("commit_count")
  public val commitCount: Long,
  public val grade: String,
  public val students: List<SimpleClassroomUser>,
  public val repository: SimpleClassroomRepository,
  public val assignment: SimpleClassroomAssignment,
)
