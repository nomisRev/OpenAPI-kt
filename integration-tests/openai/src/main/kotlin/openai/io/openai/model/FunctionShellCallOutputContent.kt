package io.openai.model

import kotlin.Long
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
    @SerialName("timeout")
    public data object Timeout : Outcome

    /**
     * Indicates that the shell commands finished and returned an exit code.
     */
    @JvmInline
    @SerialName("exit")
    @Serializable
    public value class Exit(
      @SerialName("exit_code")
      public val exitCode: Long,
    ) : Outcome
  }
}
