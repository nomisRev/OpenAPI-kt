package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * The chunking strategy used to chunk the file(s). If not set, will use the `auto` strategy.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ChunkingStrategyRequestParam {
  @Serializable
  @SerialName("auto")
  public data object Auto : ChunkingStrategyRequestParam

  /**
   * Customize your own chunking strategy by setting chunk size and chunk overlap.
   */
  @SerialName("static")
  @Serializable
  public data class Static(
    @SerialName("max_chunk_size_tokens")
    public val maxChunkSizeTokens: Long,
    @SerialName("chunk_overlap_tokens")
    public val chunkOverlapTokens: Long,
  ) : ChunkingStrategyRequestParam
}
