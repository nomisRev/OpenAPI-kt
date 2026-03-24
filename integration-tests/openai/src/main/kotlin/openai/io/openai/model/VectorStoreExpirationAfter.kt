package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The expiration policy for a vector store.
 */
@Serializable
public data class VectorStoreExpirationAfter(
  public val anchor: Anchor,
  public val days: Long,
) {
  @Serializable
  public enum class Anchor(
    public val `value`: String,
  ) {
    @SerialName("last_active_at")
    LastActiveAt("last_active_at"),
    ;
  }
}
