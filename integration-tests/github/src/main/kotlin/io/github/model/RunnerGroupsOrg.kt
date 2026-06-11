package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RunnerGroupsOrg(
  public val id: Double,
  public val name: String,
  public val visibility: String,
  public val default: Boolean,
  @SerialName("selected_repositories_url")
  public val selectedRepositoriesUrl: String? = null,
  @SerialName("runners_url")
  public val runnersUrl: String,
  @SerialName("hosted_runners_url")
  public val hostedRunnersUrl: String? = null,
  @SerialName("network_configuration_id")
  public val networkConfigurationId: String? = null,
  public val inherited: Boolean,
  @SerialName("inherited_allows_public_repositories")
  public val inheritedAllowsPublicRepositories: Boolean? = null,
  @SerialName("allows_public_repositories")
  public val allowsPublicRepositories: Boolean,
  @SerialName("workflow_restrictions_read_only")
  public val workflowRestrictionsReadOnly: Boolean? = null,
  @SerialName("restricted_to_workflows")
  public val restrictedToWorkflows: Boolean? = null,
  @SerialName("selected_workflows")
  public val selectedWorkflows: List<String>? = null,
)
