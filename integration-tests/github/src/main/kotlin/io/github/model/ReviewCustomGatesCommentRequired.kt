package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReviewCustomGatesCommentRequired(
  @SerialName("environment_name")
  public val environmentName: String,
  public val comment: String,
)
