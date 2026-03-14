package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NetworkConfiguration(
    val id: String,
    val name: String,
    @SerialName("compute_service") val computeService: ComputeService? = null,
    @SerialName("network_settings_ids") val networkSettingsIds: List<String>? = null,
    @SerialName("failover_network_settings_ids") val failoverNetworkSettingsIds: List<String>? = null,
    @SerialName("failover_network_enabled") val failoverNetworkEnabled: Boolean? = null,
    @SerialName("created_on") val createdOn: LocalDateTime?,
) {
    @Serializable
    enum class ComputeService {
        @SerialName("none") None, @SerialName("actions") Actions, @SerialName("codespaces") Codespaces;
    }
}
