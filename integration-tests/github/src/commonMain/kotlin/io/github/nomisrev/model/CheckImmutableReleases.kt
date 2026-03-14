package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CheckImmutableReleases(val enabled: Boolean, @SerialName("enforced_by_owner") val enforcedByOwner: Boolean)
