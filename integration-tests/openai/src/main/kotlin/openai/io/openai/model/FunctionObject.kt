package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class FunctionObject(
  public val description: String? = null,
  public val name: String,
  public val parameters: FunctionParameters? = null,
  public val strict: Boolean? = null,
)
