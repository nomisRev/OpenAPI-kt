package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Execute a shell command.
 */
@Serializable
public data class FunctionShellAction(
  public val commands: List<String>,
  @SerialName("timeout_ms")
  public val timeoutMs: Long?,
  @SerialName("max_output_length")
  public val maxOutputLength: Long?,
)
