package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A tool call to run code.
 *
 */
@Serializable
public data class CodeInterpreterToolCall(
  @Required
  public val type: Type = Type.CodeInterpreterCall,
  public val id: String,
  public val status: Status,
  @SerialName("container_id")
  public val containerId: String,
  public val code: String?,
  public val outputs: List<Outputs>?,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Outputs {
    @Serializable
    @JvmInline
    @SerialName("CodeInterpreterOutputLogs")
    public value class CodeInterpreterOutputLogs(
      public val `value`: io.openai.model.CodeInterpreterOutputLogs,
    ) : Outputs

    @Serializable
    @JvmInline
    @SerialName("CodeInterpreterOutputImage")
    public value class CodeInterpreterOutputImage(
      public val `value`: io.openai.model.CodeInterpreterOutputImage,
    ) : Outputs
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    @SerialName("interpreting")
    Interpreting("interpreting"),
    @SerialName("failed")
    Failed("failed"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("code_interpreter_call")
    CodeInterpreterCall("code_interpreter_call"),
    ;
  }
}
