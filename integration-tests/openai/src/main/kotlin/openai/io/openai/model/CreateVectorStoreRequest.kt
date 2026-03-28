package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class CreateVectorStoreRequest(
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  public val name: String? = null,
  public val description: String? = null,
  @SerialName("expires_after")
  public val expiresAfter: VectorStoreExpirationAfter? = null,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: ChunkingStrategy? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * The chunking strategy used to chunk the file(s). If not set, will use the `auto` strategy. Only applicable if `file_ids` is non-empty.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface ChunkingStrategy {
    @Serializable
    @SerialName("auto")
    public data object Auto : ChunkingStrategy

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
    ) : ChunkingStrategy
  }
}
