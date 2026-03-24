package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model and tool overrides applied when generating the assistant response.
 */
@Serializable
public data class InferenceOptions(
  @SerialName("tool_choice")
  public val toolChoice: ToolChoice?,
  public val model: String?,
)
