package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface FunctionAndCustomToolCallOutput {
  /**
   * A text input to the model.
   */
  @JvmInline
  @SerialName("input_text")
  @Serializable
  public value class InputText(
    public val text: String,
  ) : FunctionAndCustomToolCallOutput

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
  ) : FunctionAndCustomToolCallOutput

  /**
   * A file input to the model.
   */
  @SerialName("input_file")
  @Serializable
  public data class InputFile(
    @SerialName("file_id")
    public val fileId: String? = null,
    public val filename: String? = null,
    @SerialName("file_data")
    public val fileData: String? = null,
    @SerialName("file_url")
    public val fileUrl: String? = null,
  ) : FunctionAndCustomToolCallOutput
}
