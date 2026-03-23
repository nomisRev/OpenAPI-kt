package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * Metaproperties for Git author/committer information.
 */
@Serializable
public data class NullableGitUser(
  public val name: String? = null,
  public val email: String? = null,
  public val date: Instant? = null,
)
