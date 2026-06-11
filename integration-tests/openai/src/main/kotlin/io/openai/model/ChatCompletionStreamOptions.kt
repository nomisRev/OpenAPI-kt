package io.openai.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for streaming response. Only set this when you set `stream: true`.
 *
 */
@Serializable
public data class ChatCompletionStreamOptions(
  @SerialName("include_usage")
  public val includeUsage: Boolean? = null,
  @SerialName("include_obfuscation")
  public val includeObfuscation: Boolean? = null,
)
