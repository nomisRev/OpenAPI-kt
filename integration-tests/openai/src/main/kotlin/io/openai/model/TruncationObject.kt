package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls for how a thread will be truncated prior to the run. Use this to control the initial context window of the run.
 */
@Serializable
public data class TruncationObject(
  public val type: Type,
  @SerialName("last_messages")
  public val lastMessages: Long? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("last_messages")
    LastMessages("last_messages"),
    ;
  }
}
