package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A workflow that must run for this rule to pass
 */
@Serializable
public data class RepositoryRuleParamsWorkflowFileReferenceWrite(
  public val path: String,
  public val ref: String? = null,
  @SerialName("repository_id")
  public val repositoryId: Long,
  public val sha: String? = null,
)
