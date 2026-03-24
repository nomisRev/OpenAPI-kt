package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Error(
  public val code: String?,
  public val message: String,
  public val `param`: String?,
  public val type: String,
)
