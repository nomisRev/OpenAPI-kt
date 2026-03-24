package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The output of a shell tool call that was emitted.
 */
@Serializable
public data class FunctionShellCallOutput(
  @Required
  public val type: Type = Type.ShellCallOutput,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val status: LocalShellCallOutputStatusEnum,
  public val output: List<FunctionShellCallOutputContent>,
  @SerialName("max_output_length")
  public val maxOutputLength: Long?,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("shell_call_output")
    ShellCallOutput("shell_call_output"),
    ;
  }
}
