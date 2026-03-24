package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Options to create a new thread. If no thread is provided when running a
 * request, an empty thread will be created.
 *
 */
@Serializable
public data class CreateThreadRequest(
  public val messages: List<CreateMessageRequest>? = null,
  @SerialName("tool_resources")
  public val toolResources: ToolResources? = null,
  public val metadata: Metadata? = null,
) {
  /**
   * A set of resources that are made available to the assistant's tools in this thread. The resources are specific to the type of tool. For example, the `code_interpreter` tool requires a list of file IDs, while the `file_search` tool requires a list of vector store IDs.
   *
   */
  @Serializable
  public data class ToolResources(
    @SerialName("code_interpreter")
    public val codeInterpreter: CodeInterpreter? = null,
    @SerialName("file_search")
    public val fileSearch: FileSearch? = null,
  ) {
    @JvmInline
    @Serializable
    public value class CodeInterpreter(
      @SerialName("file_ids")
      public val fileIds: List<String>? = null,
    )

    @Serializable
    public data class FileSearch(
      @SerialName("vector_store_ids")
      public val vectorStoreIds: List<String>? = null,
      @SerialName("vector_stores")
      public val vectorStores: List<VectorStores>? = null,
    ) {
      @Serializable
      public data class VectorStores(
        @SerialName("file_ids")
        public val fileIds: List<String>? = null,
        @SerialName("chunking_strategy")
        public val chunkingStrategy: JsonElement? = null,
        public val metadata: Metadata? = null,
      )
    }
  }
}
