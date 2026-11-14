package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public data class ErrorResponse(
  public val error: Error,
)
