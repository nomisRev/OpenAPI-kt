package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ImageGenActionEnum(
  public val `value`: String,
) {
  @SerialName("generate")
  Generate("generate"),
  @SerialName("edit")
  Edit("edit"),
  @SerialName("auto")
  Auto("auto"),
  ;
}
