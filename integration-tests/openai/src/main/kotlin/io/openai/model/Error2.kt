package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An error that occurred while generating the response.
 */
@Serializable
public data class Error2(
  public val code: String,
  public val message: String,
)
