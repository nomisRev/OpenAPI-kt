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
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateThreadAndRunRequest(
  @SerialName("assistant_id")
  public val assistantId: String,
  public val thread: CreateThreadRequest? = null,
  public val model: Model? = null,
  public val instructions: String? = null,
  public val tools: List<Tools>? = null,
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  public val stream: Boolean? = null,
  @SerialName("max_prompt_tokens")
  public val maxPromptTokens: Long? = null,
  @SerialName("max_completion_tokens")
  public val maxCompletionTokens: Long? = null,
  @SerialName("truncation_strategy")
  public val truncationStrategy: TruncationObject? = null,
  @SerialName("tool_choice")
  public val toolChoice: AssistantsApiToolChoiceOption? = null,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: ParallelToolCalls? = null,
  @SerialName("response_format")
  public val responseFormat: AssistantsApiResponseFormatOption? = null,
) {
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class CaseEnum(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-5")
      Gpt5("gpt-5"),
      @SerialName("gpt-5-mini")
      Gpt5Mini("gpt-5-mini"),
      @SerialName("gpt-5-nano")
      Gpt5Nano("gpt-5-nano"),
      @SerialName("gpt-5-2025-08-07")
      Gpt520250807("gpt-5-2025-08-07"),
      @SerialName("gpt-5-mini-2025-08-07")
      Gpt5Mini20250807("gpt-5-mini-2025-08-07"),
      @SerialName("gpt-5-nano-2025-08-07")
      Gpt5Nano20250807("gpt-5-nano-2025-08-07"),
      @SerialName("gpt-4.1")
      Gpt41("gpt-4.1"),
      @SerialName("gpt-4.1-mini")
      Gpt41Mini("gpt-4.1-mini"),
      @SerialName("gpt-4.1-nano")
      Gpt41Nano("gpt-4.1-nano"),
      @SerialName("gpt-4.1-2025-04-14")
      Gpt4120250414("gpt-4.1-2025-04-14"),
      @SerialName("gpt-4.1-mini-2025-04-14")
      Gpt41Mini20250414("gpt-4.1-mini-2025-04-14"),
      @SerialName("gpt-4.1-nano-2025-04-14")
      Gpt41Nano20250414("gpt-4.1-nano-2025-04-14"),
      @SerialName("gpt-4o")
      Gpt4o("gpt-4o"),
      @SerialName("gpt-4o-2024-11-20")
      Gpt4o20241120("gpt-4o-2024-11-20"),
      @SerialName("gpt-4o-2024-08-06")
      Gpt4o20240806("gpt-4o-2024-08-06"),
      @SerialName("gpt-4o-2024-05-13")
      Gpt4o20240513("gpt-4o-2024-05-13"),
      @SerialName("gpt-4o-mini")
      Gpt4oMini("gpt-4o-mini"),
      @SerialName("gpt-4o-mini-2024-07-18")
      Gpt4oMini20240718("gpt-4o-mini-2024-07-18"),
      @SerialName("gpt-4.5-preview")
      Gpt45Preview("gpt-4.5-preview"),
      @SerialName("gpt-4.5-preview-2025-02-27")
      Gpt45Preview20250227("gpt-4.5-preview-2025-02-27"),
      @SerialName("gpt-4-turbo")
      Gpt4Turbo("gpt-4-turbo"),
      @SerialName("gpt-4-turbo-2024-04-09")
      Gpt4Turbo20240409("gpt-4-turbo-2024-04-09"),
      @SerialName("gpt-4-0125-preview")
      Gpt40125Preview("gpt-4-0125-preview"),
      @SerialName("gpt-4-turbo-preview")
      Gpt4TurboPreview("gpt-4-turbo-preview"),
      @SerialName("gpt-4-1106-preview")
      Gpt41106Preview("gpt-4-1106-preview"),
      @SerialName("gpt-4-vision-preview")
      Gpt4VisionPreview("gpt-4-vision-preview"),
      @SerialName("gpt-4")
      Gpt4("gpt-4"),
      @SerialName("gpt-4-0314")
      Gpt40314("gpt-4-0314"),
      @SerialName("gpt-4-0613")
      Gpt40613("gpt-4-0613"),
      @SerialName("gpt-4-32k")
      Gpt432k("gpt-4-32k"),
      @SerialName("gpt-4-32k-0314")
      Gpt432k0314("gpt-4-32k-0314"),
      @SerialName("gpt-4-32k-0613")
      Gpt432k0613("gpt-4-32k-0613"),
      @SerialName("gpt-3.5-turbo")
      Gpt35Turbo("gpt-3.5-turbo"),
      @SerialName("gpt-3.5-turbo-16k")
      Gpt35Turbo16k("gpt-3.5-turbo-16k"),
      @SerialName("gpt-3.5-turbo-0613")
      Gpt35Turbo0613("gpt-3.5-turbo-0613"),
      @SerialName("gpt-3.5-turbo-1106")
      Gpt35Turbo1106("gpt-3.5-turbo-1106"),
      @SerialName("gpt-3.5-turbo-0125")
      Gpt35Turbo0125("gpt-3.5-turbo-0125"),
      @SerialName("gpt-3.5-turbo-16k-0613")
      Gpt35Turbo16k0613("gpt-3.5-turbo-16k-0613"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          CaseEnum.Gpt5 -> encoder.encodeString("gpt-5")
          CaseEnum.Gpt5Mini -> encoder.encodeString("gpt-5-mini")
          CaseEnum.Gpt5Nano -> encoder.encodeString("gpt-5-nano")
          CaseEnum.Gpt520250807 -> encoder.encodeString("gpt-5-2025-08-07")
          CaseEnum.Gpt5Mini20250807 -> encoder.encodeString("gpt-5-mini-2025-08-07")
          CaseEnum.Gpt5Nano20250807 -> encoder.encodeString("gpt-5-nano-2025-08-07")
          CaseEnum.Gpt41 -> encoder.encodeString("gpt-4.1")
          CaseEnum.Gpt41Mini -> encoder.encodeString("gpt-4.1-mini")
          CaseEnum.Gpt41Nano -> encoder.encodeString("gpt-4.1-nano")
          CaseEnum.Gpt4120250414 -> encoder.encodeString("gpt-4.1-2025-04-14")
          CaseEnum.Gpt41Mini20250414 -> encoder.encodeString("gpt-4.1-mini-2025-04-14")
          CaseEnum.Gpt41Nano20250414 -> encoder.encodeString("gpt-4.1-nano-2025-04-14")
          CaseEnum.Gpt4o -> encoder.encodeString("gpt-4o")
          CaseEnum.Gpt4o20241120 -> encoder.encodeString("gpt-4o-2024-11-20")
          CaseEnum.Gpt4o20240806 -> encoder.encodeString("gpt-4o-2024-08-06")
          CaseEnum.Gpt4o20240513 -> encoder.encodeString("gpt-4o-2024-05-13")
          CaseEnum.Gpt4oMini -> encoder.encodeString("gpt-4o-mini")
          CaseEnum.Gpt4oMini20240718 -> encoder.encodeString("gpt-4o-mini-2024-07-18")
          CaseEnum.Gpt45Preview -> encoder.encodeString("gpt-4.5-preview")
          CaseEnum.Gpt45Preview20250227 -> encoder.encodeString("gpt-4.5-preview-2025-02-27")
          CaseEnum.Gpt4Turbo -> encoder.encodeString("gpt-4-turbo")
          CaseEnum.Gpt4Turbo20240409 -> encoder.encodeString("gpt-4-turbo-2024-04-09")
          CaseEnum.Gpt40125Preview -> encoder.encodeString("gpt-4-0125-preview")
          CaseEnum.Gpt4TurboPreview -> encoder.encodeString("gpt-4-turbo-preview")
          CaseEnum.Gpt41106Preview -> encoder.encodeString("gpt-4-1106-preview")
          CaseEnum.Gpt4VisionPreview -> encoder.encodeString("gpt-4-vision-preview")
          CaseEnum.Gpt4 -> encoder.encodeString("gpt-4")
          CaseEnum.Gpt40314 -> encoder.encodeString("gpt-4-0314")
          CaseEnum.Gpt40613 -> encoder.encodeString("gpt-4-0613")
          CaseEnum.Gpt432k -> encoder.encodeString("gpt-4-32k")
          CaseEnum.Gpt432k0314 -> encoder.encodeString("gpt-4-32k-0314")
          CaseEnum.Gpt432k0613 -> encoder.encodeString("gpt-4-32k-0613")
          CaseEnum.Gpt35Turbo -> encoder.encodeString("gpt-3.5-turbo")
          CaseEnum.Gpt35Turbo16k -> encoder.encodeString("gpt-3.5-turbo-16k")
          CaseEnum.Gpt35Turbo0613 -> encoder.encodeString("gpt-3.5-turbo-0613")
          CaseEnum.Gpt35Turbo1106 -> encoder.encodeString("gpt-3.5-turbo-1106")
          CaseEnum.Gpt35Turbo0125 -> encoder.encodeString("gpt-3.5-turbo-0125")
          CaseEnum.Gpt35Turbo16k0613 -> encoder.encodeString("gpt-3.5-turbo-16k-0613")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-5" -> CaseEnum.Gpt5
        "gpt-5-mini" -> CaseEnum.Gpt5Mini
        "gpt-5-nano" -> CaseEnum.Gpt5Nano
        "gpt-5-2025-08-07" -> CaseEnum.Gpt520250807
        "gpt-5-mini-2025-08-07" -> CaseEnum.Gpt5Mini20250807
        "gpt-5-nano-2025-08-07" -> CaseEnum.Gpt5Nano20250807
        "gpt-4.1" -> CaseEnum.Gpt41
        "gpt-4.1-mini" -> CaseEnum.Gpt41Mini
        "gpt-4.1-nano" -> CaseEnum.Gpt41Nano
        "gpt-4.1-2025-04-14" -> CaseEnum.Gpt4120250414
        "gpt-4.1-mini-2025-04-14" -> CaseEnum.Gpt41Mini20250414
        "gpt-4.1-nano-2025-04-14" -> CaseEnum.Gpt41Nano20250414
        "gpt-4o" -> CaseEnum.Gpt4o
        "gpt-4o-2024-11-20" -> CaseEnum.Gpt4o20241120
        "gpt-4o-2024-08-06" -> CaseEnum.Gpt4o20240806
        "gpt-4o-2024-05-13" -> CaseEnum.Gpt4o20240513
        "gpt-4o-mini" -> CaseEnum.Gpt4oMini
        "gpt-4o-mini-2024-07-18" -> CaseEnum.Gpt4oMini20240718
        "gpt-4.5-preview" -> CaseEnum.Gpt45Preview
        "gpt-4.5-preview-2025-02-27" -> CaseEnum.Gpt45Preview20250227
        "gpt-4-turbo" -> CaseEnum.Gpt4Turbo
        "gpt-4-turbo-2024-04-09" -> CaseEnum.Gpt4Turbo20240409
        "gpt-4-0125-preview" -> CaseEnum.Gpt40125Preview
        "gpt-4-turbo-preview" -> CaseEnum.Gpt4TurboPreview
        "gpt-4-1106-preview" -> CaseEnum.Gpt41106Preview
        "gpt-4-vision-preview" -> CaseEnum.Gpt4VisionPreview
        "gpt-4" -> CaseEnum.Gpt4
        "gpt-4-0314" -> CaseEnum.Gpt40314
        "gpt-4-0613" -> CaseEnum.Gpt40613
        "gpt-4-32k" -> CaseEnum.Gpt432k
        "gpt-4-32k-0314" -> CaseEnum.Gpt432k0314
        "gpt-4-32k-0613" -> CaseEnum.Gpt432k0613
        "gpt-3.5-turbo" -> CaseEnum.Gpt35Turbo
        "gpt-3.5-turbo-16k" -> CaseEnum.Gpt35Turbo16k
        "gpt-3.5-turbo-0613" -> CaseEnum.Gpt35Turbo0613
        "gpt-3.5-turbo-1106" -> CaseEnum.Gpt35Turbo1106
        "gpt-3.5-turbo-0125" -> CaseEnum.Gpt35Turbo0125
        "gpt-3.5-turbo-16k-0613" -> CaseEnum.Gpt35Turbo16k0613
        else -> CaseString(value)
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
          buildSerialDescriptor("io.openai.model.CreateThreadAndRunRequest.Tools", PolymorphicKind.SEALED) {
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
