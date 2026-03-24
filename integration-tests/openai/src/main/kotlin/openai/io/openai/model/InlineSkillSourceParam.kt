package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Inline skill payload
 */
@Serializable
public data class InlineSkillSourceParam(
  @Required
  public val type: Type = Type.Base64,
  @SerialName("media_type")
  @Required
  public val mediaType: MediaType = MediaType.ApplicationZip,
  public val `data`: String,
) {
  @Serializable
  public enum class MediaType(
    public val `value`: String,
  ) {
    @SerialName("application/zip")
    ApplicationZip("application/zip"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("base64")
    Base64("base64"),
    ;
  }
}
