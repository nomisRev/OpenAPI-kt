package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A chat message that makes up the prompt or context. May include variable references to the `item` namespace, ie {{item.name}}.
 */
@JvmInline
@Serializable
public value class CreateEvalItem(
  public val `value`: JsonElement,
)
