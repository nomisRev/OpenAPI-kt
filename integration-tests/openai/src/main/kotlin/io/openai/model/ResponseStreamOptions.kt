package io.openai.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Options for streaming responses. Only set this when you set `stream: true`.
 *
 */
@JvmInline
@Serializable
public value class ResponseStreamOptions(
  @SerialName("include_obfuscation")
  public val includeObfuscation: Boolean? = null,
)
