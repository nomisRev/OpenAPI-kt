package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An OIDC custom property inclusion for repository properties
 */
@Serializable
public data class OidcCustomPropertyInclusion(
  @SerialName("custom_property_name")
  public val customPropertyName: String,
  @SerialName("inclusion_source")
  public val inclusionSource: InclusionSource,
) {
  @Serializable
  public enum class InclusionSource(
    public val `value`: String,
  ) {
    @SerialName("organization")
    Organization("organization"),
    @SerialName("enterprise")
    Enterprise("enterprise"),
    ;
  }
}
