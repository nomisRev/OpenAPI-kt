package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actions OIDC Subject customization
 */
@Serializable
public data class OidcCustomSub(
  @SerialName("include_claim_keys")
  public val includeClaimKeys: List<String>,
)
