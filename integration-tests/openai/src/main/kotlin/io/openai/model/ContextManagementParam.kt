package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContextManagementParam(
  public val type: String,
  @SerialName("compact_threshold")
  public val compactThreshold: Long? = null,
)
