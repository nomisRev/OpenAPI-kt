package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsRepositoryPermissions(
  public val enabled: ActionsEnabled,
  @SerialName("allowed_actions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowedActions: AllowedActions? = null,
  @SerialName("selected_actions_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val selectedActionsUrl: SelectedActionsUrl? = null,
  @SerialName("sha_pinning_required")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val shaPinningRequired: ShaPinningRequired? = null,
)
