package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsOrganizationPermissions(
  @SerialName("enabled_repositories")
  public val enabledRepositories: EnabledRepositories,
  @SerialName("selected_repositories_url")
  public val selectedRepositoriesUrl: String? = null,
  @SerialName("allowed_actions")
  public val allowedActions: AllowedActions? = null,
  @SerialName("selected_actions_url")
  public val selectedActionsUrl: SelectedActionsUrl? = null,
  @SerialName("sha_pinning_required")
  public val shaPinningRequired: ShaPinningRequired? = null,
)
