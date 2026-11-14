package io.github.model

import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsArtifactAndLogRetention(
  public val days: Long,
)
