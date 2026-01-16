package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class RestCorsSettingsRequest(val allowedOrigins: List<String>? = null, val allowAllOrigins: Boolean? = null)
