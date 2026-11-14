package io.openai.model

import kotlin.Boolean
import kotlin.Float
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

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
      @SerialName("input_text")
      @Serializable
      public data class InputText(
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
      @SerialName("text")
      @Serializable
      public data class Text(
        public val text: String,
      ) : Content

      /**
       * A summary text from the model.
       */
      @SerialName("summary_text")
      @Serializable
      public data class SummaryText(
        public val text: String,
      ) : Content

      /**
       * Reasoning text from the model.
       */
      @SerialName("reasoning_text")
      @Serializable
      public data class ReasoningText(
        public val text: String,
      ) : Content

      /**
       * A refusal from the model.
       */
      @SerialName("refusal")
      @Serializable
      public data class Refusal(
        public val refusal: String,
      ) : Content

      /**
       * An image input to the model. Learn about [image inputs](/docs/guides/vision).
       */
      @SerialName("input_image")
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class InputImage(
        @SerialName("image_url")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val imageUrl: Optional<String> = Optional.Absent,
        @SerialName("file_id")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val fileId: Optional<String> = Optional.Absent,
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
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class InputFile(
        @SerialName("file_id")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val fileId: Optional<String> = Optional.Absent,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val filename: String? = null,
        @SerialName("file_data")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val fileData: String? = null,
        @SerialName("file_url")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FunctionCall(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val namespace: String? = null,
    public val name: String,
    public val arguments: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ToolSearchCall(
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val arguments: JsonElement,
    public val status: FunctionCallStatus,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: String? = null,
  ) : ItemField

  @SerialName("tool_search_output")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ToolSearchOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String?,
    public val execution: ToolSearchExecutionType,
    public val tools: List<Tool>,
    public val status: FunctionCallOutputStatusEnum,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: String? = null,
  ) : ItemField

  /**
   * The output of a function tool call.
   *
   */
  @SerialName("function_call_output")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FunctionCallOutput(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    public val output: Output,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
          return when {
            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
              value,
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
            value is JsonArray -> json.attemptDeserialize(
              value,
              CaseFunctionAndCustomToolCallOutputList::class to { CaseFunctionAndCustomToolCallOutputList(decodeFromJsonElement(ListSerializer(FunctionAndCustomToolCallOutput.serializer()), it)) },
            )
            else -> throw UnionSerializationException(value, emptyMap())
          }
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FileSearchCall(
    public val id: String,
    public val status: Status,
    public val queries: List<String>,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val results: Optional<List<Results>> = Optional.Absent,
  ) : ItemField {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Results(
      @SerialName("file_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileId: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val text: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val filename: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val attributes: Optional<VectorStoreFileAttributes> = Optional.Absent,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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
    public val action: Action,
  ) : ItemField {
    /**
     * An object describing the specific action taken in this web search call.
     * Includes details on how the model used the web (search, open_page, find_in_page).
     *
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Action {
      /**
       * Action type "search" - Performs a web search query.
       *
       */
      @SerialName("search")
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Search(
        public val query: String,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val queries: List<String>? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val sources: List<Sources>? = null,
      ) : Action {
        /**
         * A source used in the search.
         *
         */
        @Serializable
        public data class Sources(
          public val type: Type,
          public val url: String,
        ) {
          @Serializable
          public enum class Type(
            public val `value`: String,
          ) {
            @SerialName("url")
            Url("url"),
            ;
          }
        }
      }

      /**
       * Action type "open_page" - Opens a specific URL from search results.
       *
       */
      @SerialName("open_page")
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class OpenPage(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val url: Optional<String> = Optional.Absent,
      ) : Action

      /**
       * Action type "find_in_page": Searches for a pattern within a loaded page.
       *
       */
      @SerialName("find_in_page")
      @Serializable
      public data class FindInPage(
        public val url: String,
        public val pattern: String,
      ) : Action
    }

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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ComputerCall(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val action: ComputerAction? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @SerialName("computer_call_output")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ComputerCallOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    @SerialName("acknowledged_safety_checks")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val acknowledgedSafetyChecks: List<ComputerCallSafetyCheckParam>? = null,
    public val output: ComputerScreenshotImage,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
   * A description of the chain of thought used by a reasoning model while generating
   * a response. Be sure to include these items in your `input` to the Responses API
   * for subsequent turns of a conversation if you are manually
   * [managing context](/docs/guides/conversation-state).
   *
   */
  @SerialName("reasoning")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Reasoning(
    public val id: String,
    @SerialName("encrypted_content")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val encryptedContent: Optional<String> = Optional.Absent,
    public val summary: List<SummaryTextContent>,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val content: List<ReasoningTextContent>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Compaction(
    public val id: String,
    @SerialName("encrypted_content")
    public val encryptedContent: String,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
      @SerialName("logs")
      @Serializable
      public data class Logs(
        public val logs: String,
      ) : Outputs

      /**
       * The image output from the code interpreter.
       */
      @SerialName("image")
      @Serializable
      public data class Image(
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LocalShellCallOutput(
    public val id: String,
    public val output: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val status: Optional<Status> = Optional.Absent,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ShellCall(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val action: FunctionShellAction,
    public val status: LocalShellCallStatus,
    public val environment: Environment?,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
      @SerialName("container_reference")
      @Serializable
      public data class ContainerReference(
        @SerialName("container_id")
        public val containerId: String,
      ) : Environment
    }
  }

  /**
   * The output of a shell tool call that was emitted.
   */
  @SerialName("shell_call_output")
  @OptIn(ExperimentalSerializationApi::class)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: String? = null,
  ) : ItemField

  /**
   * A tool call that applies file diffs by creating, deleting, or updating files.
   */
  @SerialName("apply_patch_call")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ApplyPatchCall(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: ApplyPatchCallStatus,
    public val operation: Operation,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
      @SerialName("delete_file")
      @Serializable
      public data class DeleteFile(
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ApplyPatchCallOutput(
    public val id: String,
    @SerialName("call_id")
    public val callId: String,
    public val status: ApplyPatchCallOutputStatus,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val output: Optional<String> = Optional.Absent,
    @SerialName("created_by")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: String? = null,
  ) : ItemField

  /**
   * A list of tools available on an MCP server.
   *
   */
  @SerialName("mcp_list_tools")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class McpListTools(
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val tools: List<MCPListToolsTool>,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val error: Optional<String> = Optional.Absent,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class McpApprovalResponse(
    public val id: String,
    @SerialName("approval_request_id")
    public val approvalRequestId: String,
    public val approve: Boolean,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reason: Optional<String> = Optional.Absent,
  ) : ItemField

  /**
   * An invocation of a tool on an MCP server.
   *
   */
  @SerialName("mcp_call")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class McpCall(
    public val id: String,
    @SerialName("server_label")
    public val serverLabel: String,
    public val name: String,
    public val arguments: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val output: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val error: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val status: MCPToolCallStatus? = null,
    @SerialName("approval_request_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val approvalRequestId: Optional<String> = Optional.Absent,
  ) : ItemField

  /**
   * A call to a custom tool created by the model.
   *
   */
  @SerialName("custom_tool_call")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CustomToolCall(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("call_id")
    public val callId: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val namespace: String? = null,
    public val name: String,
    public val input: String,
  ) : ItemField

  /**
   * The output of a custom tool call from your code, being sent back to the model.
   *
   */
  @SerialName("custom_tool_call_output")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CustomToolCallOutput(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
          return when {
            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
              value,
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
            value is JsonArray -> json.attemptDeserialize(
              value,
              CaseFunctionAndCustomToolCallOutputList::class to { CaseFunctionAndCustomToolCallOutputList(decodeFromJsonElement(ListSerializer(FunctionAndCustomToolCallOutput.serializer()), it)) },
            )
            else -> throw UnionSerializationException(value, emptyMap())
          }
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
