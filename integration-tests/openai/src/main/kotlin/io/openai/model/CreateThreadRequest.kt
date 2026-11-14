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

/**
 * Options to create a new thread. If no thread is provided when running a
 * request, an empty thread will be created.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateThreadRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val messages: List<CreateMessageRequest>? = null,
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * A set of resources that are made available to the assistant's tools in this thread. The resources are specific to the type of tool. For example, the `code_interpreter` tool requires a list of file IDs, while the `file_search` tool requires a list of vector store IDs.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ToolResources(
    @SerialName("code_interpreter")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codeInterpreter: CodeInterpreter? = null,
    @SerialName("file_search")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileSearch: FileSearch? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class CodeInterpreter(
      @SerialName("file_ids")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileIds: List<String>? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FileSearch(
      @SerialName("vector_store_ids")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val vectorStoreIds: List<String>? = null,
      @SerialName("vector_stores")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val vectorStores: List<VectorStores>? = null,
    ) {
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class VectorStores(
        @SerialName("file_ids")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val fileIds: List<String>? = null,
        @SerialName("chunking_strategy")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val chunkingStrategy: ChunkingStrategy? = null,
        public val metadata: Metadata? = null,
      ) {
        /**
         * The chunking strategy used to chunk the file(s). If not set, will use the `auto` strategy.
         */
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
      }
    }
  }
}
