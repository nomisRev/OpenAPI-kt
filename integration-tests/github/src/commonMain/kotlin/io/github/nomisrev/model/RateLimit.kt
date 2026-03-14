package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class RateLimit(val limit: Long, val remaining: Long, val reset: Long, val used: Long)
