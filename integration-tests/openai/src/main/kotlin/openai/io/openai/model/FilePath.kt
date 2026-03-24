package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A path to a file.
 *
 */
@Serializable
public data class FilePath(
  public val type: Type,
  @SerialName("file_id")
  public val fileId: String,
  public val index: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_path")
    FilePath("file_path"),
    ;
  }
}
