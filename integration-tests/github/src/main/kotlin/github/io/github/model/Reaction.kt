package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reactions to conversations provide a way to help people express their feelings more simply and effectively.
 */
@Serializable
public data class Reaction(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val user: NullableSimpleUser?,
  public val content: Content,
  @SerialName("created_at")
  public val createdAt: Instant,
) {
  @Serializable
  public enum class Content(
    public val `value`: String,
  ) {
    `+1`("+1"),
    `-1`("-1"),
    @SerialName("laugh")
    Laugh("laugh"),
    @SerialName("confused")
    Confused("confused"),
    @SerialName("heart")
    Heart("heart"),
    @SerialName("hooray")
    Hooray("hooray"),
    @SerialName("rocket")
    Rocket("rocket"),
    @SerialName("eyes")
    Eyes("eyes"),
    ;
  }
}
