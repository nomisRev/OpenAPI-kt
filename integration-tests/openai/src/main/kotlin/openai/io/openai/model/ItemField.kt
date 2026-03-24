package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * An item representing a message, tool call, tool output, reasoning, or other response element.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ItemField {
  @Serializable
  @JvmInline
  @SerialName("Message")
  public value class Message(
    public val `value`: io.openai.model.Message,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCall")
  public value class FunctionToolCall(
    public val `value`: io.openai.model.FunctionToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ToolSearchCall")
  public value class ToolSearchCall(
    public val `value`: io.openai.model.ToolSearchCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ToolSearchOutput")
  public value class ToolSearchOutput(
    public val `value`: io.openai.model.ToolSearchOutput,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCallOutput")
  public value class FunctionToolCallOutput(
    public val `value`: io.openai.model.FunctionToolCallOutput,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("FileSearchToolCall")
  public value class FileSearchToolCall(
    public val `value`: io.openai.model.FileSearchToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("WebSearchToolCall")
  public value class WebSearchToolCall(
    public val `value`: io.openai.model.WebSearchToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ImageGenToolCall")
  public value class ImageGenToolCall(
    public val `value`: io.openai.model.ImageGenToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCall")
  public value class ComputerToolCall(
    public val `value`: io.openai.model.ComputerToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCallOutputResource")
  public value class ComputerToolCallOutputResource(
    public val `value`: io.openai.model.ComputerToolCallOutputResource,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ReasoningItem")
  public value class ReasoningItem(
    public val `value`: io.openai.model.ReasoningItem,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("CompactionBody")
  public value class CompactionBody(
    public val `value`: io.openai.model.CompactionBody,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("CodeInterpreterToolCall")
  public value class CodeInterpreterToolCall(
    public val `value`: io.openai.model.CodeInterpreterToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCall")
  public value class LocalShellToolCall(
    public val `value`: io.openai.model.LocalShellToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCallOutput")
  public value class LocalShellToolCallOutput(
    public val `value`: io.openai.model.LocalShellToolCallOutput,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCall")
  public value class FunctionShellCall(
    public val `value`: io.openai.model.FunctionShellCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCallOutput")
  public value class FunctionShellCallOutput(
    public val `value`: io.openai.model.FunctionShellCallOutput,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCall")
  public value class ApplyPatchToolCall(
    public val `value`: io.openai.model.ApplyPatchToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCallOutput")
  public value class ApplyPatchToolCallOutput(
    public val `value`: io.openai.model.ApplyPatchToolCallOutput,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("MCPListTools")
  public value class MCPListTools(
    public val `value`: io.openai.model.MCPListTools,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalRequest")
  public value class MCPApprovalRequest(
    public val `value`: io.openai.model.MCPApprovalRequest,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalResponseResource")
  public value class MCPApprovalResponseResource(
    public val `value`: io.openai.model.MCPApprovalResponseResource,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("MCPToolCall")
  public value class MCPToolCall(
    public val `value`: io.openai.model.MCPToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("CustomToolCall")
  public value class CustomToolCall(
    public val `value`: io.openai.model.CustomToolCall,
  ) : ItemField

  @Serializable
  @JvmInline
  @SerialName("CustomToolCallOutput")
  public value class CustomToolCallOutput(
    public val `value`: io.openai.model.CustomToolCallOutput,
  ) : ItemField
}
