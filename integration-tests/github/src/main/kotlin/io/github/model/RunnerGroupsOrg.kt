package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RunnerGroupsOrg(
  public val id: Double,
  public val name: String,
  public val visibility: String,
  public val default: Boolean,
  @SerialName("selected_repositories_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val selectedRepositoriesUrl: String? = null,
  @SerialName("runners_url")
  public val runnersUrl: String,
  @SerialName("hosted_runners_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hostedRunnersUrl: String? = null,
  @SerialName("network_configuration_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val networkConfigurationId: String? = null,
  public val inherited: Boolean,
  @SerialName("inherited_allows_public_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inheritedAllowsPublicRepositories: Boolean? = null,
  @SerialName("allows_public_repositories")
  public val allowsPublicRepositories: Boolean,
  @SerialName("workflow_restrictions_read_only")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workflowRestrictionsReadOnly: Boolean? = null,
  @SerialName("restricted_to_workflows")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val restrictedToWorkflows: Boolean? = null,
  @SerialName("selected_workflows")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val selectedWorkflows: List<String>? = null,
)
