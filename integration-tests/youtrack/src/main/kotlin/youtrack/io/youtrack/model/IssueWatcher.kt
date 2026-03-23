package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user who subscribed for notifications about an issue.
 */
@Serializable
public data class IssueWatcher(
  public val id: String? = null,
  public val user: UserRead? = null,
  public val issue: IssueRead? = null,
  public val isStarred: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
