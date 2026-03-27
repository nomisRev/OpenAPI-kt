package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A text input to the model.
 *
 */
@JvmInline
@Serializable
public value class EvalItemContentText(
  public val `value`: String,
)
