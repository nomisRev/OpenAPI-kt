package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CampaignStateWrite(
  public val `value`: String,
) {
  @SerialName("open")
  Open("open"),
  @SerialName("closed")
  Closed("closed"),
  ;
}
