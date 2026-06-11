package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents users that have voted for the issue or its duplicates.
 */
@Serializable
public data class IssueVoters(
  public val id: String? = null,
  public val hasVote: Boolean? = null,
  public val original: List<UserRead>? = null,
  public val duplicate: List<DuplicateVote>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
