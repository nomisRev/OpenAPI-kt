package io.openai.model

import kotlin.Boolean
import kotlin.Float
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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Content item used to generate a response.
 *
 */
@Serializable(with = ItemResource.Serializer::class)
public sealed interface ItemResource {
  /**
   * A message input to the model with a role indicating instruction following
   * hierarchy. Instructions given with the `developer` or `system` role take
   * precedence over instructions given with the `user` role.
   *
   */
  @Serializable
  public data class InputMessageResource(
    public val type: Type? = null,
    public val role: Role,
    public val status: Status? = null,
    public val content: InputMessageContentList,
    public val id: String,
  ) : ItemResource {
    @Serializable
    public enum class Role(
      public val `value`: String,
    ) {
      @SerialName("user")
      User("user"),
      @SerialName("system")
      System("system"),
      @SerialName("developer")
      Developer("developer"),
      ;
    }

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("message")
      Message("message"),
      ;
    }
  }

  /**
   * An output message from the model.
   *
   */
  @Serializable
  public data class OutputMessage(
    public val id: String,
    public val type: Type,
    public val role: Role,
    public val content: List<OutputMessageContent>,
    public val phase: MessagePhase? = null,
    public val status: Status,
  ) : ItemResource {
    @Serializable
    public enum class Role(
      public val `value`: String,
    ) {
      @SerialName("assistant")
      Assistant("assistant"),
      ;
    }

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("message")
      Message("message"),
      ;
    }
  }

  /**
   * The results of a file search tool call. See the
   * [file search guide](/docs/guides/tools-file-search) for more information.
   *
   */
  @Serializable
  public data class FileSearchCall(
    public val id: String,
    public val type: Type,
    public val status: Status,
    public val queries: List<String>,
    public val results: List<Results>? = null,
  ) : ItemResource {
    @Serializable
    public data class Results(
      @SerialName("file_id")
      public val fileId: String? = null,
      public val text: String? = null,
      public val filename: String? = null,
      public val attributes: VectorStoreFileAttributes? = null,
      public val score: Float? = null,
    )

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("searching")
      Searching("searching"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      @SerialName("failed")
      Failed("failed"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("file_search_call")
      FileSearchCall("file_search_call"),
      ;
    }
  }

  /**
   * A tool call to a computer use tool. See the
   * [computer use guide](/docs/guides/tools-computer-use) for more information.
   *
   */
  @Serializable
  public data class ComputerCall(
    @Required
    public val type: Type = Type.ComputerCall,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val action: ComputerAction? = null,
    public val actions: ComputerActionList? = null,
    @SerialName("pending_safety_checks")
    public val pendingSafetyChecks: List<ComputerCallSafetyCheckParam>,
    public val status: Status,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("computer_call")
      ComputerCall("computer_call"),
      ;
    }
  }

  /**
   * The output of a computer tool call.
   *
   */
  @Serializable
  public data class ComputerCallOutput(
    @Required
    public val type: Type = Type.ComputerCallOutput,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    @SerialName("acknowledged_safety_checks")
    public val acknowledgedSafetyChecks: List<ComputerCallSafetyCheckParam>? = null,
    public val output: ComputerScreenshotImage,
    public val status: Status? = null,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("computer_call_output")
      ComputerCallOutput("computer_call_output"),
      ;
    }
  }

  /**
   * The results of a web search tool call. See the
   * [web search guide](/docs/guides/tools-web-search) for more information.
   *
   */
  @Serializable
  public data class WebSearchCall(
    public val id: String,
    public val type: Type,
    public val status: Status,
    public val action: JsonElement,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("searching")
      Searching("searching"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("failed")
      Failed("failed"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("web_search_call")
      WebSearchCall("web_search_call"),
      ;
    }
  }

  /**
   * A tool call to run a function. See the 
   * [function calling guide](/docs/guides/function-calling) for more information.
   *
   */
  @Serializable
  public data class FunctionCall(
    public val id: String,
    public val type: Type,
    @SerialName("call_id")
    public val callId: String,
    public val namespace: String? = null,
    public val name: String,
    public val arguments: String,
    public val status: Status? = null,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("function_call")
      FunctionCall("function_call"),
      ;
    }
  }

  /**
   * The output of a function tool call.
   *
   */
  @Serializable
  public data class FunctionCallOutput(
    public val id: String,
    public val type: Type,
    @SerialName("call_id")
    public val callId: String,
    public val output: Output,
    public val status: Status? = null,
  ) : ItemResource {
    /**
     * The output from the function call generated by your code.
     * Can be a string or an list of output content.
     *
     */
    @Serializable(with = Output.Serializer::class)
    public sealed interface Output {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Output

      @Serializable
      @JvmInline
      public value class CaseFunctionAndCustomToolCallOutputList(
        public val `value`: List<FunctionAndCustomToolCallOutput>,
      ) : Output

      public object Serializer : KSerializer<Output> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.ItemResource.FunctionCallOutput.Output", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseFunctionAndCustomToolCallOutputList", ListSerializer(FunctionAndCustomToolCallOutput.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Output {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseFunctionAndCustomToolCallOutputList::class to { CaseFunctionAndCustomToolCallOutputList(decodeFromJsonElement(ListSerializer(FunctionAndCustomToolCallOutput.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Output) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseFunctionAndCustomToolCallOutputList -> encoder.encodeSerializableValue(ListSerializer(FunctionAndCustomToolCallOutput.serializer()), value.value)
          }
        }
      }
    }

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("function_call_output")
      FunctionCallOutput("function_call_output"),
      ;
    }
  }

  @Serializable
  public data class ToolSearchCall(
    @Required
    public val type: Type = Type.ToolSearchCall,
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val arguments: JsonElement,
    public val status: FunctionCallStatus,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("tool_search_call")
      ToolSearchCall("tool_search_call"),
      ;
    }
  }

  @Serializable
  public data class ToolSearchOutput(
    @Required
    public val type: Type = Type.ToolSearchOutput,
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val tools: List<Tool>,
    public val status: FunctionCallOutputStatusEnum,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("tool_search_output")
      ToolSearchOutput("tool_search_output"),
      ;
    }
  }

  /**
   * An image generation request made by the model.
   *
   */
  @Serializable
  public data class ImageGenerationCall(
    public val type: Type,
    public val id: String,
    public val status: Status,
    public val result: String?,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("generating")
      Generating("generating"),
      @SerialName("failed")
      Failed("failed"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("image_generation_call")
      ImageGenerationCall("image_generation_call"),
      ;
    }
  }

  /**
   * A tool call to run code.
   *
   */
  @Serializable
  public data class CodeInterpreterCall(
    @Required
    public val type: Type = Type.CodeInterpreterCall,
    public val id: String,
    public val status: Status,
    @SerialName("container_id")
    public val containerId: String,
    public val code: String?,
    public val outputs: List<Outputs>?,
  ) : ItemResource {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Outputs {
      /**
       * The logs output from the code interpreter.
       */
      @JvmInline
      @SerialName("logs")
      @Serializable
      public value class Logs(
        public val logs: String,
      ) : Outputs

      /**
       * The image output from the code interpreter.
       */
      @JvmInline
      @SerialName("image")
      @Serializable
      public value class Image(
        public val url: String,
      ) : Outputs
    }

    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      @SerialName("interpreting")
      Interpreting("interpreting"),
      @SerialName("failed")
      Failed("failed"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("code_interpreter_call")
      CodeInterpreterCall("code_interpreter_call"),
      ;
    }
  }

  /**
   * A tool call to run a command on the local shell.
   *
   */
  @Serializable
  public data class LocalShellCall(
    public val type: Type,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val action: LocalShellExecAction,
    public val status: Status,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("local_shell_call")
      LocalShellCall("local_shell_call"),
      ;
    }
  }

  /**
   * The output of a local shell tool call.
   *
   */
  @Serializable
  public data class LocalShellCallOutput(
    public val type: Type,
    public val id: String,
    public val output: String,
    public val status: Status? = null,
  ) : ItemResource {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("in_progress")
      InProgress("in_progress"),
      @SerialName("completed")
      Completed("completed"),
      @SerialName("incomplete")
      Incomplete("incomplete"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("local_shell_call_output")
      LocalShellCallOutput("local_shell_call_output"),
      ;
    }
  }

  /**
   * A tool call that executes one or more shell commands in a managed environment.
   */
  @Serializable
  public data class ShellCall(
    @Required
    public val type: Type = Type.ShellCall,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val action: FunctionShellAction,
    public val status: LocalShellCallStatus,
    public val environment: Environment?,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Environment {
      @Serializable
      @SerialName("local")
      public data object Local : Environment

      /**
       * Represents a container created with /v1/containers.
       */
      @JvmInline
      @SerialName("container_reference")
      @Serializable
      public value class ContainerReference(
        @SerialName("container_id")
        public val containerId: String,
      ) : Environment
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("shell_call")
      ShellCall("shell_call"),
      ;
    }
  }

  /**
   * The output of a shell tool call that was emitted.
   */
  @Serializable
  public data class ShellCallOutput(
    @Required
    public val type: Type = Type.ShellCallOutput,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: LocalShellCallOutputStatusEnum,
    public val output: List<FunctionShellCallOutputContent>,
    @SerialName("max_output_length")
    public val maxOutputLength: Long?,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("shell_call_output")
      ShellCallOutput("shell_call_output"),
      ;
    }
  }

  /**
   * A tool call that applies file diffs by creating, deleting, or updating files.
   */
  @Serializable
  public data class ApplyPatchCall(
    @Required
    public val type: Type = Type.ApplyPatchCall,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: ApplyPatchCallStatus,
    public val operation: Operation,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    /**
     * One of the create_file, delete_file, or update_file operations applied via apply_patch.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Operation {
      /**
       * Instruction describing how to create a file via the apply_patch tool.
       */
      @SerialName("create_file")
      @Serializable
      public data class CreateFile(
        public val path: String,
        public val diff: String,
      ) : Operation

      /**
       * Instruction describing how to delete a file via the apply_patch tool.
       */
      @JvmInline
      @SerialName("delete_file")
      @Serializable
      public value class DeleteFile(
        public val path: String,
      ) : Operation

      /**
       * Instruction describing how to update a file via the apply_patch tool.
       */
      @SerialName("update_file")
      @Serializable
      public data class UpdateFile(
        public val path: String,
        public val diff: String,
      ) : Operation
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("apply_patch_call")
      ApplyPatchCall("apply_patch_call"),
      ;
    }
  }

  /**
   * The output emitted by an apply patch tool call.
   */
  @Serializable
  public data class ApplyPatchCallOutput(
    @Required
    public val type: Type = Type.ApplyPatchCallOutput,
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: ApplyPatchCallOutputStatus,
    public val output: String? = null,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("apply_patch_call_output")
      ApplyPatchCallOutput("apply_patch_call_output"),
      ;
    }
  }

  /**
   * A list of tools available on an MCP server.
   *
   */
  @Serializable
  public data class McpListTools(
    public val type: Type,
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val tools: List<MCPListToolsTool>,
    public val error: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("mcp_list_tools")
      McpListTools("mcp_list_tools"),
      ;
    }
  }

  /**
   * A request for human approval of a tool invocation.
   *
   */
  @Serializable
  public data class McpApprovalRequest(
    public val type: Type,
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val name: String,
    public val arguments: String,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("mcp_approval_request")
      McpApprovalRequest("mcp_approval_request"),
      ;
    }
  }

  /**
   * A response to an MCP approval request.
   *
   */
  @Serializable
  public data class McpApprovalResponse(
    public val type: Type,
    public val id: String,
    @SerialName("approval_request_id")
    public val approvalRequestId: String,
    public val approve: Boolean,
    public val reason: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("mcp_approval_response")
      McpApprovalResponse("mcp_approval_response"),
      ;
    }
  }

  /**
   * An invocation of a tool on an MCP server.
   *
   */
  @Serializable
  public data class McpCall(
    public val type: Type,
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val name: String,
    public val arguments: String,
    public val output: String? = null,
    public val error: String? = null,
    public val status: MCPToolCallStatus? = null,
    @SerialName("approval_request_id")
    public val approvalRequestId: String? = null,
  ) : ItemResource {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("mcp_call")
      McpCall("mcp_call"),
      ;
    }
  }

  public object Serializer : KSerializer<ItemResource> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ItemResource", PolymorphicKind.SEALED) {
      element("InputMessageResource", InputMessageResource.serializer().descriptor)
      element("OutputMessage", OutputMessage.serializer().descriptor)
      element("FileSearchCall", FileSearchCall.serializer().descriptor)
      element("ComputerCall", ComputerCall.serializer().descriptor)
      element("ComputerCallOutput", ComputerCallOutput.serializer().descriptor)
      element("WebSearchCall", WebSearchCall.serializer().descriptor)
      element("FunctionCall", FunctionCall.serializer().descriptor)
      element("FunctionCallOutput", FunctionCallOutput.serializer().descriptor)
      element("ToolSearchCall", ToolSearchCall.serializer().descriptor)
      element("ToolSearchOutput", ToolSearchOutput.serializer().descriptor)
      element("ImageGenerationCall", ImageGenerationCall.serializer().descriptor)
      element("CodeInterpreterCall", CodeInterpreterCall.serializer().descriptor)
      element("LocalShellCall", LocalShellCall.serializer().descriptor)
      element("LocalShellCallOutput", LocalShellCallOutput.serializer().descriptor)
      element("ShellCall", ShellCall.serializer().descriptor)
      element("ShellCallOutput", ShellCallOutput.serializer().descriptor)
      element("ApplyPatchCall", ApplyPatchCall.serializer().descriptor)
      element("ApplyPatchCallOutput", ApplyPatchCallOutput.serializer().descriptor)
      element("McpListTools", McpListTools.serializer().descriptor)
      element("McpApprovalRequest", McpApprovalRequest.serializer().descriptor)
      element("McpApprovalResponse", McpApprovalResponse.serializer().descriptor)
      element("McpCall", McpCall.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ItemResource {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "file_search_call" -> return json.decodeFromJsonElement(FileSearchCall.serializer(), value)
        "computer_call" -> return json.decodeFromJsonElement(ComputerCall.serializer(), value)
        "computer_call_output" -> return json.decodeFromJsonElement(ComputerCallOutput.serializer(), value)
        "web_search_call" -> return json.decodeFromJsonElement(WebSearchCall.serializer(), value)
        "function_call" -> return json.decodeFromJsonElement(FunctionCall.serializer(), value)
        "function_call_output" -> return json.decodeFromJsonElement(FunctionCallOutput.serializer(), value)
        "tool_search_call" -> return json.decodeFromJsonElement(ToolSearchCall.serializer(), value)
        "tool_search_output" -> return json.decodeFromJsonElement(ToolSearchOutput.serializer(), value)
        "image_generation_call" -> return json.decodeFromJsonElement(ImageGenerationCall.serializer(), value)
        "code_interpreter_call" -> return json.decodeFromJsonElement(CodeInterpreterCall.serializer(), value)
        "local_shell_call" -> return json.decodeFromJsonElement(LocalShellCall.serializer(), value)
        "local_shell_call_output" -> return json.decodeFromJsonElement(LocalShellCallOutput.serializer(), value)
        "shell_call" -> return json.decodeFromJsonElement(ShellCall.serializer(), value)
        "shell_call_output" -> return json.decodeFromJsonElement(ShellCallOutput.serializer(), value)
        "apply_patch_call" -> return json.decodeFromJsonElement(ApplyPatchCall.serializer(), value)
        "apply_patch_call_output" -> return json.decodeFromJsonElement(ApplyPatchCallOutput.serializer(), value)
        "mcp_list_tools" -> return json.decodeFromJsonElement(McpListTools.serializer(), value)
        "mcp_approval_request" -> return json.decodeFromJsonElement(McpApprovalRequest.serializer(), value)
        "mcp_approval_response" -> return json.decodeFromJsonElement(McpApprovalResponse.serializer(), value)
        "mcp_call" -> return json.decodeFromJsonElement(McpCall.serializer(), value)
        "message" -> {
          val keys = obj?.keys.orEmpty()
          if ("phase" in keys) {
            return json.decodeFromJsonElement(OutputMessage.serializer(), value)
          }
          return json.attemptDeserialize(
            value,
            OutputMessage::class to { decodeFromJsonElement(OutputMessage.serializer(), it) },
            InputMessageResource::class to { decodeFromJsonElement(InputMessageResource.serializer(), it) },
          )
        }
        else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.ItemResource")
      }
    }

    override fun serialize(encoder: Encoder, `value`: ItemResource) {
      when(value) {
        is InputMessageResource -> encoder.encodeSerializableValue(InputMessageResource.serializer(), value)
        is OutputMessage -> encoder.encodeSerializableValue(OutputMessage.serializer(), value)
        is FileSearchCall -> encoder.encodeSerializableValue(FileSearchCall.serializer(), value)
        is ComputerCall -> encoder.encodeSerializableValue(ComputerCall.serializer(), value)
        is ComputerCallOutput -> encoder.encodeSerializableValue(ComputerCallOutput.serializer(), value)
        is WebSearchCall -> encoder.encodeSerializableValue(WebSearchCall.serializer(), value)
        is FunctionCall -> encoder.encodeSerializableValue(FunctionCall.serializer(), value)
        is FunctionCallOutput -> encoder.encodeSerializableValue(FunctionCallOutput.serializer(), value)
        is ToolSearchCall -> encoder.encodeSerializableValue(ToolSearchCall.serializer(), value)
        is ToolSearchOutput -> encoder.encodeSerializableValue(ToolSearchOutput.serializer(), value)
        is ImageGenerationCall -> encoder.encodeSerializableValue(ImageGenerationCall.serializer(), value)
        is CodeInterpreterCall -> encoder.encodeSerializableValue(CodeInterpreterCall.serializer(), value)
        is LocalShellCall -> encoder.encodeSerializableValue(LocalShellCall.serializer(), value)
        is LocalShellCallOutput -> encoder.encodeSerializableValue(LocalShellCallOutput.serializer(), value)
        is ShellCall -> encoder.encodeSerializableValue(ShellCall.serializer(), value)
        is ShellCallOutput -> encoder.encodeSerializableValue(ShellCallOutput.serializer(), value)
        is ApplyPatchCall -> encoder.encodeSerializableValue(ApplyPatchCall.serializer(), value)
        is ApplyPatchCallOutput -> encoder.encodeSerializableValue(ApplyPatchCallOutput.serializer(), value)
        is McpListTools -> encoder.encodeSerializableValue(McpListTools.serializer(), value)
        is McpApprovalRequest -> encoder.encodeSerializableValue(McpApprovalRequest.serializer(), value)
        is McpApprovalResponse -> encoder.encodeSerializableValue(McpApprovalResponse.serializer(), value)
        is McpCall -> encoder.encodeSerializableValue(McpCall.serializer(), value)
      }
    }
  }
}
