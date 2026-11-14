package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Issue Event Milestone
 */
@Serializable
public data class IssueEventMilestone(
  public val title: String,
)
