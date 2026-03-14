package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RunnerGroupsOrg(
    val id: Double,
    val name: String,
    val visibility: String,
    val default: Boolean,
    @SerialName("selected_repositories_url") val selectedRepositoriesUrl: String? = null,
    @SerialName("runners_url") val runnersUrl: String,
    @SerialName("hosted_runners_url") val hostedRunnersUrl: String? = null,
    @SerialName("network_configuration_id") val networkConfigurationId: String? = null,
    val inherited: Boolean,
    @SerialName("inherited_allows_public_repositories") val inheritedAllowsPublicRepositories: Boolean? = null,
    @SerialName("allows_public_repositories") val allowsPublicRepositories: Boolean,
    @SerialName("workflow_restrictions_read_only") val workflowRestrictionsReadOnly: Boolean? = null,
    @SerialName("restricted_to_workflows") val restrictedToWorkflows: Boolean? = null,
    @SerialName("selected_workflows") val selectedWorkflows: List<String>? = null,
)
