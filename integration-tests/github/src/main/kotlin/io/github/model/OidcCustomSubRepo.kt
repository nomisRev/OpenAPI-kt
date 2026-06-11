package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actions OIDC subject customization for a repository
 */
@Serializable
public data class OidcCustomSubRepo(
  @SerialName("use_default")
  public val useDefault: Boolean,
  @SerialName("include_claim_keys")
  public val includeClaimKeys: List<String>? = null,
)
