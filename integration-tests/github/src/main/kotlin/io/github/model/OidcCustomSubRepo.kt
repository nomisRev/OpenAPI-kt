package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actions OIDC subject customization for a repository
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OidcCustomSubRepo(
  @SerialName("use_default")
  public val useDefault: Boolean,
  @SerialName("include_claim_keys")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val includeClaimKeys: List<String>? = null,
)
