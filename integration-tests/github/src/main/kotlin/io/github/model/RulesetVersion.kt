package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The historical version of a ruleset
 */
@Serializable
public data class RulesetVersion(
  @SerialName("version_id")
  public val versionId: Long,
  public val actor: Actor,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  /**
   * The actor who updated the ruleset
   */
  @Serializable
  public data class Actor(
    public val id: Long? = null,
    public val type: String? = null,
  )
}
