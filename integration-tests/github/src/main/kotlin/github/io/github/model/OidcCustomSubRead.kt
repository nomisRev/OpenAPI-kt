package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actions OIDC Subject customization
 */
@JvmInline
@Serializable
public value class OidcCustomSubRead(
  @SerialName("include_claim_keys")
  public val includeClaimKeys: List<String>,
)
