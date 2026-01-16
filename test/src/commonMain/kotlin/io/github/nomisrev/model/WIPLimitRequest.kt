package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WIPLimitRequest(val max: Int? = null, val min: Int? = null, val column: AgileColumnRequest? = null)
