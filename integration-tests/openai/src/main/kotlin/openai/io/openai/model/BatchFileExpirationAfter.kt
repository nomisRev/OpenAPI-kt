package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The expiration policy for the output and/or error file that are generated for a batch.
 */
@Serializable
public data class BatchFileExpirationAfter(
  public val anchor: Anchor,
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
