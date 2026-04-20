package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for a targeting a repository property
 */
@Serializable
public data class RepositoryRulesetConditionsRepositoryPropertySpecRead(
  public val name: String,
  @SerialName("property_values")
  public val propertyValues: List<String>,
  public val source: Source? = null,
) {
  @Serializable
  public enum class Source(
    public val `value`: String,
  ) {
    @SerialName("custom")
    Custom("custom"),
    @SerialName("system")
    System("system"),
    ;
  }
}
