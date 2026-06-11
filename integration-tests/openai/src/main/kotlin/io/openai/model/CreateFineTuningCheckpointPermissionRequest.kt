package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class CreateFineTuningCheckpointPermissionRequest(
  @SerialName("project_ids")
  public val projectIds: List<String>,
)
