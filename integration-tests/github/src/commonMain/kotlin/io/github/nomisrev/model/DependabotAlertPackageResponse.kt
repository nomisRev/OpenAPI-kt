package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class DependabotAlertPackageResponse(val ecosystem: String, val name: String)
