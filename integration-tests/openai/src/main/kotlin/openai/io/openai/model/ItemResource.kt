package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Content item used to generate a response.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ItemResource {
  @Serializable
  @JvmInline
  @SerialName("InputMessageResource")
  public value class InputMessageResource(
    public val `value`: io.openai.model.InputMessageResource,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("OutputMessage")
  public value class OutputMessage(
    public val `value`: io.openai.model.OutputMessage,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("FileSearchToolCall")
  public value class FileSearchToolCall(
    public val `value`: io.openai.model.FileSearchToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCall")
  public value class ComputerToolCall(
    public val `value`: io.openai.model.ComputerToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ComputerToolCallOutputResource")
  public value class ComputerToolCallOutputResource(
    public val `value`: io.openai.model.ComputerToolCallOutputResource,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("WebSearchToolCall")
  public value class WebSearchToolCall(
    public val `value`: io.openai.model.WebSearchToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCallResource")
  public value class FunctionToolCallResource(
    public val `value`: io.openai.model.FunctionToolCallResource,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("FunctionToolCallOutputResource")
  public value class FunctionToolCallOutputResource(
    public val `value`: io.openai.model.FunctionToolCallOutputResource,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ToolSearchCall")
  public value class ToolSearchCall(
    public val `value`: io.openai.model.ToolSearchCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ToolSearchOutput")
  public value class ToolSearchOutput(
    public val `value`: io.openai.model.ToolSearchOutput,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ImageGenToolCall")
  public value class ImageGenToolCall(
    public val `value`: io.openai.model.ImageGenToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("CodeInterpreterToolCall")
  public value class CodeInterpreterToolCall(
    public val `value`: io.openai.model.CodeInterpreterToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCall")
  public value class LocalShellToolCall(
    public val `value`: io.openai.model.LocalShellToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("LocalShellToolCallOutput")
  public value class LocalShellToolCallOutput(
    public val `value`: io.openai.model.LocalShellToolCallOutput,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCall")
  public value class FunctionShellCall(
    public val `value`: io.openai.model.FunctionShellCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("FunctionShellCallOutput")
  public value class FunctionShellCallOutput(
    public val `value`: io.openai.model.FunctionShellCallOutput,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCall")
  public value class ApplyPatchToolCall(
    public val `value`: io.openai.model.ApplyPatchToolCall,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("ApplyPatchToolCallOutput")
  public value class ApplyPatchToolCallOutput(
    public val `value`: io.openai.model.ApplyPatchToolCallOutput,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("MCPListTools")
  public value class MCPListTools(
    public val `value`: io.openai.model.MCPListTools,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalRequest")
  public value class MCPApprovalRequest(
    public val `value`: io.openai.model.MCPApprovalRequest,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("MCPApprovalResponseResource")
  public value class MCPApprovalResponseResource(
    public val `value`: io.openai.model.MCPApprovalResponseResource,
  ) : ItemResource

  @Serializable
  @JvmInline
  @SerialName("MCPToolCall")
  public value class MCPToolCall(
    public val `value`: io.openai.model.MCPToolCall,
  ) : ItemResource
}
