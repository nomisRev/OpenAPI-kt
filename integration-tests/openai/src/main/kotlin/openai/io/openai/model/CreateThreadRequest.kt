package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Options to create a new thread. If no thread is provided when running a
 * request, an empty thread will be created.
 *
 */
@Serializable
public data class CreateThreadRequest(
  public val messages: List<CreateMessageRequest>? = null,
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * A set of resources that are made available to the assistant's tools in this thread. The resources are specific to the type of tool. For example, the `code_interpreter` tool requires a list of file IDs, while the `file_search` tool requires a list of vector store IDs.
   *
   */
  @Serializable
  public data class ToolResources(
    @SerialName("code_interpreter")
    public val codeInterpreter: CodeInterpreter? = null,
    @SerialName("file_search")
    public val fileSearch: FileSearch? = null,
  ) {
    @JvmInline
    @Serializable
    public value class CodeInterpreter(
      @SerialName("file_ids")
      public val fileIds: List<String>? = null,
    )

    @Serializable
    public data class FileSearch(
      @SerialName("vector_store_ids")
      public val vectorStoreIds: List<String>? = null,
      @SerialName("vector_stores")
      public val vectorStores: List<VectorStores>? = null,
    ) {
      @Serializable
      public data class VectorStores(
        @SerialName("file_ids")
        public val fileIds: List<String>? = null,
        @SerialName("chunking_strategy")
        public val chunkingStrategy: ChunkingStrategy? = null,
        public val metadata: Metadata? = null,
      ) {
        /**
         * The chunking strategy used to chunk the file(s). If not set, will use the `auto` strategy.
         */
        @Serializable(with = ChunkingStrategy.Serializer::class)
        public sealed interface ChunkingStrategy {
          /**
           * The default strategy. This strategy currently uses a `max_chunk_size_tokens` of `800` and `chunk_overlap_tokens` of `400`.
           */
          @JvmInline
          @Serializable
          public value class Auto(
            public val type: Type,
          ) : ChunkingStrategy {
            @Serializable
            public enum class Type(
              public val `value`: String,
            ) {
              @SerialName("auto")
              Auto("auto"),
              ;
            }
          }

          @Serializable
          public data class Static(
            public val type: Type,
            public val static: Static,
          ) : ChunkingStrategy {
            @Serializable
            public data class Static(
              @SerialName("max_chunk_size_tokens")
              public val maxChunkSizeTokens: Long,
              @SerialName("chunk_overlap_tokens")
              public val chunkOverlapTokens: Long,
            )

            @Serializable
            public enum class Type(
              public val `value`: String,
            ) {
              @SerialName("static")
              Static("static"),
              ;
            }
          }

          public object Serializer : KSerializer<ChunkingStrategy> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.openai.model.CreateThreadRequest.ToolResources.FileSearch.VectorStores.ChunkingStrategy", PolymorphicKind.SEALED) {
              element("Auto", Auto.serializer().descriptor)
              element("Static", Static.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): ChunkingStrategy {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                Static::class to { decodeFromJsonElement(Static.serializer(), it) },
                Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: ChunkingStrategy) {
              when(value) {
                is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
                is Static -> encoder.encodeSerializableValue(Static.serializer(), value)
              }
            }
          }
        }
      }
    }
  }
}
