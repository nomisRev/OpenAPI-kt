package io.openai.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Controls diagnostic tracing during the session.
 */
@Serializable
public data class ChatkitWorkflowTracing(
  public val enabled: Boolean,
)
