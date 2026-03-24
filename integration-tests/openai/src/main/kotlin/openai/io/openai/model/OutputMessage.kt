package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An output message from the model.
 *
 */
@Serializable
public data class OutputMessage(
  public val id: String,
  public val type: Type,
  public val role: Role,
  public val content: List<OutputMessageContent>,
  public val phase: MessagePhase? = null,
  public val status: Status,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("assistant")
    Assistant("assistant"),
    ;
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
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("message")
    Message("message"),
    ;
  }
}
