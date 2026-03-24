package io.openai.model

import kotlin.Float
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The results of a file search tool call. See the
 * [file search guide](/docs/guides/tools-file-search) for more information.
 *
 */
@Serializable
public data class FileSearchToolCall(
  public val id: String,
  public val type: Type,
  public val status: Status,
  public val queries: List<String>,
  public val results: List<Results>? = null,
) {
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
