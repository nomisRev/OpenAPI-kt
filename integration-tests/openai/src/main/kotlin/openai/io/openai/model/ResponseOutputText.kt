package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
    @Serializable
    @JvmInline
    @SerialName("FileAnnotation")
    public value class FileAnnotation(
      public val `value`: io.openai.model.FileAnnotation,
    ) : Annotations

    @Serializable
    @JvmInline
    @SerialName("UrlAnnotation")
    public value class UrlAnnotation(
      public val `value`: io.openai.model.UrlAnnotation,
    ) : Annotations
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
