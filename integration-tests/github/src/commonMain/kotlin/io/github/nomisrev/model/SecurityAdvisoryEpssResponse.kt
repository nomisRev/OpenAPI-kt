package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SecurityAdvisoryEpssResponse(val percentage: Double? = null, val percentile: Double? = null)
