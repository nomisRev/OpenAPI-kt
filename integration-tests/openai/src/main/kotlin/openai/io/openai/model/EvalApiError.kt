package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An object representing an error response from the Eval API.
 *
 */
@Serializable
public data class EvalApiError(
  public val code: String,
  public val message: String,
)
