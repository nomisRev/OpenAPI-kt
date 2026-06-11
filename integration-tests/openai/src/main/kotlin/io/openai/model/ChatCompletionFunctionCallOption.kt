package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Specifying a particular function via `{"name": "my_function"}` forces the model to call that function.
 *
 */
@JvmInline
@Serializable
public value class ChatCompletionFunctionCallOption(
  public val name: String,
)
