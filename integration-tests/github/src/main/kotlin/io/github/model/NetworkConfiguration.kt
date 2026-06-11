package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A hosted compute network configuration.
 */
@Serializable
public data class NetworkConfiguration(
  public val id: String,
  public val name: String,
  @SerialName("compute_service")
  public val computeService: ComputeService? = null,
  @SerialName("network_settings_ids")
  public val networkSettingsIds: List<String>? = null,
  @SerialName("failover_network_settings_ids")
  public val failoverNetworkSettingsIds: List<String>? = null,
  @SerialName("failover_network_enabled")
  public val failoverNetworkEnabled: Boolean? = null,
  @SerialName("created_on")
  public val createdOn: Instant?,
) {
  @Serializable
  public enum class ComputeService(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("actions")
    Actions("actions"),
    @SerialName("codespaces")
    Codespaces("codespaces"),
    ;
  }
}
