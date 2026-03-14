package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeploymentSimple(
    val url: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val task: String,
    @SerialName("original_environment") val originalEnvironment: String? = null,
    val environment: String,
    val description: String?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("statuses_url") val statusesUrl: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("transient_environment") val transientEnvironment: Boolean? = null,
    @SerialName("production_environment") val productionEnvironment: Boolean? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
)
