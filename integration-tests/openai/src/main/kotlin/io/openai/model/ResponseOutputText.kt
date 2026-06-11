package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Assistant response text accompanied by optional annotations.
 */
@Serializable
public data class ResponseOutputText(
  @Required
  public val type: Type = Type.OutputText,
  public val text: String,
  public val annotations: List<Annotations>,
) {
  /**
   * Annotation object describing a cited source.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Annotations {
    /**
     * Annotation that references an uploaded file.
     */
    @SerialName("file")
    @Serializable
    public data class File(
      @Required
      public val type: Type = Type.File,
      public val filename: String,
    ) : Annotations {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("file")
        File("file"),
        ;
      }
    }

    /**
     * Annotation that references a URL.
     */
    @SerialName("url")
    @Serializable
    public data class Url(
      @Required
      public val type: Type = Type.Url,
      public val url: String,
    ) : Annotations {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("url")
        Url("url"),
        ;
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("output_text")
    OutputText("output_text"),
    ;
  }
}
