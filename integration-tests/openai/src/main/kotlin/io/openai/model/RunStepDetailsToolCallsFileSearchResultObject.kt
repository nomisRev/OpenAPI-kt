package io.openai.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A result instance of the file search.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RunStepDetailsToolCallsFileSearchResultObject(
  @SerialName("file_id")
  public val fileId: String,
  @SerialName("file_name")
  public val fileName: String,
  public val score: Double,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val content: List<Content>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Content(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: Type? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
