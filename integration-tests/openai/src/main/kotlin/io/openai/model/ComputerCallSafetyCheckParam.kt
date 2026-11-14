package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * A pending safety check for the computer call.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ComputerCallSafetyCheckParam(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val code: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: Optional<String> = Optional.Absent,
)
