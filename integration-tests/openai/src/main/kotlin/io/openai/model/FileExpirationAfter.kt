package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The expiration policy for a file. By default, files with `purpose=batch` expire after 30 days and all other files are persisted until they are manually deleted.
 */
@Serializable
public data class FileExpirationAfter(
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
