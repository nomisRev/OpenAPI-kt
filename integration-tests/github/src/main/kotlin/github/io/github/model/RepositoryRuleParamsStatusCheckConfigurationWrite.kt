package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Required status check
 */
@Serializable
public data class RepositoryRuleParamsStatusCheckConfigurationWrite(
  public val context: String,
  @SerialName("integration_id")
  public val integrationId: Long? = null,
)
