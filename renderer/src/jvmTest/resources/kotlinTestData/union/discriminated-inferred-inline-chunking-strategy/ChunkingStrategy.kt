package io.github.nomisrev.render.test.union.discriminated.inferred.`inline`.chunking.strategy

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ChunkingStrategy {
  @Serializable
  @SerialName("auto")
  public data object Auto : ChunkingStrategy

  @SerialName("static")
  @Serializable
  public data class Static(
    @SerialName("max_chunk_size_tokens")
    public val maxChunkSizeTokens: Long,
    @SerialName("chunk_overlap_tokens")
    public val chunkOverlapTokens: Long,
  ) : ChunkingStrategy
}
