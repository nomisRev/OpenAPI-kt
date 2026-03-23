package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub App that is providing a custom deployment protection rule.
 */
@Serializable
public data class CustomDeploymentRuleApp(
  public val id: Long,
  public val slug: String,
  @SerialName("integration_url")
  public val integrationUrl: String,
  @SerialName("node_id")
  public val nodeId: String,
)
