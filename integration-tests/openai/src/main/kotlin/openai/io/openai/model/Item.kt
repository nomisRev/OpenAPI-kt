package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Content item used to generate a response.
 *
 */
@JvmInline
@Serializable
public value class Item(
  public val `value`: JsonElement,
)
