package io.openai.model

import kotlin.Double
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
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class ModifyAssistantRequest(
  public val model: Model? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  public val name: String? = null,
  public val description: String? = null,
  public val instructions: String? = null,
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

    @JvmInline
    @Serializable
    public value class FileSearch(
      @SerialName("vector_store_ids")
      public val vectorStoreIds: List<String>? = null,
    )
  }

  @Serializable(with = Tools.Serializer::class)
  public sealed interface Tools {
    @Serializable
    @JvmInline
    public value class CaseAssistantToolsCode(
      public val `value`: AssistantToolsCode,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseAssistantToolsFileSearch(
      public val `value`: AssistantToolsFileSearch,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseAssistantToolsFunction(
      public val `value`: AssistantToolsFunction,
    ) : Tools

    public object Serializer : KSerializer<Tools> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.ModifyAssistantRequest.Tools", PolymorphicKind.SEALED) {
        element("CaseAssistantToolsCode", AssistantToolsCode.serializer().descriptor)
        element("CaseAssistantToolsFileSearch", AssistantToolsFileSearch.serializer().descriptor)
        element("CaseAssistantToolsFunction", AssistantToolsFunction.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Tools {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseAssistantToolsCode::class to { CaseAssistantToolsCode(decodeFromJsonElement(AssistantToolsCode.serializer(), it)) },
          CaseAssistantToolsFileSearch::class to { CaseAssistantToolsFileSearch(decodeFromJsonElement(AssistantToolsFileSearch.serializer(), it)) },
          CaseAssistantToolsFunction::class to { CaseAssistantToolsFunction(decodeFromJsonElement(AssistantToolsFunction.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Tools) {
        when(value) {
          is CaseAssistantToolsCode -> encoder.encodeSerializableValue(AssistantToolsCode.serializer(), value.value)
          is CaseAssistantToolsFileSearch -> encoder.encodeSerializableValue(AssistantToolsFileSearch.serializer(), value.value)
          is CaseAssistantToolsFunction -> encoder.encodeSerializableValue(AssistantToolsFunction.serializer(), value.value)
        }
      }
    }
  }
}
