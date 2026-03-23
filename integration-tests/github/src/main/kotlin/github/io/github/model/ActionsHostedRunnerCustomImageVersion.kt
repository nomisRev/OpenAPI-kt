package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Provides details of a hosted runner custom image version
 */
@Serializable
public data class ActionsHostedRunnerCustomImageVersion(
  public val version: String,
  public val state: String,
  @SerialName("size_gb")
  public val sizeGb: Long,
  @SerialName("created_on")
  public val createdOn: String,
  @SerialName("state_details")
  public val stateDetails: String,
)
