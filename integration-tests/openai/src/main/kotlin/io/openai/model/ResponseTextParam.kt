package io.openai.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Configuration options for a text response from the model. Can be plain
 * text or structured JSON data. Learn more:
 * - [Text inputs and outputs](/docs/guides/text)
 * - [Structured Outputs](/docs/guides/structured-outputs)
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ResponseTextParam(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val format: TextResponseFormatConfiguration? = null,
  public val verbosity: Verbosity? = null,
)
