package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that the model should use a built-in tool to generate a response.
 * [Learn more about built-in tools](/docs/guides/tools).
 *
 */
@JvmInline
@Serializable
public value class ToolChoiceTypes(
  public val type: Type,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_search")
    FileSearch("file_search"),
    @SerialName("web_search_preview")
    WebSearchPreview("web_search_preview"),
    @SerialName("computer")
    Computer("computer"),
    @SerialName("computer_use_preview")
    ComputerUsePreview("computer_use_preview"),
    @SerialName("computer_use")
    ComputerUse("computer_use"),
    @SerialName("web_search_preview_2025_03_11")
    WebSearchPreview20250311("web_search_preview_2025_03_11"),
    @SerialName("image_generation")
    ImageGeneration("image_generation"),
    @SerialName("code_interpreter")
    CodeInterpreter("code_interpreter"),
    ;
  }
}
