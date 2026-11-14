package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A workflow that must run for this rule to pass
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRuleParamsWorkflowFileReference(
  public val path: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ref: String? = null,
  @SerialName("repository_id")
  public val repositoryId: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sha: String? = null,
)
