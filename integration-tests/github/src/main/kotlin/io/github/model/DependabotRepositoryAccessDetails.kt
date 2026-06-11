package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information about repositories that Dependabot is able to access in an organization
 */
@Serializable
public data class DependabotRepositoryAccessDetails(
  @SerialName("default_level")
  public val defaultLevel: DefaultLevel? = null,
  @SerialName("accessible_repositories")
  public val accessibleRepositories: List<NullableSimpleRepository?>? = null,
) {
  @Serializable
  public enum class DefaultLevel(
    public val `value`: String,
  ) {
    @SerialName("public")
    Public("public"),
    @SerialName("internal")
    Internal("internal"),
    ;
  }
}
