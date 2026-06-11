package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class NoiseReductionType(
  public val `value`: String,
) {
  @SerialName("near_field")
  NearField("near_field"),
  @SerialName("far_field")
  FarField("far_field"),
  ;
}
