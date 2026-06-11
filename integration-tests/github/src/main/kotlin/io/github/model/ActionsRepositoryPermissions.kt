package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsRepositoryPermissions(
  public val enabled: ActionsEnabled,
  @SerialName("allowed_actions")
  public val allowedActions: AllowedActions? = null,
  @SerialName("selected_actions_url")
  public val selectedActionsUrl: SelectedActionsUrl? = null,
  @SerialName("sha_pinning_required")
  public val shaPinningRequired: ShaPinningRequired? = null,
)
