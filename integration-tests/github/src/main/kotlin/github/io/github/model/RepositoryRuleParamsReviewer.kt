package io.github.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * A required reviewing team
 */
@Serializable
public data class RepositoryRuleParamsReviewer(
  public val id: Long,
  public val type: Type,
) {
  @Serializable
  public enum class Type {
    Team,
  }
}
