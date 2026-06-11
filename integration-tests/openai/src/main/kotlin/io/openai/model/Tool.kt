package io.openai.model

import kotlin.Boolean
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * A tool that can be used to generate a response.
 *
 */
@Serializable(with = Tool.Serializer::class)
public sealed interface Tool {
  /**
   * Defines a function in your own code the model can choose to call. Learn more about [function calling](https://platform.openai.com/docs/guides/function-calling).
   */
  @Serializable
  public data class Function(
    @Required
    public val type: Type = Type.Function,
    public val name: String,
    public val description: String? = null,
    @Required
    public val parameters: JsonArray? = null,
    public val strict: Boolean?,
    @SerialName("defer_loading")
    public val deferLoading: Boolean? = null,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("function")
      Function("function"),
      ;
    }
  }

  /**
   * A tool that searches for relevant content from uploaded files. Learn more about the [file search tool](https://platform.openai.com/docs/guides/tools-file-search).
   */
  @Serializable
  public data class FileSearch(
    @Required
    public val type: Type = Type.FileSearch,
    @SerialName("vector_store_ids")
    public val vectorStoreIds: List<String>,
    @SerialName("max_num_results")
    public val maxNumResults: Long? = null,
    @SerialName("ranking_options")
    public val rankingOptions: RankingOptions? = null,
    public val filters: Filters? = null,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("file_search")
      FileSearch("file_search"),
      ;
    }
  }

  /**
   * A tool that controls a virtual computer. Learn more about the [computer tool](https://platform.openai.com/docs/guides/tools-computer-use).
   */
  @JvmInline
  @Serializable
  public value class Computer(
    @Required
    public val type: Type = Type.Computer,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("computer")
      Computer("computer"),
      ;
    }
  }

  /**
   * A tool that controls a virtual computer. Learn more about the [computer tool](https://platform.openai.com/docs/guides/tools-computer-use).
   */
  @Serializable
  public data class ComputerUsePreview(
    @Required
    public val type: Type = Type.ComputerUsePreview,
    public val environment: ComputerEnvironment,
    @SerialName("display_width")
    public val displayWidth: Long,
    @SerialName("display_height")
    public val displayHeight: Long,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("computer_use_preview")
      ComputerUsePreview("computer_use_preview"),
      ;
    }
  }

  /**
   * Search the Internet for sources related to the prompt. Learn more about the
   * [web search tool](/docs/guides/tools-web-search).
   *
   */
  @Serializable
  public data class WebSearchTool(
    @Required
    public val type: Type = Type.WebSearch,
    public val filters: Filters? = null,
    @SerialName("user_location")
    public val userLocation: WebSearchApproximateLocation? = null,
    @SerialName("search_context_size")
    public val searchContextSize: SearchContextSize? = null,
  ) : Tool {
    /**
     * Filters for the search.
     *
     */
    @JvmInline
    @Serializable
    public value class Filters(
      @SerialName("allowed_domains")
      public val allowedDomains: List<String>? = null,
    )

    @Serializable
    public enum class SearchContextSize(
      public val `value`: String,
    ) {
      @SerialName("low")
      Low("low"),
      @SerialName("medium")
      Medium("medium"),
      @SerialName("high")
      High("high"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("web_search")
      WebSearch("web_search"),
      @SerialName("web_search_2025_08_26")
      WebSearch20250826("web_search_2025_08_26"),
      ;
    }
  }

  /**
   * Give the model access to additional tools via remote Model Context Protocol
   * (MCP) servers. [Learn more about MCP](/docs/guides/tools-remote-mcp).
   *
   */
  @Serializable
  public data class Mcp(
    public val type: Type,
    @SerialName("server_label")
    public val serverLabel: String,
    @SerialName("server_url")
    public val serverUrl: String? = null,
    @SerialName("connector_id")
    public val connectorId: ConnectorId? = null,
    public val authorization: String? = null,
    @SerialName("server_description")
    public val serverDescription: String? = null,
    public val headers: List<String>? = null,
    @SerialName("allowed_tools")
    public val allowedTools: AllowedTools? = null,
    @SerialName("require_approval")
    public val requireApproval: RequireApproval? = null,
    @SerialName("defer_loading")
    public val deferLoading: Boolean? = null,
  ) : Tool {
    /**
     * List of allowed tool names or a filter object.
     *
     */
    @Serializable(with = AllowedTools.Serializer::class)
    public sealed interface AllowedTools {
      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>,
      ) : AllowedTools

      @Serializable
      @JvmInline
      public value class CaseMCPToolFilter(
        public val `value`: MCPToolFilter,
      ) : AllowedTools

      public object Serializer : KSerializer<AllowedTools> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.Tool.Mcp.AllowedTools", PolymorphicKind.SEALED) {
          element("CaseStrings", ListSerializer(String.serializer()).descriptor)
          element("CaseMCPToolFilter", MCPToolFilter.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): AllowedTools {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
            CaseMCPToolFilter::class to { CaseMCPToolFilter(decodeFromJsonElement(MCPToolFilter.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: AllowedTools) {
          when(value) {
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            is CaseMCPToolFilter -> encoder.encodeSerializableValue(MCPToolFilter.serializer(), value.value)
          }
        }
      }
    }

    @Serializable
    public enum class ConnectorId(
      public val `value`: String,
    ) {
      @SerialName("connector_dropbox")
      ConnectorDropbox("connector_dropbox"),
      @SerialName("connector_gmail")
      ConnectorGmail("connector_gmail"),
      @SerialName("connector_googlecalendar")
      ConnectorGooglecalendar("connector_googlecalendar"),
      @SerialName("connector_googledrive")
      ConnectorGoogledrive("connector_googledrive"),
      @SerialName("connector_microsoftteams")
      ConnectorMicrosoftteams("connector_microsoftteams"),
      @SerialName("connector_outlookcalendar")
      ConnectorOutlookcalendar("connector_outlookcalendar"),
      @SerialName("connector_outlookemail")
      ConnectorOutlookemail("connector_outlookemail"),
      @SerialName("connector_sharepoint")
      ConnectorSharepoint("connector_sharepoint"),
      ;
    }

    /**
     * Specify which of the MCP server's tools require approval.
     */
    @Serializable(with = RequireApproval.Serializer::class)
    public sealed interface RequireApproval {
      /**
       * Specify which of the MCP server's tools require approval. Can be
       * `always`, `never`, or a filter object associated with tools
       * that require approval.
       *
       */
      @Serializable
      public data class MCPToolApprovalFilter(
        public val always: MCPToolFilter? = null,
        public val never: MCPToolFilter? = null,
      ) : RequireApproval

      @Serializable
      public enum class MCPToolApprovalSetting(
        public val `value`: String,
      ) : RequireApproval {
        @SerialName("always")
        Always("always"),
        @SerialName("never")
        Never("never"),
        ;
      }

      public object Serializer : KSerializer<RequireApproval> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.Tool.Mcp.RequireApproval", PolymorphicKind.SEALED) {
          element("MCPToolApprovalFilter", MCPToolApprovalFilter.serializer().descriptor)
          element("MCPToolApprovalSetting", MCPToolApprovalSetting.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): RequireApproval {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            MCPToolApprovalFilter::class to { decodeFromJsonElement(MCPToolApprovalFilter.serializer(), it) },
            MCPToolApprovalSetting::class to { decodeFromJsonElement(MCPToolApprovalSetting.serializer(), it) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: RequireApproval) {
          when(value) {
            is MCPToolApprovalFilter -> encoder.encodeSerializableValue(MCPToolApprovalFilter.serializer(), value)
            is MCPToolApprovalSetting -> encoder.encodeSerializableValue(MCPToolApprovalSetting.serializer(), value)
          }
        }
      }
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("mcp")
      Mcp("mcp"),
      ;
    }
  }

  /**
   * A tool that runs Python code to help generate a response to a prompt.
   *
   */
  @Serializable
  public data class CodeInterpreter(
    public val type: Type,
    public val container: Container,
  ) : Tool {
    /**
     * The code interpreter container. Can be a container ID or an object that
     * specifies uploaded file IDs to make available to your code, along with an
     * optional `memory_limit` setting.
     *
     */
    @Serializable(with = Container.Serializer::class)
    public sealed interface Container {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Container

      @Serializable
      @JvmInline
      public value class CaseAutoCodeInterpreterToolParam(
        public val `value`: AutoCodeInterpreterToolParam,
      ) : Container

      public object Serializer : KSerializer<Container> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.Tool.CodeInterpreter.Container", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseAutoCodeInterpreterToolParam", AutoCodeInterpreterToolParam.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Container {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseAutoCodeInterpreterToolParam::class to { CaseAutoCodeInterpreterToolParam(decodeFromJsonElement(AutoCodeInterpreterToolParam.serializer(), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Container) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseAutoCodeInterpreterToolParam -> encoder.encodeSerializableValue(AutoCodeInterpreterToolParam.serializer(), value.value)
          }
        }
      }
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("code_interpreter")
      CodeInterpreter("code_interpreter"),
      ;
    }
  }

  /**
   * A tool that generates images using the GPT image models.
   *
   */
  @Serializable
  public data class ImageGeneration(
    public val type: Type,
    public val model: Model? = null,
    public val quality: Quality? = null,
    public val size: Size? = null,
    @SerialName("output_format")
    public val outputFormat: OutputFormat? = null,
    @SerialName("output_compression")
    public val outputCompression: Long? = null,
    public val moderation: Moderation? = null,
    public val background: Background? = null,
    @SerialName("input_fidelity")
    public val inputFidelity: InputFidelity? = null,
    @SerialName("input_image_mask")
    public val inputImageMask: InputImageMask? = null,
    @SerialName("partial_images")
    public val partialImages: Long? = null,
    public val action: ImageGenActionEnum? = null,
  ) : Tool {
    @Serializable
    public enum class Background(
      public val `value`: String,
    ) {
      @SerialName("transparent")
      Transparent("transparent"),
      @SerialName("opaque")
      Opaque("opaque"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    /**
     * Optional mask for inpainting. Contains `image_url`
     * (string, optional) and `file_id` (string, optional).
     *
     */
    @Serializable
    public data class InputImageMask(
      @SerialName("image_url")
      public val imageUrl: String? = null,
      @SerialName("file_id")
      public val fileId: String? = null,
    )

    @Serializable(with = Model.Serializer::class)
    public sealed interface Model {
      public val `value`: String

      @Serializable
      @JvmInline
      public value class CaseString(
        override val `value`: String,
      ) : Model

      @Serializable
      public enum class GptImage1OrGptImage1MiniOrGptImage15(
        override val `value`: String,
      ) : Model {
        @SerialName("gpt-image-1")
        GptImage1("gpt-image-1"),
        @SerialName("gpt-image-1-mini")
        GptImage1Mini("gpt-image-1-mini"),
        @SerialName("gpt-image-1.5")
        GptImage15("gpt-image-1.5"),
        ;
      }

      public object Serializer : KSerializer<Model> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(encoder: Encoder, `value`: Model) {
          when(value) {
            GptImage1OrGptImage1MiniOrGptImage15.GptImage1 -> encoder.encodeString("gpt-image-1")
            GptImage1OrGptImage1MiniOrGptImage15.GptImage1Mini -> encoder.encodeString("gpt-image-1-mini")
            GptImage1OrGptImage1MiniOrGptImage15.GptImage15 -> encoder.encodeString("gpt-image-1.5")
            is CaseString -> encoder.encodeString(value.value)
          }
        }

        override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
          "gpt-image-1" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage1
          "gpt-image-1-mini" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage1Mini
          "gpt-image-1.5" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage15
          else -> CaseString(value)
        }
      }
    }

    @Serializable
    public enum class Moderation(
      public val `value`: String,
    ) {
      @SerialName("auto")
      Auto("auto"),
      @SerialName("low")
      Low("low"),
      ;
    }

    @Serializable
    public enum class OutputFormat(
      public val `value`: String,
    ) {
      @SerialName("png")
      Png("png"),
      @SerialName("webp")
      Webp("webp"),
      @SerialName("jpeg")
      Jpeg("jpeg"),
      ;
    }

    @Serializable
    public enum class Quality(
      public val `value`: String,
    ) {
      @SerialName("low")
      Low("low"),
      @SerialName("medium")
      Medium("medium"),
      @SerialName("high")
      High("high"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    public enum class Size(
      public val `value`: String,
    ) {
      `1024x1024`("1024x1024"),
      `1024x1536`("1024x1536"),
      `1536x1024`("1536x1024"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("image_generation")
      ImageGeneration("image_generation"),
      ;
    }
  }

  /**
   * A tool that allows the model to execute shell commands in a local environment.
   */
  @JvmInline
  @Serializable
  public value class LocalShell(
    @Required
    public val type: Type = Type.LocalShell,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("local_shell")
      LocalShell("local_shell"),
      ;
    }
  }

  /**
   * A tool that allows the model to execute shell commands.
   */
  @Serializable
  public data class Shell(
    @Required
    public val type: Type = Type.Shell,
    public val environment: Environment? = null,
  ) : Tool {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Environment {
      @SerialName("container_auto")
      @Serializable
      public data class ContainerAuto(
        @SerialName("file_ids")
        public val fileIds: List<String>? = null,
        @SerialName("memory_limit")
        public val memoryLimit: ContainerMemoryLimit? = null,
        @SerialName("network_policy")
        public val networkPolicy: NetworkPolicy? = null,
        public val skills: List<Skills>? = null,
      ) : Environment {
        /**
         * Network access policy for the container.
         */
        @OptIn(ExperimentalSerializationApi::class)
        @JsonClassDiscriminator("type")
        @Serializable
        public sealed interface NetworkPolicy {
          @Serializable
          @SerialName("disabled")
          public data object Disabled : NetworkPolicy

          @SerialName("allowlist")
          @Serializable
          public data class Allowlist(
            @SerialName("allowed_domains")
            public val allowedDomains: List<String>,
            @SerialName("domain_secrets")
            public val domainSecrets: List<ContainerNetworkPolicyDomainSecretParam>? = null,
          ) : NetworkPolicy
        }

        @OptIn(ExperimentalSerializationApi::class)
        @JsonClassDiscriminator("type")
        @Serializable
        public sealed interface Skills {
          @SerialName("skill_reference")
          @Serializable
          public data class SkillReference(
            @SerialName("skill_id")
            public val skillId: String,
            public val version: String? = null,
          ) : Skills

          @SerialName("inline")
          @Serializable
          public data class Inline(
            public val name: String,
            public val description: String,
            public val source: InlineSkillSourceParam,
          ) : Skills
        }
      }

      @JvmInline
      @SerialName("local")
      @Serializable
      public value class Local(
        public val skills: List<LocalSkillParam>? = null,
      ) : Environment

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
      @SerialName("shell")
      Shell("shell"),
      ;
    }
  }

  /**
   * A custom tool that processes input using a specified format. Learn more about   [custom tools](/docs/guides/function-calling#custom-tools)
   */
  @Serializable
  public data class Custom(
    @Required
    public val type: Type = Type.Custom,
    public val name: String,
    public val description: String? = null,
    public val format: Format? = null,
    @SerialName("defer_loading")
    public val deferLoading: Boolean? = null,
  ) : Tool {
    /**
     * The input format for the custom tool. Default is unconstrained text.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Format {
      @Serializable
      @SerialName("text")
      public data object Text : Format

      /**
       * A grammar defined by the user.
       */
      @SerialName("grammar")
      @Serializable
      public data class Grammar(
        public val syntax: GrammarSyntax1,
        public val definition: String,
      ) : Format
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("custom")
      Custom("custom"),
      ;
    }
  }

  /**
   * Groups function/custom tools under a shared namespace.
   */
  @Serializable
  public data class Namespace(
    @Required
    public val type: Type = Type.Namespace,
    public val name: String,
    public val description: String,
    public val tools: List<Tools>,
  ) : Tool {
    /**
     * A function or custom tool that belongs to a namespace.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    @Serializable
    public sealed interface Tools {
      @SerialName("function")
      @Serializable
      public data class Function(
        public val name: String,
        public val description: String? = null,
        public val parameters: EmptyModelParam? = null,
        public val strict: Boolean? = null,
      ) : Tools

      /**
       * A custom tool that processes input using a specified format. Learn more about   [custom tools](/docs/guides/function-calling#custom-tools)
       */
      @SerialName("custom")
      @Serializable
      public data class Custom(
        public val name: String,
        public val description: String? = null,
        public val format: Format? = null,
        @SerialName("defer_loading")
        public val deferLoading: Boolean? = null,
      ) : Tools {
        /**
         * The input format for the custom tool. Default is unconstrained text.
         */
        @OptIn(ExperimentalSerializationApi::class)
        @JsonClassDiscriminator("type")
        @Serializable
        public sealed interface Format {
          @Serializable
          @SerialName("text")
          public data object Text : Format

          /**
           * A grammar defined by the user.
           */
          @SerialName("grammar")
          @Serializable
          public data class Grammar(
            public val syntax: GrammarSyntax1,
            public val definition: String,
          ) : Format
        }
      }
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("namespace")
      Namespace("namespace"),
      ;
    }
  }

  /**
   * Hosted or BYOT tool search configuration for deferred tools.
   */
  @Serializable
  public data class ToolSearch(
    @Required
    public val type: Type = Type.ToolSearch,
    public val execution: ToolSearchExecutionType? = null,
    public val description: String? = null,
    public val parameters: EmptyModelParam? = null,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("tool_search")
      ToolSearch("tool_search"),
      ;
    }
  }

  /**
   * This tool searches the web for relevant results to use in a response. Learn more about the [web search tool](https://platform.openai.com/docs/guides/tools-web-search).
   */
  @Serializable
  public data class WebSearchPreviewTool(
    @Required
    public val type: Type = Type.WebSearchPreview,
    @SerialName("user_location")
    public val userLocation: ApproximateLocation? = null,
    @SerialName("search_context_size")
    public val searchContextSize: SearchContextSize? = null,
    @SerialName("search_content_types")
    public val searchContentTypes: List<SearchContentType>? = null,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("web_search_preview")
      WebSearchPreview("web_search_preview"),
      @SerialName("web_search_preview_2025_03_11")
      WebSearchPreview20250311("web_search_preview_2025_03_11"),
      ;
    }
  }

  /**
   * Allows the assistant to create, delete, or update files using unified diffs.
   */
  @JvmInline
  @Serializable
  public value class ApplyPatch(
    @Required
    public val type: Type = Type.ApplyPatch,
  ) : Tool {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("apply_patch")
      ApplyPatch("apply_patch"),
      ;
    }
  }

  public object Serializer : KSerializer<Tool> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.Tool", PolymorphicKind.SEALED) {
      element("Function", Function.serializer().descriptor)
      element("FileSearch", FileSearch.serializer().descriptor)
      element("Computer", Computer.serializer().descriptor)
      element("ComputerUsePreview", ComputerUsePreview.serializer().descriptor)
      element("WebSearchTool", WebSearchTool.serializer().descriptor)
      element("Mcp", Mcp.serializer().descriptor)
      element("CodeInterpreter", CodeInterpreter.serializer().descriptor)
      element("ImageGeneration", ImageGeneration.serializer().descriptor)
      element("LocalShell", LocalShell.serializer().descriptor)
      element("Shell", Shell.serializer().descriptor)
      element("Custom", Custom.serializer().descriptor)
      element("Namespace", Namespace.serializer().descriptor)
      element("ToolSearch", ToolSearch.serializer().descriptor)
      element("WebSearchPreviewTool", WebSearchPreviewTool.serializer().descriptor)
      element("ApplyPatch", ApplyPatch.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Tool {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "function" -> return json.decodeFromJsonElement(Function.serializer(), value)
        "file_search" -> return json.decodeFromJsonElement(FileSearch.serializer(), value)
        "computer" -> return json.decodeFromJsonElement(Computer.serializer(), value)
        "computer_use_preview" -> return json.decodeFromJsonElement(ComputerUsePreview.serializer(), value)
        "web_search", "web_search_2025_08_26" -> return json.decodeFromJsonElement(WebSearchTool.serializer(), value)
        "mcp" -> return json.decodeFromJsonElement(Mcp.serializer(), value)
        "code_interpreter" -> return json.decodeFromJsonElement(CodeInterpreter.serializer(), value)
        "image_generation" -> return json.decodeFromJsonElement(ImageGeneration.serializer(), value)
        "local_shell" -> return json.decodeFromJsonElement(LocalShell.serializer(), value)
        "shell" -> return json.decodeFromJsonElement(Shell.serializer(), value)
        "custom" -> return json.decodeFromJsonElement(Custom.serializer(), value)
        "namespace" -> return json.decodeFromJsonElement(Namespace.serializer(), value)
        "tool_search" -> return json.decodeFromJsonElement(ToolSearch.serializer(), value)
        "web_search_preview", "web_search_preview_2025_03_11" -> return json.decodeFromJsonElement(WebSearchPreviewTool.serializer(), value)
        "apply_patch" -> return json.decodeFromJsonElement(ApplyPatch.serializer(), value)
        else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.Tool")
      }
    }

    override fun serialize(encoder: Encoder, `value`: Tool) {
      when(value) {
        is Function -> encoder.encodeSerializableValue(Function.serializer(), value)
        is FileSearch -> encoder.encodeSerializableValue(FileSearch.serializer(), value)
        is Computer -> encoder.encodeSerializableValue(Computer.serializer(), value)
        is ComputerUsePreview -> encoder.encodeSerializableValue(ComputerUsePreview.serializer(), value)
        is WebSearchTool -> encoder.encodeSerializableValue(WebSearchTool.serializer(), value)
        is Mcp -> encoder.encodeSerializableValue(Mcp.serializer(), value)
        is CodeInterpreter -> encoder.encodeSerializableValue(CodeInterpreter.serializer(), value)
        is ImageGeneration -> encoder.encodeSerializableValue(ImageGeneration.serializer(), value)
        is LocalShell -> encoder.encodeSerializableValue(LocalShell.serializer(), value)
        is Shell -> encoder.encodeSerializableValue(Shell.serializer(), value)
        is Custom -> encoder.encodeSerializableValue(Custom.serializer(), value)
        is Namespace -> encoder.encodeSerializableValue(Namespace.serializer(), value)
        is ToolSearch -> encoder.encodeSerializableValue(ToolSearch.serializer(), value)
        is WebSearchPreviewTool -> encoder.encodeSerializableValue(WebSearchPreviewTool.serializer(), value)
        is ApplyPatch -> encoder.encodeSerializableValue(ApplyPatch.serializer(), value)
      }
    }
  }
}
