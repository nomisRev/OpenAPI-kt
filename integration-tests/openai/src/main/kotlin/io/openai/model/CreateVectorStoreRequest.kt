package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateVectorStoreRequest(
  @SerialName("file_ids")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileIds: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @SerialName("expires_after")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val expiresAfter: VectorStoreExpirationAfter? = null,
  @SerialName("chunking_strategy")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val chunkingStrategy: ChunkingStrategy? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
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
