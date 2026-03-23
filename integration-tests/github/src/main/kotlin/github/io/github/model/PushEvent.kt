package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PushEvent(
  @SerialName("repository_id")
  public val repositoryId: Long,
  @SerialName("push_id")
  public val pushId: Long,
  public val ref: String,
  public val head: String,
  public val before: String,
)
