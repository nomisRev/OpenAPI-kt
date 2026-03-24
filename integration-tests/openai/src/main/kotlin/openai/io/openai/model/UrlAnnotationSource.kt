package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * URL backing an annotation entry.
 */
@Serializable
public data class UrlAnnotationSource(
  @Required
  public val type: Type = Type.Url,
  public val url: String,
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
