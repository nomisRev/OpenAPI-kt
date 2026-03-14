package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsRepositoryPermissions(
    val enabled: ActionsEnabled,
    @SerialName("allowed_actions") val allowedActions: AllowedActions? = null,
    @SerialName("selected_actions_url") val selectedActionsUrl: SelectedActionsUrl? = null,
    @SerialName("sha_pinning_required") val shaPinningRequired: ShaPinningRequired? = null,
)
