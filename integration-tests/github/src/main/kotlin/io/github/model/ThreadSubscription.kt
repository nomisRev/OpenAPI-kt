package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Thread Subscription
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ThreadSubscription(
  public val subscribed: Boolean,
  public val ignored: Boolean,
  public val reason: String?,
  @SerialName("created_at")
  public val createdAt: Instant?,
  public val url: String,
  @SerialName("thread_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val threadUrl: String? = null,
  @SerialName("repository_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryUrl: String? = null,
)
