package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A file input to the model.
 */
@Serializable
public data class InputFileContent(
  @Required
  public val type: Type = Type.InputFile,
  @SerialName("file_id")
  public val fileId: String? = null,
  public val filename: String? = null,
  @SerialName("file_data")
  public val fileData: String? = null,
  @SerialName("file_url")
  public val fileUrl: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("input_file")
    InputFile("input_file"),
    ;
  }
}
