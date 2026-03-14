package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Traffic(val timestamp: LocalDateTime, val uniques: Long, val count: Long)
