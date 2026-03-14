package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeploymentProtectionRule(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val enabled: Boolean,
    val app: CustomDeploymentRuleApp,
)
