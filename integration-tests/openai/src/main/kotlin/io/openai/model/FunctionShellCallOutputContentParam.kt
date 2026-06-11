package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Captured stdout and stderr for a portion of a shell tool call output.
 */
@Serializable
public data class FunctionShellCallOutputContentParam(
  public val stdout: String,
  public val stderr: String,
  public val outcome: FunctionShellCallOutputOutcomeParam,
)
