package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsHostedRunnerLimits(
  @SerialName("public_ips")
  public val publicIps: PublicIps,
) {
  /**
   * Provides details of static public IP limits for GitHub-hosted Hosted Runners
   */
  @Serializable
  public data class PublicIps(
    public val maximum: Long,
    @SerialName("current_usage")
    public val currentUsage: Long,
  )
}
