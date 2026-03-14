package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CloneTraffic(val count: Long, val uniques: Long, val clones: List<Traffic>)
