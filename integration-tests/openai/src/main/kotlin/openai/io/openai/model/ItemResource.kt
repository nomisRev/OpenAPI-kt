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

/**
 * Content item used to generate a response.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ItemResource {
  /**
   * A message input to the model with a role indicating instruction following
   * hierarchy. Instructions given with the `developer` or `system` role take
   * precedence over instructions given with the `user` role.
   *
   */
  @SerialName("InputMessageResource")
  @Serializable
  public data class InputMessageResource(
    public val type: Type? = null,
    public val role: Role,
    public val status: Status? = null,
    public val content: InputMessageContentList,
    public val id: String? = null,
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
  @SerialName("message")
  @Serializable
  public data class Message(
    public val id: String,
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
  }

  /**
   * The results of a file search tool call. See the
   * [file search guide](/docs/guides/tools-file-search) for more information.
   *
   */
  @SerialName("file_search_call")
  @Serializable
  public data class FileSearchCall(
    public val id: String,
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
  }

  /**
   * A tool call to a computer use tool. See the
   * [computer use guide](/docs/guides/tools-computer-use) for more information.
   *
   */
  @SerialName("computer_call")
  @Serializable
  public data class ComputerCall(
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
  }

  /**
   * The output of a computer tool call.
   *
   */
  @SerialName("ComputerToolCallOutputResource")
  @Serializable
  public data class ComputerToolCallOutputResource(
    @Required
    public val type: Type = Type.ComputerCallOutput,
    public val id: String? = null,
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
  @SerialName("web_search_call")
  @Serializable
  public data class WebSearchCall(
    public val id: String,
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
  }

  /**
   * A tool call to run a function. See the 
   * [function calling guide](/docs/guides/function-calling) for more information.
   *
   */
  @SerialName("FunctionToolCallResource")
  @Serializable
  public data class FunctionToolCallResource(
    public val id: String? = null,
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
  @SerialName("FunctionToolCallOutputResource")
  @Serializable
  public data class FunctionToolCallOutputResource(
    public val id: String? = null,
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
            buildSerialDescriptor("io.openai.model.ItemResource.FunctionToolCallOutputResource.Output", PolymorphicKind.SEALED) {
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

  @SerialName("tool_search_call")
  @Serializable
  public data class ToolSearchCall(
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val arguments: JsonElement,
    public val status: FunctionCallStatus,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource

  @SerialName("tool_search_output")
  @Serializable
  public data class ToolSearchOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val tools: List<Tool>,
    public val status: FunctionCallOutputStatusEnum,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource

  /**
   * An image generation request made by the model.
   *
   */
  @SerialName("image_generation_call")
  @Serializable
  public data class ImageGenerationCall(
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
  }

  /**
   * A tool call to run code.
   *
   */
  @SerialName("code_interpreter_call")
  @Serializable
  public data class CodeInterpreterCall(
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
  }

  /**
   * A tool call to run a command on the local shell.
   *
   */
  @SerialName("local_shell_call")
  @Serializable
  public data class LocalShellCall(
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
  }

  /**
   * The output of a local shell tool call.
   *
   */
  @SerialName("local_shell_call_output")
  @Serializable
  public data class LocalShellCallOutput(
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
  }

  /**
   * A tool call that executes one or more shell commands in a managed environment.
   */
  @SerialName("shell_call")
  @Serializable
  public data class ShellCall(
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
  }

  /**
   * The output of a shell tool call that was emitted.
   */
  @SerialName("shell_call_output")
  @Serializable
  public data class ShellCallOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: LocalShellCallOutputStatusEnum,
    public val output: List<FunctionShellCallOutputContent>,
    @SerialName("max_output_length")
    public val maxOutputLength: Long?,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource

  /**
   * A tool call that applies file diffs by creating, deleting, or updating files.
   */
  @SerialName("apply_patch_call")
  @Serializable
  public data class ApplyPatchCall(
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
  }

  /**
   * The output emitted by an apply patch tool call.
   */
  @SerialName("apply_patch_call_output")
  @Serializable
  public data class ApplyPatchCallOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: ApplyPatchCallOutputStatus,
    public val output: String? = null,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemResource

  /**
   * A list of tools available on an MCP server.
   *
   */
  @SerialName("mcp_list_tools")
  @Serializable
  public data class McpListTools(
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val tools: List<MCPListToolsTool>,
    public val error: String? = null,
  ) : ItemResource

  /**
   * A request for human approval of a tool invocation.
   *
   */
  @SerialName("mcp_approval_request")
  @Serializable
  public data class McpApprovalRequest(
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val name: String,
    public val arguments: String,
  ) : ItemResource

  /**
   * A response to an MCP approval request.
   *
   */
  @SerialName("mcp_approval_response")
  @Serializable
  public data class McpApprovalResponse(
    public val id: String,
    @SerialName("approval_request_id")
    public val approvalRequestId: String,
    public val approve: Boolean,
    public val reason: String? = null,
  ) : ItemResource

  /**
   * An invocation of a tool on an MCP server.
   *
   */
  @SerialName("mcp_call")
  @Serializable
  public data class McpCall(
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
  ) : ItemResource
}
