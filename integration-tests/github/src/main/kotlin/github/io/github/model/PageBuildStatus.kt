package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Page Build Status
 */
@Serializable
public data class PageBuildStatus(
  public val url: String,
  public val status: String,
)
