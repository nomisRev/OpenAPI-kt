package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ParticipationStats(val all: List<Long>, val owner: List<Long>)
