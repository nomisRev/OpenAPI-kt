package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Learn about [file inputs](/docs/guides/text) for text generation.
 *
 */
@Serializable
public data class ChatCompletionRequestMessageContentPartFile(
  public val type: Type,
  public val `file`: File,
) {
  @Serializable
  public data class File(
    public val filename: String? = null,
    @SerialName("file_data")
    public val fileData: String? = null,
    @SerialName("file_id")
    public val fileId: String? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file")
    File("file"),
    ;
  }
}
