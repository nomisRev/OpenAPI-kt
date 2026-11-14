package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commands and limits describing how to run the shell tool call.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FunctionShellActionParam(
  public val commands: List<String>,
  @SerialName("timeout_ms")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeoutMs: Optional<Long> = Optional.Absent,
  @SerialName("max_output_length")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxOutputLength: Optional<Long> = Optional.Absent,
)
