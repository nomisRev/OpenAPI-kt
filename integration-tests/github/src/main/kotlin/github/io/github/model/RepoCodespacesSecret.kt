package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Set repository secrets for GitHub Codespaces.
 */
@Serializable
public data class RepoCodespacesSecret(
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
)
