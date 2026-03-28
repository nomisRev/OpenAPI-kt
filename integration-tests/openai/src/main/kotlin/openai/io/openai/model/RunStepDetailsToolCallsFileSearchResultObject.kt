package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A result instance of the file search.
 */
@Serializable
public data class RunStepDetailsToolCallsFileSearchResultObject(
  @SerialName("file_id")
  public val fileId: String,
  @SerialName("file_name")
  public val fileName: String,
  public val score: Double,
  public val content: List<Content>? = null,
) {
  @Serializable
  public data class Content(
    public val type: Type? = null,
    public val text: String? = null,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("text")
      Text("text"),
      ;
    }
  }
}
