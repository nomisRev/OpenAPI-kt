package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ErrorResponse(
  public val error: Error,
)
