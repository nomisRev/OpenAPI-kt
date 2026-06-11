package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerFileResource(
  public val id: String,
  public val `object`: String,
  @SerialName("container_id")
  public val containerId: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val bytes: Long,
  public val path: String,
  public val source: String,
)
