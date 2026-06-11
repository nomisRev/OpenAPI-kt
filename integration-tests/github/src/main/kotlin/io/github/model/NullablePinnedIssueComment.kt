package io.github.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Context around who pinned an issue comment and when it was pinned.
 */
@Serializable
public data class NullablePinnedIssueComment(
  @SerialName("pinned_at")
  public val pinnedAt: Instant,
  @SerialName("pinned_by")
  public val pinnedBy: NullableSimpleUser?,
)
