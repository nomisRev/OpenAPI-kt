package io.openai.model

import kotlinx.serialization.Serializable

/**
 * Configuration options for a text response from the model. Can be plain
 * text or structured JSON data. Learn more:
 * - [Text inputs and outputs](/docs/guides/text)
 * - [Structured Outputs](/docs/guides/structured-outputs)
 *
 */
@Serializable
public data class ResponseTextParam(
  public val format: TextResponseFormatConfiguration? = null,
  public val verbosity: Verbosity? = null,
)
