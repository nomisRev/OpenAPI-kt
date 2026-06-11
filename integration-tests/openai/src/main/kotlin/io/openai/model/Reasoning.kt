package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **gpt-5 and o-series models only**
 *
 * Configuration options for
 * [reasoning models](https://platform.openai.com/docs/guides/reasoning).
 *
 */
@Serializable
public data class Reasoning(
  public val effort: ReasoningEffort? = null,
  public val summary: Summary? = null,
  @SerialName("generate_summary")
  public val generateSummary: GenerateSummary? = null,
) {
  @Serializable
  public enum class GenerateSummary(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("concise")
    Concise("concise"),
    @SerialName("detailed")
    Detailed("detailed"),
    ;
  }

  @Serializable
  public enum class Summary(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("concise")
    Concise("concise"),
    @SerialName("detailed")
    Detailed("detailed"),
    ;
  }
}
