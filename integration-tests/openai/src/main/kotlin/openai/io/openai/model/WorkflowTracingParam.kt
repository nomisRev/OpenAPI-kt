package io.openai.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Controls diagnostic tracing during the session.
 */
@JvmInline
@Serializable
public value class WorkflowTracingParam(
  public val enabled: Boolean? = null,
)
