package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferrerTraffic(val referrer: String, val count: Long, val uniques: Long)
