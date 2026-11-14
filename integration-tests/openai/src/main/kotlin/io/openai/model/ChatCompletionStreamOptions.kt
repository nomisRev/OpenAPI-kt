package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for streaming response. Only set this when you set `stream: true`.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ChatCompletionStreamOptions(
  @SerialName("include_usage")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val includeUsage: Boolean? = null,
  @SerialName("include_obfuscation")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val includeObfuscation: Boolean? = null,
)
