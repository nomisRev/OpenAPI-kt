package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class InteractionLimit(val limit: InteractionGroup, val expiry: InteractionExpiry? = null)
