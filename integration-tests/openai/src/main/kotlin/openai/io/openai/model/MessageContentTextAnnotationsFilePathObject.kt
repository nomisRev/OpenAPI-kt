package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A URL for the file that's generated when the assistant used the `code_interpreter` tool to generate a file.
 */
@Serializable
public data class MessageContentTextAnnotationsFilePathObject(
  public val type: Type,
  public val text: String,
  @SerialName("file_path")
  public val filePath: FilePath,
  @SerialName("start_index")
  public val startIndex: Long,
  @SerialName("end_index")
  public val endIndex: Long,
) {
  @JvmInline
  @Serializable
  public value class FilePath(
    @SerialName("file_id")
    public val fileId: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_path")
    FilePath("file_path"),
    ;
  }
}
