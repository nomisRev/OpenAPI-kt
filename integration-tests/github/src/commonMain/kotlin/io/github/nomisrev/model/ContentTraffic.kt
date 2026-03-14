package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentTraffic(val path: String, val title: String, val count: Long, val uniques: Long)
