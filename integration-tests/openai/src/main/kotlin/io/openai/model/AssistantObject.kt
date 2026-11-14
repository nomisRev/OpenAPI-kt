package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents an `assistant` that can call the model and use tools.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AssistantObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  public val name: String?,
  public val description: String?,
  public val model: String,
  public val instructions: String?,
  @Required
  public val tools: List<Tools> = emptyList(),
  @SerialName("tool_resources")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolResources: Optional<ToolResources> = Optional.Absent,
  public val metadata: Metadata?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Optional<Double> = Optional.Absent,
  @SerialName("top_p")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topP: Optional<Double> = Optional.Absent,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val responseFormat: Optional<AssistantsApiResponseFormatOption> = Optional.Absent,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("assistant")
    Assistant("assistant"),
    ;
  }

  /**
   * A set of resources that are used by the assistant's tools. The resources are specific to the type of tool. For example, the `code_interpreter` tool requires a list of file IDs, while the `file_search` tool requires a list of vector store IDs.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ToolResources(
    @SerialName("code_interpreter")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val codeInterpreter: CodeInterpreter? = null,
    @SerialName("file_search")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileSearch: FileSearch? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class CodeInterpreter(
      @SerialName("file_ids")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileIds: List<String>? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FileSearch(
      @SerialName("vector_store_ids")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val vectorStoreIds: List<String>? = null,
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @SerialName("code_interpreter")
    public data object CodeInterpreter : Tools

    @SerialName("file_search")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FileSearch(
      @SerialName("file_search")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileSearch: FileSearch? = null,
    ) : Tools {
      /**
       * Overrides for the file search tool.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class FileSearch(
        @SerialName("max_num_results")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val maxNumResults: Long? = null,
        @SerialName("ranking_options")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val rankingOptions: FileSearchRankingOptions? = null,
      )
    }

    @SerialName("function")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Function(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val parameters: FunctionParameters? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val strict: Optional<Boolean> = Optional.Absent,
    ) : Tools
  }
}
