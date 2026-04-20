package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A reviewing team, and file patterns describing which files they must approve changes to.
 */
@Serializable
public data class RepositoryRuleParamsRequiredReviewerConfigurationRead(
  @SerialName("file_patterns")
  public val filePatterns: List<String>,
  @SerialName("minimum_approvals")
  public val minimumApprovals: Long,
  public val reviewer: RepositoryRuleParamsReviewerRead,
)
