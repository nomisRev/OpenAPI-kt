package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Annotation that references a URL.
 */
@Serializable
public data class UrlAnnotation(
  @Required
  public val type: Type = Type.Url,
  public val source: UrlAnnotationSource,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("url")
    Url("url"),
    ;
  }
}
