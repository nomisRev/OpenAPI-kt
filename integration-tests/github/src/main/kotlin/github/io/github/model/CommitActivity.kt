package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Commit Activity
 */
@Serializable
public data class CommitActivity(
  public val days: List<Long>,
  public val total: Long,
  public val week: Long,
)
