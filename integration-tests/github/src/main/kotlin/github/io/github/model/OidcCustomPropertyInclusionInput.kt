package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Input for creating an OIDC custom property inclusion
 */
@JvmInline
@Serializable
public value class OidcCustomPropertyInclusionInput(
  @SerialName("custom_property_name")
  public val customPropertyName: String,
)
