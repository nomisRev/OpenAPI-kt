package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class ParticipationStats(
  public val all: List<Long>,
  public val owner: List<Long>,
)
