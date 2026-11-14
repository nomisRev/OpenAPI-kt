package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A file input to the model.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class InputFileContent(
  @Required
  public val type: Type = Type.InputFile,
  @SerialName("file_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileId: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val filename: String? = null,
  @SerialName("file_data")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileData: String? = null,
  @SerialName("file_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
