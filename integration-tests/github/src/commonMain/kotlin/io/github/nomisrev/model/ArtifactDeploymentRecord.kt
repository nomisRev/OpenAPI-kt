package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ArtifactDeploymentRecord(
    val id: Long? = null,
    val digest: String? = null,
    @SerialName("logical_environment") val logicalEnvironment: String? = null,
    @SerialName("physical_environment") val physicalEnvironment: String? = null,
    val cluster: String? = null,
    @SerialName("deployment_name") val deploymentName: String? = null,
    val tags: List<String>? = null,
    @SerialName("runtime_risks") val runtimeRisks: List<RuntimeRisks>? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("attestation_id") val attestationId: Long? = null,
) {
    @Serializable
    enum class RuntimeRisks {
        @SerialName("critical-resource")
        CriticalResource,
        @SerialName("internet-exposed")
        InternetExposed,
        @SerialName("lateral-movement")
        LateralMovement,
        @SerialName("sensitive-data")
        SensitiveData;
    }
}
