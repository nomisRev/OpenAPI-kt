package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ModifyThreadRequest(
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

    @JvmInline
    @Serializable
    public value class FileSearch(
      @SerialName("vector_store_ids")
      public val vectorStoreIds: List<String>? = null,
    )
  }
}
