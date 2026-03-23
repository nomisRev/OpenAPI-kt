package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Thread
 */
@Serializable
public data class Thread(
  public val id: String,
  public val repository: MinimalRepository,
  public val subject: Subject,
  public val reason: String,
  public val unread: Boolean,
  @SerialName("updated_at")
  public val updatedAt: String,
  @SerialName("last_read_at")
  public val lastReadAt: String?,
  public val url: String,
  @SerialName("subscription_url")
  public val subscriptionUrl: String,
) {
  @Serializable
  public data class Subject(
    public val title: String,
    public val url: String,
    @SerialName("latest_comment_url")
    public val latestCommentUrl: String,
    public val type: String,
  )
}
