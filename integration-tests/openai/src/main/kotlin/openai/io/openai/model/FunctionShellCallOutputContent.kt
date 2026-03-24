package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * The content of a shell tool call output that was emitted.
 */
@Serializable
public data class FunctionShellCallOutputContent(
  public val stdout: String,
  public val stderr: String,
  public val outcome: Outcome,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  /**
   * Represents either an exit outcome (with an exit code) or a timeout outcome for a shell call output chunk.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Outcome {
    @Serializable
    @JvmInline
    @SerialName("FunctionShellCallOutputTimeoutOutcome")
    public value class FunctionShellCallOutputTimeoutOutcome(
      public val `value`: io.openai.model.FunctionShellCallOutputTimeoutOutcome,
    ) : Outcome

    @Serializable
    @JvmInline
    @SerialName("FunctionShellCallOutputExitOutcome")
    public value class FunctionShellCallOutputExitOutcome(
      public val `value`: io.openai.model.FunctionShellCallOutputExitOutcome,
    ) : Outcome
  }
}
