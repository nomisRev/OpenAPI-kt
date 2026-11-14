package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls for how a thread will be truncated prior to the run. Use this to control the initial context window of the run.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TruncationObject(
  public val type: Type,
  @SerialName("last_messages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastMessages: Optional<Long> = Optional.Absent,
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
