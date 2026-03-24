package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface OutputItem {
  @Serializable
  @JvmInline
  @SerialName("OutputMessage")
  public value class OutputMessage(
    public val `value`: io.openai.model.OutputMessage,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("FileSearchToolCall")
  public value class FileSearchToolCall(
    public val `value`: io.openai.model.FileSearchToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCall")
  public value class FunctionToolCall(
    public val `value`: io.openai.model.FunctionToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("WebSearchToolCall")
  public value class WebSearchToolCall(
    public val `value`: io.openai.model.WebSearchToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCall")
  public value class ComputerToolCall(
    public val `value`: io.openai.model.ComputerToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ReasoningItem")
  public value class ReasoningItem(
    public val `value`: io.openai.model.ReasoningItem,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ToolSearchCall")
  public value class ToolSearchCall(
    public val `value`: io.openai.model.ToolSearchCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ToolSearchOutput")
  public value class ToolSearchOutput(
    public val `value`: io.openai.model.ToolSearchOutput,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("CompactionBody")
  public value class CompactionBody(
    public val `value`: io.openai.model.CompactionBody,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ImageGenToolCall")
  public value class ImageGenToolCall(
    public val `value`: io.openai.model.ImageGenToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("CodeInterpreterToolCall")
  public value class CodeInterpreterToolCall(
    public val `value`: io.openai.model.CodeInterpreterToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCall")
  public value class LocalShellToolCall(
    public val `value`: io.openai.model.LocalShellToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCall")
  public value class FunctionShellCall(
    public val `value`: io.openai.model.FunctionShellCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCallOutput")
  public value class FunctionShellCallOutput(
    public val `value`: io.openai.model.FunctionShellCallOutput,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCall")
  public value class ApplyPatchToolCall(
    public val `value`: io.openai.model.ApplyPatchToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCallOutput")
  public value class ApplyPatchToolCallOutput(
    public val `value`: io.openai.model.ApplyPatchToolCallOutput,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("MCPToolCall")
  public value class MCPToolCall(
    public val `value`: io.openai.model.MCPToolCall,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("MCPListTools")
  public value class MCPListTools(
    public val `value`: io.openai.model.MCPListTools,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalRequest")
  public value class MCPApprovalRequest(
    public val `value`: io.openai.model.MCPApprovalRequest,
  ) : OutputItem

  @Serializable
  @JvmInline
  @SerialName("CustomToolCall")
  public value class CustomToolCall(
    public val `value`: io.openai.model.CustomToolCall,
  ) : OutputItem
}
