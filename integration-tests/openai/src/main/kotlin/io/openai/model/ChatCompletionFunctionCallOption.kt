package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Specifying a particular function via `{"name": "my_function"}` forces the model to call that function.
 *
 */
@Serializable
public data class ChatCompletionFunctionCallOption(
  public val name: String,
)
