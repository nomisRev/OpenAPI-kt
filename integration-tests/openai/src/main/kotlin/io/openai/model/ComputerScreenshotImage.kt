package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A computer screenshot image used with the computer use tool.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ComputerScreenshotImage(
  @Required
  public val type: Type = Type.ComputerScreenshot,
  @SerialName("image_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val imageUrl: String? = null,
  @SerialName("file_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
