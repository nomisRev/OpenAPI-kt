package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Pull Request Merge Result
 */
@Serializable
public data class PullRequestMergeResult(
  public val sha: String,
  public val merged: Boolean,
  public val message: String,
)
