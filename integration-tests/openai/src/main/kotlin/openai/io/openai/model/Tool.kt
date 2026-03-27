package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * A tool that can be used to generate a response.
 *
 */
@Serializable(with = Tool.Serializer::class)
public sealed interface Tool {
  @Serializable
  @JvmInline
  public value class CaseFunctionTool(
    public val `value`: FunctionTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseFileSearchTool(
    public val `value`: FileSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseComputerTool(
    public val `value`: ComputerTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseComputerUsePreviewTool(
    public val `value`: ComputerUsePreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseWebSearchTool(
    public val `value`: WebSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseMCPTool(
    public val `value`: MCPTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseCodeInterpreterTool(
    public val `value`: CodeInterpreterTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseImageGenTool(
    public val `value`: ImageGenTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseLocalShellToolParam(
    public val `value`: LocalShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseFunctionShellToolParam(
    public val `value`: FunctionShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseCustomToolParam(
    public val `value`: CustomToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseNamespaceToolParam(
    public val `value`: NamespaceToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseToolSearchToolParam(
    public val `value`: ToolSearchToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseWebSearchPreviewTool(
    public val `value`: WebSearchPreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseApplyPatchToolParam(
    public val `value`: ApplyPatchToolParam,
  ) : Tool

  public object Serializer : KSerializer<Tool> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.Tool", PolymorphicKind.SEALED) {
      element("CaseFunctionTool", FunctionTool.serializer().descriptor)
      element("CaseFileSearchTool", FileSearchTool.serializer().descriptor)
      element("CaseComputerTool", ComputerTool.serializer().descriptor)
      element("CaseComputerUsePreviewTool", ComputerUsePreviewTool.serializer().descriptor)
      element("CaseWebSearchTool", WebSearchTool.serializer().descriptor)
      element("CaseMCPTool", MCPTool.serializer().descriptor)
      element("CaseCodeInterpreterTool", CodeInterpreterTool.serializer().descriptor)
      element("CaseImageGenTool", ImageGenTool.serializer().descriptor)
      element("CaseLocalShellToolParam", LocalShellToolParam.serializer().descriptor)
      element("CaseFunctionShellToolParam", FunctionShellToolParam.serializer().descriptor)
      element("CaseCustomToolParam", CustomToolParam.serializer().descriptor)
      element("CaseNamespaceToolParam", NamespaceToolParam.serializer().descriptor)
      element("CaseToolSearchToolParam", ToolSearchToolParam.serializer().descriptor)
      element("CaseWebSearchPreviewTool", WebSearchPreviewTool.serializer().descriptor)
      element("CaseApplyPatchToolParam", ApplyPatchToolParam.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Tool {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseFunctionTool::class to { CaseFunctionTool(decodeFromJsonElement(FunctionTool.serializer(), it)) },
        CaseFileSearchTool::class to { CaseFileSearchTool(decodeFromJsonElement(FileSearchTool.serializer(), it)) },
        CaseComputerTool::class to { CaseComputerTool(decodeFromJsonElement(ComputerTool.serializer(), it)) },
        CaseComputerUsePreviewTool::class to { CaseComputerUsePreviewTool(decodeFromJsonElement(ComputerUsePreviewTool.serializer(), it)) },
        CaseWebSearchTool::class to { CaseWebSearchTool(decodeFromJsonElement(WebSearchTool.serializer(), it)) },
        CaseMCPTool::class to { CaseMCPTool(decodeFromJsonElement(MCPTool.serializer(), it)) },
        CaseCodeInterpreterTool::class to { CaseCodeInterpreterTool(decodeFromJsonElement(CodeInterpreterTool.serializer(), it)) },
        CaseImageGenTool::class to { CaseImageGenTool(decodeFromJsonElement(ImageGenTool.serializer(), it)) },
        CaseLocalShellToolParam::class to { CaseLocalShellToolParam(decodeFromJsonElement(LocalShellToolParam.serializer(), it)) },
        CaseFunctionShellToolParam::class to { CaseFunctionShellToolParam(decodeFromJsonElement(FunctionShellToolParam.serializer(), it)) },
        CaseCustomToolParam::class to { CaseCustomToolParam(decodeFromJsonElement(CustomToolParam.serializer(), it)) },
        CaseNamespaceToolParam::class to { CaseNamespaceToolParam(decodeFromJsonElement(NamespaceToolParam.serializer(), it)) },
        CaseToolSearchToolParam::class to { CaseToolSearchToolParam(decodeFromJsonElement(ToolSearchToolParam.serializer(), it)) },
        CaseWebSearchPreviewTool::class to { CaseWebSearchPreviewTool(decodeFromJsonElement(WebSearchPreviewTool.serializer(), it)) },
        CaseApplyPatchToolParam::class to { CaseApplyPatchToolParam(decodeFromJsonElement(ApplyPatchToolParam.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Tool) {
      when(value) {
        is CaseFunctionTool -> encoder.encodeSerializableValue(FunctionTool.serializer(), value.value)
        is CaseFileSearchTool -> encoder.encodeSerializableValue(FileSearchTool.serializer(), value.value)
        is CaseComputerTool -> encoder.encodeSerializableValue(ComputerTool.serializer(), value.value)
        is CaseComputerUsePreviewTool -> encoder.encodeSerializableValue(ComputerUsePreviewTool.serializer(), value.value)
        is CaseWebSearchTool -> encoder.encodeSerializableValue(WebSearchTool.serializer(), value.value)
        is CaseMCPTool -> encoder.encodeSerializableValue(MCPTool.serializer(), value.value)
        is CaseCodeInterpreterTool -> encoder.encodeSerializableValue(CodeInterpreterTool.serializer(), value.value)
        is CaseImageGenTool -> encoder.encodeSerializableValue(ImageGenTool.serializer(), value.value)
        is CaseLocalShellToolParam -> encoder.encodeSerializableValue(LocalShellToolParam.serializer(), value.value)
        is CaseFunctionShellToolParam -> encoder.encodeSerializableValue(FunctionShellToolParam.serializer(), value.value)
        is CaseCustomToolParam -> encoder.encodeSerializableValue(CustomToolParam.serializer(), value.value)
        is CaseNamespaceToolParam -> encoder.encodeSerializableValue(NamespaceToolParam.serializer(), value.value)
        is CaseToolSearchToolParam -> encoder.encodeSerializableValue(ToolSearchToolParam.serializer(), value.value)
        is CaseWebSearchPreviewTool -> encoder.encodeSerializableValue(WebSearchPreviewTool.serializer(), value.value)
        is CaseApplyPatchToolParam -> encoder.encodeSerializableValue(ApplyPatchToolParam.serializer(), value.value)
      }
    }
  }
}
