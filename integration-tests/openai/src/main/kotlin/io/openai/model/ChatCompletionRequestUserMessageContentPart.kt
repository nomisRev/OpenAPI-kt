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
public sealed interface ChatCompletionRequestUserMessageContentPart {
  /**
   * Learn about [text inputs](/docs/guides/text-generation).
   *
   */
  @SerialName("text")
  @Serializable
  public data class Text(
    public val text: String,
  ) : ChatCompletionRequestUserMessageContentPart

  /**
   * Learn about [image inputs](/docs/guides/vision).
   *
   */
  @SerialName("image_url")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ImageUrl(
    public val url: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val detail: Detail? = null,
  ) : ChatCompletionRequestUserMessageContentPart {
    @Serializable
    public enum class Detail(
      public val `value`: String,
    ) {
      @SerialName("auto")
      Auto("auto"),
      @SerialName("low")
      Low("low"),
      @SerialName("high")
      High("high"),
      ;
    }
  }

  /**
   * Learn about [audio inputs](/docs/guides/audio).
   *
   */
  @SerialName("input_audio")
  @Serializable
  public data class InputAudio(
    public val `data`: String,
    public val format: Format,
  ) : ChatCompletionRequestUserMessageContentPart {
    @Serializable
    public enum class Format(
      public val `value`: String,
    ) {
      @SerialName("wav")
      Wav("wav"),
      @SerialName("mp3")
      Mp3("mp3"),
      ;
    }
  }

  /**
   * Learn about [file inputs](/docs/guides/text) for text generation.
   *
   */
  @SerialName("file")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class File(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val filename: String? = null,
    @SerialName("file_data")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileData: String? = null,
    @SerialName("file_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fileId: String? = null,
  ) : ChatCompletionRequestUserMessageContentPart
}
