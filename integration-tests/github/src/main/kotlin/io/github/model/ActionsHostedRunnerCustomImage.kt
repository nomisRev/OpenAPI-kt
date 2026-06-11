package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Provides details of a custom runner image
 */
@Serializable
public data class ActionsHostedRunnerCustomImage(
  public val id: Long,
  public val platform: String,
  @SerialName("total_versions_size")
  public val totalVersionsSize: Long,
  public val name: String,
  public val source: String,
  @SerialName("versions_count")
  public val versionsCount: Long,
  @SerialName("latest_version")
  public val latestVersion: String,
  public val state: String,
)
