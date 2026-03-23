package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Thread Subscription
 */
@Serializable
public data class ThreadSubscription(
  public val subscribed: Boolean,
  public val ignored: Boolean,
  public val reason: String?,
  @SerialName("created_at")
  public val createdAt: Instant?,
  public val url: String,
  @SerialName("thread_url")
  public val threadUrl: String? = null,
  @SerialName("repository_url")
  public val repositoryUrl: String? = null,
)
