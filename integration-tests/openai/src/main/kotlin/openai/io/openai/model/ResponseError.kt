package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An error object returned when the model fails to generate a Response.
 *
 */
@Serializable
public data class ResponseError(
  public val code: ResponseErrorCode,
  public val message: String,
)
