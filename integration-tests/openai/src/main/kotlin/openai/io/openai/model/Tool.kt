package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A tool that can be used to generate a response.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface Tool {
  @Serializable
  @JvmInline
  @SerialName("FunctionTool")
  public value class FunctionTool(
    public val `value`: io.openai.model.FunctionTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("FileSearchTool")
  public value class FileSearchTool(
    public val `value`: io.openai.model.FileSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("ComputerTool")
  public value class ComputerTool(
    public val `value`: io.openai.model.ComputerTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("ComputerUsePreviewTool")
  public value class ComputerUsePreviewTool(
    public val `value`: io.openai.model.ComputerUsePreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("WebSearchTool")
  public value class WebSearchTool(
    public val `value`: io.openai.model.WebSearchTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("MCPTool")
  public value class MCPTool(
    public val `value`: io.openai.model.MCPTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("CodeInterpreterTool")
  public value class CodeInterpreterTool(
    public val `value`: io.openai.model.CodeInterpreterTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("ImageGenTool")
  public value class ImageGenTool(
    public val `value`: io.openai.model.ImageGenTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolParam")
  public value class LocalShellToolParam(
    public val `value`: io.openai.model.LocalShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("FunctionShellToolParam")
  public value class FunctionShellToolParam(
    public val `value`: io.openai.model.FunctionShellToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("CustomToolParam")
  public value class CustomToolParam(
    public val `value`: io.openai.model.CustomToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("NamespaceToolParam")
  public value class NamespaceToolParam(
    public val `value`: io.openai.model.NamespaceToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("ToolSearchToolParam")
  public value class ToolSearchToolParam(
    public val `value`: io.openai.model.ToolSearchToolParam,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("WebSearchPreviewTool")
  public value class WebSearchPreviewTool(
    public val `value`: io.openai.model.WebSearchPreviewTool,
  ) : Tool

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolParam")
  public value class ApplyPatchToolParam(
    public val `value`: io.openai.model.ApplyPatchToolParam,
  ) : Tool
}
