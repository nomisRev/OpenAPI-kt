package io.openai.model

import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A list of inputs, each of which may be either an input text, output text, input
 * image, or input audio object.
 *
 */
@JvmInline
@Serializable
public value class EvalItemContentArray(
  public val items: List<EvalItemContentItem>,
)
