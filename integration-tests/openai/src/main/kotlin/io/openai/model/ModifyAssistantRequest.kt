package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ModifyAssistantRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: Model? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  public val name: String? = null,
  public val description: String? = null,
  public val instructions: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
          buildSerialDescriptor("io.openai.model.ModifyAssistantRequest.Model", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseAssistantSupportedModels", AssistantSupportedModels.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Model {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseAssistantSupportedModels::class to { CaseAssistantSupportedModels(decodeFromJsonElement(AssistantSupportedModels.serializer(), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
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
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @SerialName("code_interpreter")
    public data object CodeInterpreter : Tools

    @SerialName("file_search")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FileSearch(
      @SerialName("file_search")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileSearch: FileSearch? = null,
    ) : Tools {
      /**
       * Overrides for the file search tool.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class FileSearch(
        @SerialName("max_num_results")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val maxNumResults: Long? = null,
        @SerialName("ranking_options")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val rankingOptions: FileSearchRankingOptions? = null,
      )
    }

    @SerialName("function")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Function(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val parameters: FunctionParameters? = null,
      public val strict: Boolean? = null,
    ) : Tools
  }
}
