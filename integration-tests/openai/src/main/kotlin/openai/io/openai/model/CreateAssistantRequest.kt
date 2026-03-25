package io.openai.model

import kotlin.Boolean
import kotlin.Double
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
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateAssistantRequest(
  public val model: Model,
  public val name: String? = null,
  public val description: String? = null,
  public val instructions: String? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  public val tools: List<Tools>? = null,
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  @SerialName("response_format")
  public val responseFormat: AssistantsApiResponseFormatOption? = null,
) {
  /**
   * ID of the model to use. You can use the [List models](/docs/api-reference/models/list) API to see all of your available models, or see our [Model overview](/docs/models) for descriptions of them.
   *
   */
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Model

    @Serializable
    @JvmInline
    public value class CaseAssistantSupportedModels(
      public val `value`: AssistantSupportedModels,
    ) : Model

    public object Serializer : KSerializer<Model> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateAssistantRequest.Model", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseAssistantSupportedModels", AssistantSupportedModels.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Model {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseAssistantSupportedModels::class to { CaseAssistantSupportedModels(decodeFromJsonElement(AssistantSupportedModels.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseAssistantSupportedModels -> encoder.encodeSerializableValue(AssistantSupportedModels.serializer(), value.value)
        }
      }
    }
  }

  /**
   * A set of resources that are used by the assistant's tools. The resources are specific to the type of tool. For example, the `code_interpreter` tool requires a list of file IDs, while the `file_search` tool requires a list of vector store IDs.
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
        public val chunkingStrategy: JsonElement? = null,
        public val metadata: Metadata? = null,
      )
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @SerialName("code_interpreter")
    public data object CodeInterpreter : Tools

    @JvmInline
    @SerialName("file_search")
    @Serializable
    public value class FileSearch(
      @SerialName("file_search")
      public val fileSearch: FileSearch? = null,
    ) : Tools {
      /**
       * Overrides for the file search tool.
       */
      @Serializable
      public data class FileSearch(
        @SerialName("max_num_results")
        public val maxNumResults: Long? = null,
        @SerialName("ranking_options")
        public val rankingOptions: FileSearchRankingOptions? = null,
      )
    }

    @SerialName("function")
    @Serializable
    public data class Function(
      public val description: String? = null,
      public val name: String,
      public val parameters: FunctionParameters? = null,
      public val strict: Boolean? = null,
    ) : Tools
  }
}
