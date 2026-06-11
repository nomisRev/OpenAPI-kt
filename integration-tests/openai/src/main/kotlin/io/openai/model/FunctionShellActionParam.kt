package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commands and limits describing how to run the shell tool call.
 */
@Serializable
public data class FunctionShellActionParam(
  public val commands: List<String>,
  @SerialName("timeout_ms")
  public val timeoutMs: Long? = null,
  @SerialName("max_output_length")
  public val maxOutputLength: Long? = null,
)
