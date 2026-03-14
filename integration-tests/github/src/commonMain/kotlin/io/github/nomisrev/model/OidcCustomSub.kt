package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class OidcCustomSub(@SerialName("include_claim_keys") val includeClaimKeys: List<String>)
