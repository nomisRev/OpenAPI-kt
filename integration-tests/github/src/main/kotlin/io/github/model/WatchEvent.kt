package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class WatchEvent(
  public val action: String,
)
