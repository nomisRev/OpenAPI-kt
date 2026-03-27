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
  public value class Function(
    public val `value`: FunctionTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class FileSearch(
    public val `value`: FileSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class Computer(
    public val `value`: ComputerTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class ComputerUsePreview(
    public val `value`: ComputerUsePreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseWebSearchTool(
    public val `value`: WebSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class Mcp(
    public val `value`: MCPTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CodeInterpreter(
    public val `value`: CodeInterpreterTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class ImageGeneration(
    public val `value`: ImageGenTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class LocalShell(
    public val `value`: LocalShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class Shell(
    public val `value`: FunctionShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class Custom(
    public val `value`: CustomToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class Namespace(
    public val `value`: NamespaceToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class ToolSearch(
    public val `value`: ToolSearchToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  public value class CaseWebSearchPreviewTool(
    public val `value`: WebSearchPreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  public value class ApplyPatch(
    public val `value`: ApplyPatchToolParam,
  ) : Tool

  public object Serializer : KSerializer<Tool> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.Tool", PolymorphicKind.SEALED) {
      element("Function", FunctionTool.serializer().descriptor)
      element("FileSearch", FileSearchTool.serializer().descriptor)
      element("Computer", ComputerTool.serializer().descriptor)
      element("ComputerUsePreview", ComputerUsePreviewTool.serializer().descriptor)
      element("CaseWebSearchTool", WebSearchTool.serializer().descriptor)
      element("Mcp", MCPTool.serializer().descriptor)
      element("CodeInterpreter", CodeInterpreterTool.serializer().descriptor)
      element("ImageGeneration", ImageGenTool.serializer().descriptor)
      element("LocalShell", LocalShellToolParam.serializer().descriptor)
      element("Shell", FunctionShellToolParam.serializer().descriptor)
      element("Custom", CustomToolParam.serializer().descriptor)
      element("Namespace", NamespaceToolParam.serializer().descriptor)
      element("ToolSearch", ToolSearchToolParam.serializer().descriptor)
      element("CaseWebSearchPreviewTool", WebSearchPreviewTool.serializer().descriptor)
      element("ApplyPatch", ApplyPatchToolParam.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Tool {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        Function::class to { Function(decodeFromJsonElement(FunctionTool.serializer(), it)) },
        FileSearch::class to { FileSearch(decodeFromJsonElement(FileSearchTool.serializer(), it)) },
        Computer::class to { Computer(decodeFromJsonElement(ComputerTool.serializer(), it)) },
        ComputerUsePreview::class to { ComputerUsePreview(decodeFromJsonElement(ComputerUsePreviewTool.serializer(), it)) },
        CaseWebSearchTool::class to { CaseWebSearchTool(decodeFromJsonElement(WebSearchTool.serializer(), it)) },
        Mcp::class to { Mcp(decodeFromJsonElement(MCPTool.serializer(), it)) },
        CodeInterpreter::class to { CodeInterpreter(decodeFromJsonElement(CodeInterpreterTool.serializer(), it)) },
        ImageGeneration::class to { ImageGeneration(decodeFromJsonElement(ImageGenTool.serializer(), it)) },
        LocalShell::class to { LocalShell(decodeFromJsonElement(LocalShellToolParam.serializer(), it)) },
        Shell::class to { Shell(decodeFromJsonElement(FunctionShellToolParam.serializer(), it)) },
        Custom::class to { Custom(decodeFromJsonElement(CustomToolParam.serializer(), it)) },
        Namespace::class to { Namespace(decodeFromJsonElement(NamespaceToolParam.serializer(), it)) },
        ToolSearch::class to { ToolSearch(decodeFromJsonElement(ToolSearchToolParam.serializer(), it)) },
        CaseWebSearchPreviewTool::class to { CaseWebSearchPreviewTool(decodeFromJsonElement(WebSearchPreviewTool.serializer(), it)) },
        ApplyPatch::class to { ApplyPatch(decodeFromJsonElement(ApplyPatchToolParam.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Tool) {
      when(value) {
        is Function -> encoder.encodeSerializableValue(FunctionTool.serializer(), value.value)
        is FileSearch -> encoder.encodeSerializableValue(FileSearchTool.serializer(), value.value)
        is Computer -> encoder.encodeSerializableValue(ComputerTool.serializer(), value.value)
        is ComputerUsePreview -> encoder.encodeSerializableValue(ComputerUsePreviewTool.serializer(), value.value)
        is CaseWebSearchTool -> encoder.encodeSerializableValue(WebSearchTool.serializer(), value.value)
        is Mcp -> encoder.encodeSerializableValue(MCPTool.serializer(), value.value)
        is CodeInterpreter -> encoder.encodeSerializableValue(CodeInterpreterTool.serializer(), value.value)
        is ImageGeneration -> encoder.encodeSerializableValue(ImageGenTool.serializer(), value.value)
        is LocalShell -> encoder.encodeSerializableValue(LocalShellToolParam.serializer(), value.value)
        is Shell -> encoder.encodeSerializableValue(FunctionShellToolParam.serializer(), value.value)
        is Custom -> encoder.encodeSerializableValue(CustomToolParam.serializer(), value.value)
        is Namespace -> encoder.encodeSerializableValue(NamespaceToolParam.serializer(), value.value)
        is ToolSearch -> encoder.encodeSerializableValue(ToolSearchToolParam.serializer(), value.value)
        is CaseWebSearchPreviewTool -> encoder.encodeSerializableValue(WebSearchPreviewTool.serializer(), value.value)
        is ApplyPatch -> encoder.encodeSerializableValue(ApplyPatchToolParam.serializer(), value.value)
      }
    }
  }
}
