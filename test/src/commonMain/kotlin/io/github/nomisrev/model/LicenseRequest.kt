package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class LicenseRequest(val username: String? = null, val license: String? = null)
