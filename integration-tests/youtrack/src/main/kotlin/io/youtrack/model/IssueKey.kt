package io.youtrack.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores information about a project where the issue belongs or previously belonged. This entity appears as part of the ProjectActivityItem object.
 */
@Serializable
public data class IssueKey(
  public val id: String? = null,
  public val project: IssueFolderRead.Project? = null,
  public val numberInProject: Long? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
