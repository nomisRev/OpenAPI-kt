package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **gpt-5 and o-series models only**
 *
 * Configuration options for
 * [reasoning models](https://platform.openai.com/docs/guides/reasoning).
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Reasoning(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val effort: Optional<ReasoningEffort> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val summary: Optional<Summary> = Optional.Absent,
  @SerialName("generate_summary")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val generateSummary: Optional<GenerateSummary> = Optional.Absent,
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
