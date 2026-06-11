package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * The exit or timeout outcome associated with this shell call.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface FunctionShellCallOutputOutcomeParam {
  @Serializable
  @SerialName("timeout")
  public data object Timeout : FunctionShellCallOutputOutcomeParam

  /**
   * Indicates that the shell commands finished and returned an exit code.
   */
  @JvmInline
  @SerialName("exit")
  @Serializable
  public value class Exit(
    @SerialName("exit_code")
    public val exitCode: Long,
  ) : FunctionShellCallOutputOutcomeParam
}
