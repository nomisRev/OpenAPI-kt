package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Page Build
 */
@Serializable
public data class PageBuild(
  public val url: String,
  public val status: String,
  public val error: Error,
  public val pusher: NullableSimpleUser?,
  public val commit: String,
  public val duration: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  @JvmInline
  @Serializable
  public value class Error(
    public val message: String?,
  )
}
