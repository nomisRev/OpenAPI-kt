package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsArtifactAndLogRetentionResponse(
  public val days: Long,
  @SerialName("maximum_allowed_days")
  public val maximumAllowedDays: Long,
)
