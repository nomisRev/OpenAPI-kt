package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ImageDetail(
  public val `value`: String,
) {
  @SerialName("low")
  Low("low"),
  @SerialName("high")
  High("high"),
  @SerialName("auto")
  Auto("auto"),
  @SerialName("original")
  Original("original"),
  ;
}
