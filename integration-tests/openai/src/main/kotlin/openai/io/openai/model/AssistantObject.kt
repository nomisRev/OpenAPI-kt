package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
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
 * Represents an `assistant` that can call the model and use tools.
 */
@Serializable
public data class AssistantObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  public val name: String?,
  public val description: String?,
  public val model: String,
  public val instructions: String?,
  @Required
  public val tools: List<Tools> = emptyList(),
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  @SerialName("response_format")
  public val responseFormat: AssistantsApiResponseFormatOption? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("assistant")
    Assistant("assistant"),
    ;
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
          buildSerialDescriptor("io.openai.model.AssistantObject.Tools", PolymorphicKind.SEALED) {
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
