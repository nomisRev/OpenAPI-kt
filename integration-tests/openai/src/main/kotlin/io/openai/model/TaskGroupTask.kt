package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Task entry that appears within a TaskGroup.
 */
@Serializable
public data class TaskGroupTask(
  public val type: TaskType,
  public val heading: String?,
  public val summary: String?,
)
