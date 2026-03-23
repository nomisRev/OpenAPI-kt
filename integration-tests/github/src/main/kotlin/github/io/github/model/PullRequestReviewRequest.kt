package io.github.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Pull Request Review Request
 */
@Serializable
public data class PullRequestReviewRequest(
  public val users: List<SimpleUser>,
  public val teams: List<Team>,
)
