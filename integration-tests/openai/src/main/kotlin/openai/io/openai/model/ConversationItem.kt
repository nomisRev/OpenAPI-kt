package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A single item within a conversation. The set of possible types are the same as the `output` type of a [Response object](/docs/api-reference/responses/object#responses/object-output).
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ConversationItem {
  @Serializable
  @JvmInline
  @SerialName("Message")
  public value class Message(
    public val `value`: io.openai.model.Message,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCallResource")
  public value class FunctionToolCallResource(
    public val `value`: io.openai.model.FunctionToolCallResource,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCallOutputResource")
  public value class FunctionToolCallOutputResource(
    public val `value`: io.openai.model.FunctionToolCallOutputResource,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("FileSearchToolCall")
  public value class FileSearchToolCall(
    public val `value`: io.openai.model.FileSearchToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("WebSearchToolCall")
  public value class WebSearchToolCall(
    public val `value`: io.openai.model.WebSearchToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ImageGenToolCall")
  public value class ImageGenToolCall(
    public val `value`: io.openai.model.ImageGenToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCall")
  public value class ComputerToolCall(
    public val `value`: io.openai.model.ComputerToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCallOutputResource")
  public value class ComputerToolCallOutputResource(
    public val `value`: io.openai.model.ComputerToolCallOutputResource,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ToolSearchCall")
  public value class ToolSearchCall(
    public val `value`: io.openai.model.ToolSearchCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ToolSearchOutput")
  public value class ToolSearchOutput(
    public val `value`: io.openai.model.ToolSearchOutput,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ReasoningItem")
  public value class ReasoningItem(
    public val `value`: io.openai.model.ReasoningItem,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("CodeInterpreterToolCall")
  public value class CodeInterpreterToolCall(
    public val `value`: io.openai.model.CodeInterpreterToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCall")
  public value class LocalShellToolCall(
    public val `value`: io.openai.model.LocalShellToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCallOutput")
  public value class LocalShellToolCallOutput(
    public val `value`: io.openai.model.LocalShellToolCallOutput,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCall")
  public value class FunctionShellCall(
    public val `value`: io.openai.model.FunctionShellCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCallOutput")
  public value class FunctionShellCallOutput(
    public val `value`: io.openai.model.FunctionShellCallOutput,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCall")
  public value class ApplyPatchToolCall(
    public val `value`: io.openai.model.ApplyPatchToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCallOutput")
  public value class ApplyPatchToolCallOutput(
    public val `value`: io.openai.model.ApplyPatchToolCallOutput,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("MCPListTools")
  public value class MCPListTools(
    public val `value`: io.openai.model.MCPListTools,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalRequest")
  public value class MCPApprovalRequest(
    public val `value`: io.openai.model.MCPApprovalRequest,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalResponseResource")
  public value class MCPApprovalResponseResource(
    public val `value`: io.openai.model.MCPApprovalResponseResource,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("MCPToolCall")
  public value class MCPToolCall(
    public val `value`: io.openai.model.MCPToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("CustomToolCall")
  public value class CustomToolCall(
    public val `value`: io.openai.model.CustomToolCall,
  ) : ConversationItem

  @Serializable
  @JvmInline
  @SerialName("CustomToolCallOutput")
  public value class CustomToolCallOutput(
    public val `value`: io.openai.model.CustomToolCallOutput,
  ) : ConversationItem
}
