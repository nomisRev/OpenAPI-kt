package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The chunking strategy used to chunk the file(s). If not set, will use the `auto` strategy.
 */
@JvmInline
@Serializable
public value class ChunkingStrategyRequestParam(
  public val `value`: JsonElement,
)
