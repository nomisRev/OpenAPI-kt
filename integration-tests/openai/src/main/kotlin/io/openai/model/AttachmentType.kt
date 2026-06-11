package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AttachmentType(
  public val `value`: String,
) {
  @SerialName("image")
  Image("image"),
  @SerialName("file")
  File("file"),
  ;
}
