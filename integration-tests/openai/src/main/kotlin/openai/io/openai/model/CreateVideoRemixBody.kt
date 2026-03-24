package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Parameters for remixing an existing generated video.
 */
@JvmInline
@Serializable
public value class CreateVideoRemixBody(
  public val prompt: String,
)
