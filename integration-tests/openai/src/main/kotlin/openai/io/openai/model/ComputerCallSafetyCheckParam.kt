package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A pending safety check for the computer call.
 */
@Serializable
public data class ComputerCallSafetyCheckParam(
  public val id: String,
  public val code: String? = null,
  public val message: String? = null,
)
