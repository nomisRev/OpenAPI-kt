package io.openai.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

/**
 * The number of partial images to generate. This parameter is used for
 * streaming responses that return partial images. Value must be between 0 and 3.
 * When set to 0, the response will be a single image sent in one streaming event.
 *
 * Note that the final image may be sent before the full number of partial images
 * are generated if the full image is generated more quickly.
 *
 */
@JvmInline
@Serializable
public value class PartialImages(
  @Required
  public val `value`: Long = 0L,
)
