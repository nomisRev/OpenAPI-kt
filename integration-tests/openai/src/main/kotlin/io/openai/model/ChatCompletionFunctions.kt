package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionFunctions(
  public val description: String? = null,
  public val name: String,
  public val parameters: FunctionParameters? = null,
)
