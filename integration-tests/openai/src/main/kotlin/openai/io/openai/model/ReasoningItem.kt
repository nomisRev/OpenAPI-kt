package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A description of the chain of thought used by a reasoning model while generating
 * a response. Be sure to include these items in your `input` to the Responses API
 * for subsequent turns of a conversation if you are manually
 * [managing context](/docs/guides/conversation-state).
 *
 */
@Serializable
public data class ReasoningItem(
  public val type: Type,
  public val id: String,
  @SerialName("encrypted_content")
  public val encryptedContent: String? = null,
  public val summary: List<SummaryTextContent>,
  public val content: List<ReasoningTextContent>? = null,
  public val status: Status? = null,
) {
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
    @SerialName("reasoning")
    Reasoning("reasoning"),
    ;
  }
}
