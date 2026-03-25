package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a ChatKit thread and its current status.
 */
@Serializable
public data class ThreadResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThread,
  @SerialName("created_at")
  public val createdAt: Long,
  public val title: String?,
  public val status: Status,
  public val user: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("chatkit.thread")
    ChatkitThread("chatkit.thread"),
    ;
  }

  /**
   * Current status for the thread. Defaults to `active` for newly created threads.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Status {
    @Serializable
    @SerialName("active")
    public data object Active : Status

    /**
     * Indicates that a thread is locked and cannot accept new input.
     */
    @JvmInline
    @SerialName("locked")
    @Serializable
    public value class Locked(
      public val reason: String?,
    ) : Status

    /**
     * Indicates that a thread has been closed.
     */
    @JvmInline
    @SerialName("closed")
    @Serializable
    public value class Closed(
      public val reason: String?,
    ) : Status
  }
}
