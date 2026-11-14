package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information about repositories that Dependabot is able to access in an organization
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DependabotRepositoryAccessDetails(
  @SerialName("default_level")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultLevel: Optional<DefaultLevel> = Optional.Absent,
  @SerialName("accessible_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
