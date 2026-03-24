package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls when the session expires relative to an anchor timestamp.
 */
@Serializable
public data class ExpiresAfterParam(
  @Required
  public val anchor: Anchor = Anchor.CreatedAt,
  public val seconds: Long,
) {
  @Serializable
  public enum class Anchor(
    public val `value`: String,
  ) {
    @SerialName("created_at")
    CreatedAt("created_at"),
    ;
  }
}
