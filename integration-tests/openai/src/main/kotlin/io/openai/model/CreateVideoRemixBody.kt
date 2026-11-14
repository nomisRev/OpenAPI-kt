package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Parameters for remixing an existing generated video.
 */
@Serializable
public data class CreateVideoRemixBody(
  public val prompt: String,
)
