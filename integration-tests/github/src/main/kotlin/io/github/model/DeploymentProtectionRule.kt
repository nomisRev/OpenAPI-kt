package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Deployment protection rule
 */
@Serializable
public data class DeploymentProtectionRule(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val enabled: Boolean,
  public val app: CustomDeploymentRuleApp,
)
