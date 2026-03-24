package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a transcription response returned by model, based on the provided input.
 */
@Serializable
public data class CreateTranscriptionResponseJson(
  public val text: String,
  public val logprobs: List<Logprobs>? = null,
  public val usage: JsonElement? = null,
) {
  @Serializable
  public data class Logprobs(
    public val token: String? = null,
    public val logprob: Double? = null,
    public val bytes: List<Double>? = null,
  )
}
