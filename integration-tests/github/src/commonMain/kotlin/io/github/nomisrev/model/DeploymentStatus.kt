package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Required

@Serializable
data class DeploymentStatus(
    val url: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val state: State,
    val creator: NullableSimpleUser?,
    @Required val description: String,
    val environment: String? = null,
    @SerialName("target_url") @Required val targetUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("deployment_url") val deploymentUrl: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("environment_url") val environmentUrl: String? = null,
    @SerialName("log_url") val logUrl: String? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
) {
    @Serializable
    enum class State {
        @SerialName("error")
        Error,
        @SerialName("failure")
        Failure,
        @SerialName("inactive")
        Inactive,
        @SerialName("pending")
        Pending,
        @SerialName("success")
        Success,
        @SerialName("queued")
        Queued,
        @SerialName("in_progress")
        InProgress;
    }
}
