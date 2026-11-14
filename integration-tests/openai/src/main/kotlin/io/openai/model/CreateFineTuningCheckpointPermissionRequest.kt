package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateFineTuningCheckpointPermissionRequest(
  @SerialName("project_ids")
  public val projectIds: List<String>,
)
