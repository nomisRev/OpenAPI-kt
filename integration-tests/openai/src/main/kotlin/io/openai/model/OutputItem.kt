package io.openai.model

import kotlin.Float
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem {
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
      @Serializable
      public data class Search(
        public val query: String,
        public val queries: List<String>? = null,
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
      @JvmInline
      @SerialName("open_page")
      @Serializable
      public value class OpenPage(
        public val url: String? = null,
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
  ) : OutputItem {
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
  @Serializable
  public data class Reasoning(
    public val id: String,
    @SerialName("encrypted_content")
    public val encryptedContent: String? = null,
    public val summary: List<SummaryTextContent>,
    public val content: List<ReasoningTextContent>? = null,
    public val status: Status? = null,
  ) : OutputItem {
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
  ) : OutputItem

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
  ) : OutputItem

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
  ) : OutputItem

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
  ) : OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem {
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
  ) : OutputItem

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
  ) : OutputItem {
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
  ) : OutputItem

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
  ) : OutputItem

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
  ) : OutputItem

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
  ) : OutputItem

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
  ) : OutputItem
}
