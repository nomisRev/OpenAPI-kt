package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A computer screenshot image used with the computer use tool.
 *
 */
@Serializable
public data class ComputerScreenshotImage(
  @Required
  public val type: Type = Type.ComputerScreenshot,
  @SerialName("image_url")
  public val imageUrl: String? = null,
  @SerialName("file_id")
  public val fileId: String? = null,
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
