package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class FileSearchRanker(
  public val `value`: String,
) {
  @SerialName("auto")
  Auto("auto"),
  @SerialName("default_2024_08_21")
  Default20240821("default_2024_08_21"),
  ;
}
