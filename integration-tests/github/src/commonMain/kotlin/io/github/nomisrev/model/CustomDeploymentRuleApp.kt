package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CustomDeploymentRuleApp(
    val id: Long,
    val slug: String,
    @SerialName("integration_url") val integrationUrl: String,
    @SerialName("node_id") val nodeId: String,
)
