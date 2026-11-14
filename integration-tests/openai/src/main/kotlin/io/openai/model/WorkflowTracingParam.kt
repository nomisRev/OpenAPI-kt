package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Controls diagnostic tracing during the session.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkflowTracingParam(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
)
