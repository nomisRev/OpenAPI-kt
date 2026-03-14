package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NetworkSettings(
    val id: String,
    @SerialName("network_configuration_id") val networkConfigurationId: String? = null,
    val name: String,
    @SerialName("subnet_id") val subnetId: String,
    val region: String,
)
