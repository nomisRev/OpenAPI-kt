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
 * An item representing a message, tool call, tool output, reasoning, or other response element.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ItemField {
  /**
   * A message to or from the model.
   */
  @SerialName("message")
  @Serializable
  public data class Message(
    public val id: String,
    public val status: MessageStatus,
    public val role: MessageRole,
    public val content: List<Content>,
  ) : ItemField {
    /**
     * A content part that makes up an input or output item.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Content {
      /**
       * A text input to the model.
       */
      @JvmInline
      @SerialName("input_text")
      @Serializable
      public value class InputText(
        public val text: String,
      ) : Content

      /**
       * A text output from the model.
       */
      @SerialName("output_text")
      @Serializable
      public data class OutputText(
        public val text: String,
        public val annotations: List<Annotation>,
        public val logprobs: List<LogProb>,
      ) : Content

      /**
       * A text content.
       */
      @JvmInline
      @SerialName("text")
      @Serializable
      public value class Text(
        public val text: String,
      ) : Content

      /**
       * A summary text from the model.
       */
      @JvmInline
      @SerialName("summary_text")
      @Serializable
      public value class SummaryText(
        public val text: String,
      ) : Content

      /**
       * Reasoning text from the model.
       */
      @JvmInline
      @SerialName("reasoning_text")
      @Serializable
      public value class ReasoningText(
        public val text: String,
      ) : Content

      /**
       * A refusal from the model.
       */
      @JvmInline
      @SerialName("refusal")
      @Serializable
      public value class Refusal(
        public val refusal: String,
      ) : Content

      /**
       * An image input to the model. Learn about [image inputs](/docs/guides/vision).
       */
      @SerialName("input_image")
      @Serializable
      public data class InputImage(
        @SerialName("image_url")
        public val imageUrl: String? = null,
        @SerialName("file_id")
        public val fileId: String? = null,
        public val detail: ImageDetail,
      ) : Content

      /**
       * A screenshot of a computer.
       */
      @SerialName("computer_screenshot")
      @Serializable
      public data class ComputerScreenshot(
        @SerialName("image_url")
        public val imageUrl: String?,
        @SerialName("file_id")
        public val fileId: String?,
        public val detail: ImageDetail,
      ) : Content

      /**
       * A file input to the model.
       */
      @SerialName("input_file")
      @Serializable
      public data class InputFile(
        @SerialName("file_id")
        public val fileId: String? = null,
        public val filename: String? = null,
        @SerialName("file_data")
        public val fileData: String? = null,
        @SerialName("file_url")
        public val fileUrl: String? = null,
      ) : Content
    }
  }

  /**
   * A tool call to run a function. See the 
   * [function calling guide](/docs/guides/function-calling) for more information.
   *
   */
  @SerialName("function_call")
  @Serializable
  public data class FunctionCall(
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    public val namespace: String? = null,
    public val name: String,
    public val arguments: String,
    public val status: Status? = null,
  ) : ItemField {
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
  ) : ItemField

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
  ) : ItemField

  /**
   * The output of a function tool call.
   *
   */
  @SerialName("function_call_output")
  @Serializable
  public data class FunctionCallOutput(
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    public val output: Output,
    public val status: Status? = null,
  ) : ItemField {
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
            buildSerialDescriptor("io.openai.model.ItemField.FunctionCallOutput.Output", PolymorphicKind.SEALED) {
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
  ) : ItemField {
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
  ) : ItemField {
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
   * An image generation request made by the model.
   *
   */
  @SerialName("image_generation_call")
  @Serializable
  public data class ImageGenerationCall(
    public val id: String,
    public val status: Status,
    public val result: String?,
  ) : ItemField {
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
  ) : ItemField {
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
  ) : ItemField {
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
   * A description of the chain of thought used by a reasoning model while generating
   * a response. Be sure to include these items in your `input` to the Responses API
   * for subsequent turns of a conversation if you are manually
   * [managing context](/docs/guides/conversation-state).
   *
   */
  @SerialName("reasoning")
  @Serializable
  public data class Reasoning(
    public val id: String,
    @SerialName("encrypted_content")
    public val encryptedContent: String? = null,
    public val summary: List<SummaryTextContent>,
    public val content: List<ReasoningTextContent>? = null,
    public val status: Status? = null,
  ) : ItemField {
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
   * A compaction item generated by the [`v1/responses/compact` API](/docs/api-reference/responses/compact).
   */
  @SerialName("compaction")
  @Serializable
  public data class Compaction(
    public val id: String,
    @SerialName("encrypted_content")
    public val encryptedContent: String,
    @SerialName("created_by")
    public val createdBy: String? = null,
  ) : ItemField

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
  ) : ItemField {
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
  ) : ItemField {
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
  ) : ItemField {
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
  ) : ItemField {
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
  ) : ItemField

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
  ) : ItemField {
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
  ) : ItemField

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
  ) : ItemField

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
  ) : ItemField

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
  ) : ItemField

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
  ) : ItemField

  /**
   * A call to a custom tool created by the model.
   *
   */
  @SerialName("custom_tool_call")
  @Serializable
  public data class CustomToolCall(
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    public val namespace: String? = null,
    public val name: String,
    public val input: String,
  ) : ItemField

  /**
   * The output of a custom tool call from your code, being sent back to the model.
   *
   */
  @SerialName("custom_tool_call_output")
  @Serializable
  public data class CustomToolCallOutput(
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    public val output: Output,
  ) : ItemField {
    /**
     * The output from the custom tool call generated by your code.
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
            buildSerialDescriptor("io.openai.model.ItemField.CustomToolCallOutput.Output", PolymorphicKind.SEALED) {
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
  }
}
