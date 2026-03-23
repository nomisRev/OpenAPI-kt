package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Gitignore Template
 */
@Serializable
public data class GitignoreTemplate(
  public val name: String,
  public val source: String,
)
