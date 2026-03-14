package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ViewTraffic(val count: Long, val uniques: Long, val views: List<Traffic>)
