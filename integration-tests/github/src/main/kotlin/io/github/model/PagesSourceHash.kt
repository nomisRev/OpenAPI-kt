package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class PagesSourceHash(
  public val branch: String,
  public val path: String,
)
