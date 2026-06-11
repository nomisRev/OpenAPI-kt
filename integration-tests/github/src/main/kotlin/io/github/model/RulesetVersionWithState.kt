package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The historical version of a ruleset
 */
@Serializable
public data class RulesetVersionWithState(
  @SerialName("version_id")
  public val versionId: Long,
  public val actor: Actor,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val state: JsonElement? = null,
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
