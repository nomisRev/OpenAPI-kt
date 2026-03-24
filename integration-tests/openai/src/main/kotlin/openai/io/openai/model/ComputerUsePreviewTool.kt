package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool that controls a virtual computer. Learn more about the [computer tool](https://platform.openai.com/docs/guides/tools-computer-use).
 */
@Serializable
public data class ComputerUsePreviewTool(
  @Required
  public val type: Type = Type.ComputerUsePreview,
  public val environment: ComputerEnvironment,
  @SerialName("display_width")
  public val displayWidth: Long,
  @SerialName("display_height")
  public val displayHeight: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("computer_use_preview")
    ComputerUsePreview("computer_use_preview"),
    ;
  }
}
