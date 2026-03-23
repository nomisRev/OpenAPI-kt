package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a vote for duplicates of the issue.
 */
@Serializable
public data class DuplicateVote(
  public val id: String? = null,
  public val issue: IssueRead? = null,
  public val user: UserRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
