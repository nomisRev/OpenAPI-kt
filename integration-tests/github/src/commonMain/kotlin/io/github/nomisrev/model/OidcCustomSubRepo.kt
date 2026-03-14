package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OidcCustomSubRepo(
    @SerialName("use_default") val useDefault: Boolean,
    @SerialName("include_claim_keys") val includeClaimKeys: List<String>? = null,
)
