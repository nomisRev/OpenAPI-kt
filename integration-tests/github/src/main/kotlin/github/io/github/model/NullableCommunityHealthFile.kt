package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NullableCommunityHealthFile(
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
)
