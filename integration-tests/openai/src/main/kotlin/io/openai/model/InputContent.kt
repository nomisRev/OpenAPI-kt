package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface InputContent {
  /**
   * A text input to the model.
   */
  @SerialName("input_text")
  @Serializable
  public data class InputText(
    public val text: String,
  ) : InputContent

  /**
   * An image input to the model. Learn about [image inputs](/docs/guides/vision).
   */
  @SerialName("input_image")
  @Serializable
  public data class InputImage(
    @SerialName("image_url")
    public val imageUrl: String? = null,
    @SerialName("file_id")
    public val fileId: String? = null,
    public val detail: ImageDetail,
  ) : InputContent

  /**
   * A file input to the model.
   */
  @SerialName("input_file")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InputFile(
    @SerialName("file_id")
    public val fileId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val filename: String? = null,
    @SerialName("file_data")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileData: String? = null,
    @SerialName("file_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileUrl: String? = null,
  ) : InputContent
}
