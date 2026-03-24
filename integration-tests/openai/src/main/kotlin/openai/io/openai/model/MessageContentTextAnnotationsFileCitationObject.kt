package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A citation within the message that points to a specific quote from a specific File associated with the assistant or the message. Generated when the assistant uses the "file_search" tool to search files.
 */
@Serializable
public data class MessageContentTextAnnotationsFileCitationObject(
  public val type: Type,
  public val text: String,
  @SerialName("file_citation")
  public val fileCitation: FileCitation,
  @SerialName("start_index")
  public val startIndex: Long,
  @SerialName("end_index")
  public val endIndex: Long,
) {
  @JvmInline
  @Serializable
  public value class FileCitation(
    @SerialName("file_id")
    public val fileId: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_citation")
    FileCitation("file_citation"),
    ;
  }
}
