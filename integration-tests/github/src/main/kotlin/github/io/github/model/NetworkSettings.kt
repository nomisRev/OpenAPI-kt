package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A hosted compute network settings resource.
 */
@Serializable
public data class NetworkSettings(
  public val id: String,
  @SerialName("network_configuration_id")
  public val networkConfigurationId: String? = null,
  public val name: String,
  @SerialName("subnet_id")
  public val subnetId: String,
  public val region: String,
)
