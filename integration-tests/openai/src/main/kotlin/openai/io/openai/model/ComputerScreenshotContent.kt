package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A screenshot of a computer.
 */
@Serializable
public data class ComputerScreenshotContent(
  @Required
  public val type: Type = Type.ComputerScreenshot,
  @SerialName("image_url")
  public val imageUrl: String?,
  @SerialName("file_id")
  public val fileId: String?,
  public val detail: ImageDetail,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("computer_screenshot")
    ComputerScreenshot("computer_screenshot"),
    ;
  }
}
